package br.com.semesperanca.frontendleads.presentation.leads

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal data class LeadDetailUiState(
    val activeTab: DetailTab = DetailTab.ACTIVITY,
    val activities: List<LocalActivity> = emptyList(),
    val tags: List<LocalTag> = emptyList(),
    val showActivitySheet: Boolean = false,
    val showTagPicker: Boolean = false,
    val selectedActivity: LocalActivity? = null,
    val editingActivity: LocalActivity? = null,
)

internal class LeadDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LeadDetailUiState())
    val uiState: StateFlow<LeadDetailUiState> = _uiState.asStateFlow()

    private var idCounter: Int = 0

    fun selectTab(tab: DetailTab) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun openActivityForm() {
        _uiState.update { it.copy(showActivitySheet = true) }
    }

    fun startEditActivity(activity: LocalActivity) {
        _uiState.update { it.copy(editingActivity = activity, selectedActivity = null, showActivitySheet = true) }
    }

    fun deleteActivity(activity: LocalActivity) {
        _uiState.update { it.copy(activities = it.activities.filter { a -> a.id != activity.id }, selectedActivity = null) }
    }

    fun showActivity(activity: LocalActivity) {
        _uiState.update { it.copy(selectedActivity = activity) }
    }

    fun dismissActivityDetail() {
        _uiState.update { it.copy(selectedActivity = null) }
    }

    fun dismissActivitySheet() {
        _uiState.update { it.copy(showActivitySheet = false, editingActivity = null) }
    }

    fun saveActivity(type: String, title: String, scheduledAt: String, description: String) {
        val editing = _uiState.value.editingActivity
        _uiState.update { current ->
            if (editing == null) {
                idCounter += 1
                current.copy(
                    activities = current.activities + LocalActivity(
                        id = "a$idCounter",
                        type = type,
                        title = title,
                        description = description,
                        scheduledAt = scheduledAt.ifBlank { "Agora" },
                        createdAt = "",
                        status = "PENDING",
                    ),
                    showActivitySheet = false,
                    editingActivity = null,
                )
            } else {
                current.copy(
                    activities = current.activities.map {
                        if (it.id == editing.id) {
                            it.copy(
                                type = type,
                                title = title,
                                description = description,
                                scheduledAt = scheduledAt.ifBlank { it.scheduledAt },
                            )
                        } else it
                    },
                    showActivitySheet = false,
                    editingActivity = null,
                )
            }
        }
    }

    fun openTagPicker() {
        _uiState.update { it.copy(showTagPicker = true) }
    }

    fun dismissTagPicker() {
        _uiState.update { it.copy(showTagPicker = false) }
    }

    fun addTag(tag: LocalTag) {
        _uiState.update { it.copy(tags = it.tags + tag) }
    }

    fun removeTag(tagId: String) {
        _uiState.update { it.copy(tags = it.tags.filter { it.id != tagId }) }
    }
}