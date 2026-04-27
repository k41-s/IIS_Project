package hr.algebra.iis_client_app.api

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)

@Serializable
data class ColorDTO(
    val internalId: Long? = null,
    val id: Int,
    val name: String,
    val year: Int,
    val color: String,
    val pantone_value: String
)

@Serializable
data class GraphQLRequest(
    val query: String,
    val variables: JsonElement? = null
)

@Serializable
data class GraphQLResponse<T>(
    val data: T? = null,
    val errors: List<GraphQLError>? = null
)

@Serializable
data class GraphQLError(
    val message: String
)

@Serializable
data class RefreshRequest(
    val refreshToken: String
)