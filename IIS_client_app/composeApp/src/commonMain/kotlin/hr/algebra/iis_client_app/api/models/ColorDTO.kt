package hr.algebra.iis_client_app.api.models

import kotlinx.serialization.Serializable

@Serializable
data class ColorDTO(
    val internalId: Long? = null,
    val id: Int = 0,
    val name: String = "",
    val year: Int = 0,
    val color: String = "",
    val pantone_value: String = ""
)