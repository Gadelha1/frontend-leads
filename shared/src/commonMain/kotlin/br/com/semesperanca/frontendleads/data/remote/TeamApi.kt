package br.com.semesperanca.frontendleads.data.remote

import br.com.semesperanca.frontendleads.data.dto.CreateTeamRequestDto
import br.com.semesperanca.frontendleads.data.dto.TeamDto
import br.com.semesperanca.frontendleads.data.dto.UpdateTeamRequestDto
import io.ktor.client.request.parameter

class TeamApi(private val client: ApiClient) {

    suspend fun findAll(): ApiResult<List<TeamDto>> =
        client.get("/teams")

    suspend fun findById(id: String): ApiResult<TeamDto> =
        client.get("/teams/$id")

    suspend fun search(name: String): ApiResult<List<TeamDto>> =
        client.get("/teams/search") {
            parameter("name", name)
        }

    suspend fun create(request: CreateTeamRequestDto): ApiResult<TeamDto> =
        client.post("/teams", request)

    suspend fun update(id: String, request: UpdateTeamRequestDto): ApiResult<TeamDto> =
        client.put("/teams/$id", request)

    suspend fun delete(id: String): ApiResult<Unit> =
        client.delete("/teams/$id")

    suspend fun addAgent(teamId: String, agentId: String): ApiResult<Unit> =
        client.post<Unit, Unit>("/teams/$teamId/agents/$agentId", Unit)

    suspend fun removeAgent(teamId: String, agentId: String): ApiResult<Unit> =
        client.delete("/teams/$teamId/agents/$agentId")
}
