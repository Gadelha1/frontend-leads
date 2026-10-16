package br.com.semesperanca.frontendleads.data.remote

import br.com.semesperanca.frontendleads.data.dto.ActivityDto
import br.com.semesperanca.frontendleads.data.dto.CreateActivityRequestDto
import br.com.semesperanca.frontendleads.data.dto.SpringPageDto
import br.com.semesperanca.frontendleads.data.dto.UpdateActivityRequestDto
import io.ktor.client.request.parameter

class ActivityApi(private val client: ApiClient) {

    suspend fun findAll(
        page: Int = 0,
        size: Int = 20,
        search: String? = null,
        leadId: String? = null,
    ): ApiResult<SpringPageDto<ActivityDto>> =
        client.get("/activities") {
            parameter("page", page)
            parameter("size", size)
            search?.takeIf { it.isNotBlank() }?.let { parameter("search", it) }
            leadId?.takeIf { it.isNotBlank() }?.let { parameter("leadId", it) }
        }

    suspend fun findById(id: String): ApiResult<ActivityDto> =
        client.get("/activities/$id")

    suspend fun search(title: String): ApiResult<List<ActivityDto>> =
        client.get("/activities/search") {
            parameter("title", title)
        }

    suspend fun create(request: CreateActivityRequestDto): ApiResult<ActivityDto> =
        client.post("/activities", request)

    suspend fun update(id: String, request: UpdateActivityRequestDto): ApiResult<ActivityDto> =
        client.put("/activities/$id", request)

    suspend fun delete(id: String): ApiResult<Unit> =
        client.delete("/activities/$id")
}
