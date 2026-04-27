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

data class RestColorsUiState(
    val colors: List<ColorDTO> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val showCreateDialog: Boolean = false,
    val colorToUpdate: ColorDTO? = null
)

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

    fun showDialog() {
        _uiState.update { it.copy(showCreateDialog = true) }
    }

    fun hideDialog() {
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
                fetchColors() // Refresh list with newly created color
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
}
