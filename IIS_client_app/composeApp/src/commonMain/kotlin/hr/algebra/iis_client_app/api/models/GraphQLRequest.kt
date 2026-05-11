package hr.algebra.iis_client_app.api.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class GraphQLRequest(
    val query: String,
    val variables: JsonElement? = null
)