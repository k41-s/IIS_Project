package hr.algebra.iis_client_app.ui.tabs.graphql

import hr.algebra.iis_client_app.api.models.ColorDTO

data class GraphQlColorsUiState(
    val responseText: String = "No data yet",
    val parsedColors: List<ColorDTO> = emptyList(),
    val isLoading: Boolean = false,
    val showCreateDialog: Boolean = false,
    val selectedFields: Set<String> = setOf("id", "name", "year", "color", "pantone_value")
)