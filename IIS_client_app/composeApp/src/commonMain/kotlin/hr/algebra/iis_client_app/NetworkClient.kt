package hr.algebra.iis_client_app

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun createHttpClient(appStateManager: AppStateManager): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
        }

        defaultRequest {
            val currentMode = appStateManager.state.value.apiMode.headerValue
            header("X-API-Mode", currentMode)

            appStateManager.state.value.accessToken?.let { token ->
                header(HttpHeaders.Authorization, "Bearer $token")
            }
        }
    }
}