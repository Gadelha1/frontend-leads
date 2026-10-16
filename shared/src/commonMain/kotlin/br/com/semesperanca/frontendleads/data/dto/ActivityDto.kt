package br.com.semesperanca.frontendleads.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ActivityDto(
    val id: String? = null,
    val leadId: String? = null,
    val propertyId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val activityDate: String? = null,
    val completed: Boolean = false,
    val createdAt: String? = null,
)

@Serializable
data class CreateActivityRequestDto(
    val leadId: String,
    val propertyId: String? = null,
    val title: String,
    val description: String,
    val activityDate: String,
)

@Serializable
data class UpdateActivityRequestDto(
    val propertyId: String? = null,
    val title: String,
    val description: String,
    val activityDate: String,
    val completed: Boolean = false,
)
