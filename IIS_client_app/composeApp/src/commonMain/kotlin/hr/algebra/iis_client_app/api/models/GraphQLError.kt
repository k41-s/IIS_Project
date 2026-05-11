package hr.algebra.iis_client_app.api.models

import kotlinx.serialization.Serializable

@Serializable
data class GraphQLError(
    val message: String
)