package hr.algebra.iis_client_app.api

import hr.algebra.iis_client_app.api.models.AuthResponse
import hr.algebra.iis_client_app.api.models.LoginRequest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthApi(private val client: HttpClient) {
    suspend fun login(request: LoginRequest): AuthResponse {
        return client.post("http://localhost:8080/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun register(request: LoginRequest): AuthResponse {
        return client.post("http://localhost:8080/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
