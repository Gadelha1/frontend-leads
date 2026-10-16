package br.com.semesperanca.frontendleads.domain.repository

import br.com.semesperanca.frontendleads.data.dto.LoginResponse
import br.com.semesperanca.frontendleads.data.remote.ApiResult

interface AuthRepository {
    suspend fun login(email: String, password: String): ApiResult<LoginResponse>
}
