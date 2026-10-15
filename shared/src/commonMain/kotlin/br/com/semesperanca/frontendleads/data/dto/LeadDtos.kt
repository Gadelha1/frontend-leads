package br.com.semesperanca.frontendleads.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LeadDto(
    val id: String? = null,
    val realEstateAgencyId: String? = null,
    val realEstateAgentId: String? = null,
    val portalId: String? = null,
    val funnelStageId: String? = null,
    val originLeadId: String? = null,
    val originListingId: String? = null,
    val leadOrigin: String? = null,
    val clientListingId: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phoneNumber: Long? = null,
    val message: String? = null,
    val temperature: String? = null,
    val transactionType: String? = null,
    val source: String? = null,
    val status: String? = null,
    val insightId: String? = null,
    val timestamp: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
)

@Serializable
data class SpringPageDto<T>(
    val content: List<T> = emptyList(),
    val totalElements: Long = 0,
    val totalPages: Int = 0,
    val number: Int = 0,
    val size: Int = 0,
    val first: Boolean = true,
    val last: Boolean = true,
    val empty: Boolean = true,
)