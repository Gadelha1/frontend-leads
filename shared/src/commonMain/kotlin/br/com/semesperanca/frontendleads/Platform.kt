package br.com.semesperanca.frontendleads

expect val apiBaseUrl: String
expect fun createHttpClientEngine(): io.ktor.client.engine.HttpClientEngine
