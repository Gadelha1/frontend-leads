package br.com.semesperanca.frontendleads.data.repository

import br.com.semesperanca.frontendleads.data.remote.ApiResult
import br.com.semesperanca.frontendleads.data.remote.LeadApi
import br.com.semesperanca.frontendleads.data.remote.LeadFilters
import br.com.semesperanca.frontendleads.domain.repository.LeadPage
import br.com.semesperanca.frontendleads.domain.repository.LeadRepository

class DefaultLeadRepository(private val leadApi: LeadApi) : LeadRepository {
    override suspend fun searchLeads(filters: LeadFilters): ApiResult<LeadPage> =
        when (val result = leadApi.searchLeads(filters)) {
            is ApiResult.Success -> ApiResult.Success(
                LeadPage(result.data.content, result.data.totalElements)
            )
            is ApiResult.Error -> result
        }

    override suspend fun totalLeads(): ApiResult<Long> = leadApi.totalLeads()
}
