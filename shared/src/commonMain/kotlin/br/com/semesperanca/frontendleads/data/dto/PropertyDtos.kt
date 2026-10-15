package br.com.semesperanca.frontendleads.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreatePropertyRequestDto(
    val title: String,
    val description: String? = null,
    val price: Double? = null,
    val propertyType: String? = null,
    val bedrooms: Int? = null,
    val bathrooms: Int? = null,
    val areaSqm: Double? = null,
    val address: String? = null,
    val city: String? = null,
    val state: String? = null,
    val zipCode: String? = null,
    val branchOfficeId: String,
    val status: String? = "available",
)
