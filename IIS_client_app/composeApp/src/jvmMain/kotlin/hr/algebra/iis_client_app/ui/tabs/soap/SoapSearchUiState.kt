package hr.algebra.iis_client_app.ui.tabs.soap

data class SoapSearchUiState(
    val searchTerm: String = "",
    val responseText: String = "No result",
    val isLoading: Boolean = false
)