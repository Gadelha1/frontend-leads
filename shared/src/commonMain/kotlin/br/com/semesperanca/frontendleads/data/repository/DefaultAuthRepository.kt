package br.com.semesperanca.frontendleads.data.repository

import br.com.semesperanca.frontendleads.data.dto.LoginRequest
import br.com.semesperanca.frontendleads.data.dto.LoginResponse
import br.com.semesperanca.frontendleads.data.remote.ApiResult
import br.com.semesperanca.frontendleads.data.remote.AuthApi
import br.com.semesperanca.frontendleads.domain.repository.AuthRepository

class DefaultAuthRepository(private val authApi: AuthApi) : AuthRepository {
    override suspend fun login(email: String, password: String): ApiResult<LoginResponse> =
        authApi.login(LoginRequest(email = email.trim(), password = password))
}
