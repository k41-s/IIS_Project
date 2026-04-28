package hr.algebra.iis_client_app.ui.tabs.soap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.algebra.iis_client_app.api.SoapApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SoapSearchViewModel(private val soapApi: SoapApi) : ViewModel() {
    private val _uiState = MutableStateFlow(SoapSearchUiState())
    val uiState: StateFlow<SoapSearchUiState> = _uiState.asStateFlow()

    fun updateTerm(term: String) {
        _uiState.update { it.copy(searchTerm = term) }
    }

    fun executeSearch() {
        val term = _uiState.value.searchTerm
        _uiState.update { it.copy(isLoading = true, responseText = "Executing SOAP Request...") }
        viewModelScope.launch {
            try {
                val result = soapApi.searchColors(term)
                _uiState.update { it.copy(responseText = result, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(responseText = "SOAP Error: ${e.message}", isLoading = false) }
            }
        }
    }
}
