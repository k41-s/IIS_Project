package hr.algebra.iis_client_app.api.models

import kotlinx.serialization.Serializable

@Serializable
data class GraphQLResponse<T>(
    val data: T? = null,
    val errors: List<GraphQLError>? = null
)