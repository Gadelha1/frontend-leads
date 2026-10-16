package br.com.semesperanca.frontendleads.data.remote

import br.com.semesperanca.frontendleads.data.dto.LoginRequest
import br.com.semesperanca.frontendleads.data.dto.LoginResponse

class AuthApi(private val client: ApiClient) {

    suspend fun login(request: LoginRequest): ApiResult<LoginResponse> =
        client.post("/auth/login", request)
}
