package br.com.semesperanca.frontendleads

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

actual val apiBaseUrl: String = "http://localhost:8080"

actual fun createHttpClientEngine(): HttpClientEngine = Js.create()
