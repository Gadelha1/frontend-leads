package br.com.semesperanca.frontendleads.data.dto

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class PropertySensorDto(
    val id: String? = null,
    val propertyId: String? = null,
    val deviceId: String? = null,
    val sensorType: String? = null,
    val label: String? = null,
    val isActive: Boolean = true,
    val installedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime? = null,
)

@Serializable
data class SensorMetricDto(
    val id: String? = null,
    val deviceId: String? = null,
    val temperature: Double? = null,
    val humidity: Double? = null,
    val luminosity: Double? = null,
    val batteryLevel: Int? = null,
    val isOnline: Boolean = true,
    val recordedAt: LocalDateTime? = null,
)

@Serializable
data class BindPropertySensorRequestDto(
    val propertyId: String,
    val deviceId: String,
)

@Serializable
data class PropertySensorResponseDto(
    val propertyId: String,
    val deviceId: String,
    val createdAt: Instant? = null,
)

@Serializable
data class SensorReadingDto(
    val deviceId: String,
    val temperature: Double? = null,
    val humidity: Double? = null,
    val luminosity: Double? = null,
    val recordedAt: Instant? = null,
)

@Serializable
data class SensorPropertyMetricDto(
    val propertyId: String,
    val propertyTitle: String? = null,
    val deviceId: String,
    val temperature: Double? = null,
    val humidity: Double? = null,
    val luminosity: Double? = null,
    val recordedAt: Instant? = null,
)

@Serializable
data class HourlySensorMetricDto(
    val bucketStart: Instant? = null,
    val sampleCount: Int = 0,
    val temperatureAvg: Double? = null,
    val temperatureMin: Double? = null,
    val temperatureMax: Double? = null,
    val humidityAvg: Double? = null,
    val humidityMin: Double? = null,
    val humidityMax: Double? = null,
    val luminosityAvg: Double? = null,
    val luminosityMin: Double? = null,
    val luminosityMax: Double? = null,
    val partial: Boolean = false,
)

@Serializable
data class SensorPropertyMetricDetailDto(
    val propertyId: String,
    val propertyTitle: String? = null,
    val deviceId: String,
    val latest: SensorReadingDto? = null,
    val history: List<HourlySensorMetricDto> = emptyList(),
)
