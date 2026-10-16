package br.com.semesperanca.frontendleads.presentation.sensors

import br.com.semesperanca.frontendleads.presentation.ui.form.FormField

val sensorFormFields: List<FormField> = listOf(
    FormField(
        key = "propertyId",
        label = "Imóvel (ID)",
        required = true,
        placeholder = "UUID do imóvel",
    ),
    FormField(
        key = "deviceId",
        label = "Dispositivo",
        required = true,
        placeholder = "ex.: SENSOR-01",
        maxLength = 64,
    ),
)

fun sensorFormDefaults(): Map<String, String> = emptyMap()
