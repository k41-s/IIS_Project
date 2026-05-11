package hr.algebra.iis_client_app.api

import hr.algebra.iis_client_app.api.models.ColorDTO
import hr.algebra.iis_client_app.api.models.GraphQLRequest
import hr.algebra.iis_client_app.api.models.GraphQLResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.*

class GraphQLApi(private val client: HttpClient) {
    private val url = "http://localhost:8080/graphql"

    suspend fun getAllColors(selectedFields: Set<String>): GraphQLResponse<JsonElement> {
        val fieldsString = selectedFields.joinToString(" ")

        val request = GraphQLRequest(query = "query { getAllColors { $fieldsString } }")

        return client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun createColor(color: ColorDTO): GraphQLResponse<JsonElement> {
        val query = """
            mutation CreateColor(${'$'}input: ColorInput!) {
                createColor(input: ${'$'}input) { id name }
            }
        """.trimIndent()

        val variables = buildJsonObject {
            put("input", buildJsonObject {
                put("id", color.id)
                put("name", color.name)
                put("year", color.year)
                put("color", color.color)
                put("pantone_value", color.pantone_value)
            })
        }

        val request = GraphQLRequest(query = query, variables = variables)
        return client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun deleteColor(id: Int): GraphQLResponse<JsonElement> {
        val query = "mutation { deleteColor(id: \"$id\") }"
        val request = GraphQLRequest(query = query)
        return client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
