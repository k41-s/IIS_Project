package hr.algebra.iis_client_app.ui.tabs.rest

import hr.algebra.iis_client_app.api.models.ColorDTO

data class RestColorsUiState(
    val colors: List<ColorDTO> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showCreateDialog: Boolean = false,
    val colorToUpdate: ColorDTO? = null,
    val showValidateDialog: Boolean = false,
    val validationMessage: String? = null,
    val snackbarMessage: String? = null
)
