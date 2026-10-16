package br.com.semesperanca.frontendleads.domain.repository

import br.com.semesperanca.frontendleads.data.dto.LeadDto
import br.com.semesperanca.frontendleads.data.remote.ApiResult
import br.com.semesperanca.frontendleads.data.remote.LeadFilters

data class LeadPage(
    val leads: List<LeadDto>,
    val total: Long,
)

interface LeadRepository {
    suspend fun searchLeads(filters: LeadFilters): ApiResult<LeadPage>
    suspend fun totalLeads(): ApiResult<Long>
}
