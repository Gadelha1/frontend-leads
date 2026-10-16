package br.com.semesperanca.frontendleads.presentation.users

data class UserUi(
    val name: String,
    val email: String,
    val role: UserRole,
    val creci: String? = null,
    val phone: String? = null,
    val branchOfficeId: String? = null,
    val realEstateAgencyId: String? = null,
)
