package br.com.semesperanca.frontendleads.data.remote

import br.com.semesperanca.frontendleads.data.dto.LeadDto
import br.com.semesperanca.frontendleads.data.dto.SpringPageDto
import io.ktor.client.request.parameter

data class LeadFilters(
    val page: Int = 0,
    val size: Int = 20,
    val search: String? = null,
    val status: String? = null,
    val corretorId: String? = null,
    val origin: String? = null,
    val transactionType: String? = null,
)

class LeadApi(private val client: ApiClient) {

    suspend fun searchLeads(filters: LeadFilters): ApiResult<SpringPageDto<LeadDto>> =
        client.get("/leads/search") {
            parameter("page", filters.page)
            parameter("size", filters.size)
            filters.search?.takeIf { it.isNotBlank() }?.let { parameter("search", it) }
            filters.status?.takeIf { it.isNotBlank() }?.let { parameter("status", it) }
            filters.corretorId?.takeIf { it.isNotBlank() }?.let { parameter("corretorId", it) }
            filters.origin?.takeIf { it.isNotBlank() }?.let { parameter("origin", it) }
            filters.transactionType?.takeIf { it.isNotBlank() }?.let { parameter("transactionType", it) }
        }

    suspend fun totalLeads(): ApiResult<Long> =
        when (val result = client.get<SpringPageDto<LeadDto>>("/leads/search") {
            parameter("page", 0)
            parameter("size", 1)
        }) {
            is ApiResult.Success -> ApiResult.Success(result.data.totalElements)
            is ApiResult.Error -> result
        }
}