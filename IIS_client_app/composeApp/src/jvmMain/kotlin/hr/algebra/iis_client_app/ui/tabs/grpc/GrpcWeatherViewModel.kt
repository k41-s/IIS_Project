package hr.algebra.iis_client_app.ui.tabs.grpc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.algebra.iis_client_app.api.WeatherGrpcClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GrpcWeatherUiState(
    val cityName: String = "Zagreb",
    val responseText: String = "Enter a city to call gRPC",
    val isLoading: Boolean = false
)

class GrpcWeatherViewModel(private val grpcClient: WeatherGrpcClient) : ViewModel() {
    private val _uiState = MutableStateFlow(GrpcWeatherUiState())
    val uiState: StateFlow<GrpcWeatherUiState> = _uiState.asStateFlow()

    fun updateCityName(city: String) {
        _uiState.update { it.copy(cityName = city) }
    }

    fun getTemperature() {
        val city = _uiState.value.cityName
        _uiState.update { it.copy(isLoading = true, responseText = "Connecting to gRPC...") }
        viewModelScope.launch {
            try {
                val weatherList = grpcClient.getTemperature(city)
                val resultText = if (weatherList.isEmpty()) {
                    "No results returned."
                } else {
                    weatherList.joinToString("\n") { weather -> 
                        "gRPC Result -> City: ${weather.cityName}, Temp: ${weather.temp}" 
                    }
                }
                _uiState.update { it.copy(responseText = resultText, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(responseText = "gRPC Connection Error: ${e.message}", isLoading = false) }
            }
        }
    }
}
