package hr.algebra.iis_client_app.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class ColorApi(private val client: HttpClient) {
    private val baseUrl = "http://localhost:8080/api"

    suspend fun getColors(): List<ColorDTO> {
        return client.get("$baseUrl/colors").body()
    }

    suspend fun createColor(color: ColorDTO): ColorDTO {
        return client.post("$baseUrl/colors") {
            contentType(ContentType.Application.Json)
            setBody(color)
        }.body()
    }

    suspend fun updateColor(id: Int, color: ColorDTO): ColorDTO {
        return client.put("$baseUrl/colors/$id") {
            contentType(ContentType.Application.Json)
            setBody(color)
        }.body()
    }

    suspend fun deleteColor(id: Int) {
        client.delete("$baseUrl/colors/$id")
    }

    suspend fun validateAndSave(payload: String, isXml: Boolean = false) {
        val type = if (isXml) ContentType.Text.Xml else ContentType.Application.Json
        client.post("$baseUrl/colors/validate-and-save") {
            contentType(type)
            setBody(payload)
        }
    }

    suspend fun validateGeneratedXml(): List<String> {
        return client.get("$baseUrl/validation/validate-generated-xml").body()
    }
}
