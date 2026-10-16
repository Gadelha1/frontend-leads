package br.com.semesperanca.frontendleads.data.dto

import kotlinx.serialization.Serializable

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
