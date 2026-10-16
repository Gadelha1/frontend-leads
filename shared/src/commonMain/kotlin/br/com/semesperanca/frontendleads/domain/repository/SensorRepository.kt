package br.com.semesperanca.frontendleads.domain.repository

import br.com.semesperanca.frontendleads.data.dto.PropertySensorResponseDto
import br.com.semesperanca.frontendleads.data.dto.SensorPropertyMetricDetailDto
import br.com.semesperanca.frontendleads.data.dto.SensorPropertyMetricDto
import br.com.semesperanca.frontendleads.data.remote.ApiResult

interface SensorRepository {
    suspend fun bindSensor(propertyId: String, deviceId: String): ApiResult<PropertySensorResponseDto>
    suspend fun unbindSensor(propertyId: String): ApiResult<Unit>
    suspend fun listMetrics(propertyId: String? = null): ApiResult<List<SensorPropertyMetricDto>>
    suspend fun metricsDetail(propertyId: String, hours: Int = 24): ApiResult<SensorPropertyMetricDetailDto>
}
