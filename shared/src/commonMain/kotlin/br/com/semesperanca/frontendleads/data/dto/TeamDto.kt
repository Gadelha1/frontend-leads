package br.com.semesperanca.frontendleads.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TeamDto(
    val id: String? = null,
    val managerId: String? = null,
    val branchOfficeId: String? = null,
    val name: String? = null,
    val description: String? = null,
    val active: Boolean = true,
    val maxAgents: Int? = null,
    val goalAmount: Double? = null,
    val createdAt: String? = null,
)

@Serializable
data class CreateTeamRequestDto(
    val managerId: String,
    val name: String,
    val description: String? = null,
    val maxAgents: Int? = null,
    val goalAmount: Double? = null,
)

@Serializable
data class UpdateTeamRequestDto(
    val managerId: String? = null,
    val name: String,
    val description: String? = null,
    val maxAgents: Int? = null,
    val goalAmount: Double? = null,
)
