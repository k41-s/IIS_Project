package hr.algebra.iis_client_app.ui.tabs.rest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.algebra.iis_client_app.api.ColorApi
import hr.algebra.iis_client_app.api.ColorDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RestColorsViewModel(
    private val colorApi: ColorApi
) : ViewModel() {
    private val _uiState = MutableStateFlow(RestColorsUiState())
    val uiState: StateFlow<RestColorsUiState> = _uiState.asStateFlow()

    init {
        fetchColors()
    }

    fun fetchColors() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val colors = colorApi.getColors()
                _uiState.update { it.copy(colors = colors, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "REST Error: ${e.message}", isLoading = false) }
            }
        }
    }

    fun showCreateDialog() {
        _uiState.update { it.copy(showCreateDialog = true) }
    }

    fun hideCreateDialog() {
        _uiState.update { it.copy(showCreateDialog = false) }
    }

    fun showUpdateDialog(color: ColorDTO) {
        _uiState.update { it.copy(colorToUpdate = color) }
    }

    fun hideUpdateDialog() {
        _uiState.update { it.copy(colorToUpdate = null) }
    }

    fun createColor(color: ColorDTO) {
        _uiState.update { it.copy(isLoading = true, showCreateDialog = false) }
        viewModelScope.launch {
            try {
                colorApi.createColor(color)
                fetchColors()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to create color: ${e.message}", isLoading = false) }
            }
        }
    }

    fun updateColor(id: Int, color: ColorDTO) {
        _uiState.update { it.copy(isLoading = true, colorToUpdate = null) }
        viewModelScope.launch {
            try {
                colorApi.updateColor(id, color)
                fetchColors()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to update color: ${e.message}", isLoading = false) }
            }
        }
    }

    fun deleteColor(id: Int) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                colorApi.deleteColor(id)
                fetchColors()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Failed to delete color: ${e.message}", isLoading = false) }
            }
        }
    }

    fun showValidateDialog() {
        _uiState.update { it.copy(showValidateDialog = true, validationMessage = null) }
    }

    fun hideValidateDialog() {
        _uiState.update { it.copy(showValidateDialog = false) }
    }

    fun validateAndSave(payload: String, isXml: Boolean) {
        _uiState.update { it.copy(isLoading = true, validationMessage = "Validating...") }
        viewModelScope.launch {
            try {
                colorApi.validateAndSave(payload, isXml)
                _uiState.update { it.copy(validationMessage = "Success: Data validated and saved!", isLoading = false) }
                fetchColors()
            } catch (e: Exception) {
                _uiState.update { it.copy(validationMessage = "Validation Failed: ${e.message}", isLoading = false) }
            }
        }
    }
}
