package hr.algebra.iis_client_app

import hr.algebra.iis_client_app.api.AuthResponse
import hr.algebra.iis_client_app.api.RefreshRequest
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
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

        install(Auth) {
            bearer {
                loadTokens {
                    val state = appStateManager.state.value
                    if (state.accessToken != null && state.refreshToken != null) {
                        BearerTokens(state.accessToken, state.refreshToken)
                    } else null
                }

                refreshTokens {
                    val oldRefreshToken = oldTokens?.refreshToken ?: return@refreshTokens null

                    val refreshClient = HttpClient(CIO) {
                        install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
                    }

                    try {
                        val response: AuthResponse = refreshClient.post("http://localhost:8080/api/auth/refresh") {
                            contentType(ContentType.Application.Json)
                            setBody(RefreshRequest(oldRefreshToken))
                        }.body()

                        appStateManager.setTokensAndRole(
                            response.accessToken,
                            response.refreshToken,
                            appStateManager.state.value.role.name
                        )

                        BearerTokens(response.accessToken, response.refreshToken)
                    } catch (e: Exception) {
                        appStateManager.logout()
                        null
                    } finally {
                        refreshClient.close()
                    }
                }
            }
        }

        defaultRequest {
            val currentMode = appStateManager.state.value.apiMode.headerValue
            header("X-API-Mode", currentMode)
        }
    }
}