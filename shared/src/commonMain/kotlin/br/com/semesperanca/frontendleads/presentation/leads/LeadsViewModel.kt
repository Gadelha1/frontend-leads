package br.com.semesperanca.frontendleads.presentation.leads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.semesperanca.frontendleads.data.remote.ApiResult
import br.com.semesperanca.frontendleads.data.remote.LeadFilters
import br.com.semesperanca.frontendleads.domain.repository.LeadRepository
import br.com.semesperanca.frontendleads.data.storage.KeyValueStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class LeadOverrides(
    val isFavorite: Boolean? = null,
    val isArchived: Boolean? = null,
)

data class LeadsUiState(
    val leads: List<LeadUi> = emptyList(),
    val cacheLeads: List<LeadUi> = emptyList(),
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val activeFilter: LeadFilter = LeadFilter.TODOS,
    val hasNewLeads: Boolean = false,
    val newLeadsCount: Int = 0,
    val hasMore: Boolean = false,
) {
    val displayLeads: List<LeadUi>
        get() = if (leads.isNotEmpty() || !isLoading) leads else cacheLeads
}

class LeadsViewModel(
    private val repo: LeadRepository,
    private val store: KeyValueStore,
) : ViewModel() {

    companion object {
        private const val OVERRIDES_KEY = "lead_overrides"
        private const val SEEN_KEY = "seen_lead_ids"
        private const val PAGE_SIZE = 10
        private const val BACKEND_PAGE_SIZE = 1000
        private const val MIN_LOAD_MORE_MS = 400L
    }

    private val json = Json { ignoreUnknownKeys = true }

    private val _uiState = MutableStateFlow(LeadsUiState())
    val uiState: StateFlow<LeadsUiState> = _uiState.asStateFlow()

    private var page = 1
    private var overrides: Map<String, LeadOverrides> = decodeOverrides(store.get(OVERRIDES_KEY))
    private val seenLeadIds: MutableSet<String> = decodeSeen(store.get(SEEN_KEY)).toMutableSet()
    private var lastKnownTotal: Long? = null
    private var searchJob: Job? = null
    private var requestInFlight = false

    init {
        fetchLeadsAction()
    }

    fun fetchLeadsAction(refresh: Boolean = true) {
        if (requestInFlight) return
        searchJob?.cancel()
        requestInFlight = true
        viewModelScope.launch {
            val query = _uiState.value.searchQuery
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isLoadingMore = false,
                    isRefreshing = refresh,
                    error = null,
                )
            }
            when (val result = fetchRemoteLeads(query)) {
                is ApiResult.Success -> {
                    val all = applyLocalFilters(
                        applyOverrides(result.data.leads).filter { it.matchesSearch(query) },
                        _uiState.value.activeFilter,
                    )
                    val slice = all.take(PAGE_SIZE)
                    lastKnownTotal = result.data.total
                    _uiState.update {
                        it.copy(
                            leads = slice.map { it.withNewFlag() },
                            cacheLeads = if (slice.isNotEmpty()) slice.map { it.withNewFlag() } else it.cacheLeads,
                            isLoading = false,
                            isRefreshing = false,
                            hasMore = all.size > PAGE_SIZE,
                            hasNewLeads = false,
                            newLeadsCount = 0,
                        )
                    }
                }
                is ApiResult.Error -> _uiState.update {
                    it.copy(
                        error = "Erro ao carregar leads",
                        isLoading = false,
                        isRefreshing = false,
                    )
                }
            }
            requestInFlight = false
        }
    }

    fun onSearchChange(value: String) {
        _uiState.update { it.copy(searchQuery = value) }
        page = 1
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            fetchLeadsAction(refresh = true)
        }
    }

    fun setActiveFilter(filter: LeadFilter) {
        if (_uiState.value.activeFilter == filter) return
        _uiState.update {
            it.copy(
                activeFilter = filter,
                leads = emptyList(),
                cacheLeads = emptyList(),
                isLoading = true,
                isRefreshing = false,
            )
        }
        page = 1
        fetchLeadsAction(refresh = true)
    }

    fun refresh() {
        clearNewLeadsFlag()
        page = 1
        fetchLeadsAction(refresh = true)
    }

    fun clearNewLeadsFlag() {
        _uiState.update { it.copy(hasNewLeads = false, newLeadsCount = 0) }
    }

    fun checkForNewLeads() {
        viewModelScope.launch {
            when (val result = repo.totalLeads()) {
                is ApiResult.Success -> {
                    val last = lastKnownTotal
                    if (last != null && result.data > last) {
                        _uiState.update {
                            it.copy(
                                newLeadsCount = (result.data - last).toInt(),
                                hasNewLeads = true,
                            )
                        }
                    }
                }
                is ApiResult.Error -> Unit
            }
        }
    }

    fun loadMore() {
        val state = _uiState.value
        if (!state.hasMore || state.isLoading || state.isLoadingMore) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }
            delay(MIN_LOAD_MORE_MS)
            val nextPage = page + 1
            val query = state.searchQuery
            when (val result = fetchRemoteLeads(query)) {
                is ApiResult.Success -> {
                    val all = applyLocalFilters(
                        applyOverrides(result.data.leads).filter { it.matchesSearch(query) },
                        _uiState.value.activeFilter,
                    )
                    val start = page * PAGE_SIZE
                    val slice = all.drop(start).take(PAGE_SIZE)
                    page = nextPage
                    _uiState.update {
                        it.copy(
                            leads = it.leads + slice.map { it.withNewFlag() },
                            isLoadingMore = false,
                            hasMore = all.size > start + PAGE_SIZE,
                        )
                    }
                }
                is ApiResult.Error -> _uiState.update {
                    it.copy(error = "Erro ao carregar mais leads", isLoadingMore = false)
                }
            }
        }
    }

    fun toggleFavorite(id: String) {
        val state = _uiState.value
        val lead = state.leads.firstOrNull { it.id == id } ?: return
        val newFav = !lead.isFavorite
        overrides = overrides + (id to LeadOverrides(
            isFavorite = newFav,
            isArchived = overrides[id]?.isArchived,
        ))
        persistOverrides()
        val keeps = state.activeFilter == LeadFilter.FAVORITOS && !newFav
        val updatedLeads = if (keeps) state.leads.filter { it.id != id } else state.leads.map { if (it.id == id) it.copy(isFavorite = newFav) else it }
        val updatedCache = if (keeps) state.cacheLeads.filter { it.id != id } else state.cacheLeads.map { if (it.id == id) it.copy(isFavorite = newFav) else it }
        _uiState.update { it.copy(leads = updatedLeads, cacheLeads = updatedCache) }
    }

    fun archiveLead(id: String) {
        overrides = overrides + (id to LeadOverrides(
            isArchived = true,
            isFavorite = overrides[id]?.isFavorite,
        ))
        persistOverrides()
        _uiState.update {
            it.copy(
                leads = it.leads.filter { l -> l.id != id },
                cacheLeads = it.cacheLeads.filter { l -> l.id != id },
            )
        }
    }

    fun unarchiveLead(id: String) {
        overrides = overrides + (id to LeadOverrides(
            isArchived = false,
            isFavorite = overrides[id]?.isFavorite,
        ))
        persistOverrides()
        val state = _uiState.value
        val keeps = state.activeFilter == LeadFilter.ARQUIVADOS
        val updated = state.leads.map { if (it.id == id) it.copy(isArchived = false) else it }
        val updatedCache = state.cacheLeads.map { if (it.id == id) it.copy(isArchived = false) else it }
        _uiState.update {
            it.copy(
                leads = if (keeps) updated.filterNot { it.id == id } else updated,
                cacheLeads = if (keeps) updatedCache.filterNot { it.id == id } else updatedCache,
            )
        }
    }

    fun markLeadAsSeen(id: String) {
        if (id in seenLeadIds) return
        seenLeadIds += id
        persistSeen()
        _uiState.update {
            it.copy(
                leads = it.leads.map { if (it.id == id) it.copy(isNew = false) else it },
                cacheLeads = it.cacheLeads.map { if (it.id == id) it.copy(isNew = false) else it },
            )
        }
    }

    private suspend fun fetchRemoteLeads(query: String): ApiResult<FetchedLeads> =
        when (val result = repo.searchLeads(LeadFilters(page = 0, size = BACKEND_PAGE_SIZE, search = query))) {
            is ApiResult.Success -> ApiResult.Success(
                FetchedLeads(result.data.leads.map { it.toLeadUi() }, result.data.total)
            )
            is ApiResult.Error -> result
        }

    private fun applyOverrides(leads: List<LeadUi>): List<LeadUi> =
        leads.map { lead ->
            val o = overrides[lead.id]
            if (o == null) lead
            else lead.copy(
                isFavorite = o.isFavorite ?: lead.isFavorite,
                isArchived = o.isArchived ?: lead.isArchived,
            )
        }

    private fun LeadUi.withNewFlag(): LeadUi = copy(isNew = !seenLeadIds.contains(id))

    private fun persistOverrides() {
        store.set(OVERRIDES_KEY, json.encodeToString(overrides))
    }

    private fun persistSeen() {
        store.set(SEEN_KEY, json.encodeToString(seenLeadIds.toList()))
    }

    private fun decodeOverrides(raw: String?): Map<String, LeadOverrides> =
        raw?.takeIf { it.isNotBlank() }?.let {
            runCatching { json.decodeFromString<Map<String, LeadOverrides>>(it) }.getOrNull()
        } ?: emptyMap()

    private fun decodeSeen(raw: String?): Set<String> =
        raw?.takeIf { it.isNotBlank() }?.let {
            runCatching { json.decodeFromString<List<String>>(it) }.getOrNull()
        }?.toSet() ?: emptySet()

    private data class FetchedLeads(
        val leads: List<LeadUi>,
        val total: Long,
    )
}