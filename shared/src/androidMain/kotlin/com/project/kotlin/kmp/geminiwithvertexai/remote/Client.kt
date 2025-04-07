package com.project.kotlin.kmp.geminiwithvertexai.remote

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.ConnectionSpec

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
    config(this)
    val connectionSpecs: ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).build()

    engine {
        config {
            cache(null)
            connectionSpecs(listOf(connectionSpecs))
        }
    }
}