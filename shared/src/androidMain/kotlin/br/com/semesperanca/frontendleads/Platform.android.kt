package br.com.semesperanca.frontendleads

import android.content.Context
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual val apiBaseUrl: String = "http://10.0.2.2:8080"

actual fun createHttpClientEngine(): HttpClientEngine = OkHttp.create()

object AndroidContextHolder {
    lateinit var appContext: Context
        private set

    fun init(context: Context) {
        appContext = context.applicationContext
    }
}
