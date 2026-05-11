package hr.algebra.iis_client_app.ui.tabs.graphql

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.algebra.iis_client_app.api.models.ColorDTO
import hr.algebra.iis_client_app.api.GraphQLApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.decodeFromJsonElement

class GraphQlColorsViewModel(
    private val graphQLApi: GraphQLApi
) : ViewModel() {
    private val _uiState = MutableStateFlow(GraphQlColorsUiState())
    val uiState: StateFlow<GraphQlColorsUiState> = _uiState.asStateFlow()
    private val jsonFormat = Json { ignoreUnknownKeys = true }
    private val prettyJson = Json { prettyPrint = true }

    fun toggleField(field: String) {
        _uiState.update { state ->
            val newFields = if (state.selectedFields.contains(field)) {
                state.selectedFields - field
            } else {
                state.selectedFields + field
            }
            if (newFields.isEmpty()) state else state.copy(selectedFields = newFields)
        }
    }

    fun getAllColors() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = graphQLApi.getAllColors(_uiState.value.selectedFields)
                val responseStr = prettyJson.encodeToString(response)

                val dataArray = response.data?.jsonObject?.get("getAllColors")
                val parsed = if (dataArray is JsonArray) {
                    jsonFormat.decodeFromJsonElement<List<ColorDTO>>(dataArray)
                } else emptyList()
                
                _uiState.update { it.copy(responseText = responseStr, parsedColors = parsed, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(responseText = "Error: ${e.message}", parsedColors = emptyList(), isLoading = false) }
            }
        }
    }

    fun showDialog() {
        _uiState.update { it.copy(showCreateDialog = true) }
    }

    fun hideDialog() {
        _uiState.update { it.copy(showCreateDialog = false) }
    }

    fun createColor(color: ColorDTO) {
        _uiState.update { it.copy(isLoading = true, showCreateDialog = false) }
        viewModelScope.launch {
            try {
                val response = graphQLApi.createColor(color)
                _uiState.update { it.copy(responseText = prettyJson.encodeToString(response)) }
                getAllColors()
            } catch (e: Exception) {
                _uiState.update { it.copy(responseText = "Error: ${e.message}", isLoading = false) }
            }
        }
    }

    fun deleteColor(id: Int) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val response = graphQLApi.deleteColor(id)
                _uiState.update { it.copy(responseText = prettyJson.encodeToString(response)) }
                getAllColors()
            } catch (e: Exception) {
                _uiState.update { it.copy(responseText = "Error: ${e.message}", isLoading = false) }
            }
        }
    }
}
