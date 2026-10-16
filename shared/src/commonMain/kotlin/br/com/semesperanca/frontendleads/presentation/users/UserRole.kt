package br.com.semesperanca.frontendleads.presentation.users

enum class UserRole(val value: String, val label: String) {
    REAL_ESTATE_AGENT("REAL_ESTATE_AGENT", "Corretor"),
    MANAGER("MANAGER", "Gerente"),
    BRANCH_OWNER("ADMIN", "Responsável"),
}
