package hr.algebra.iis_client_app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.algebra.iis_client_app.api.AuthApi
import hr.algebra.iis_client_app.api.models.LoginRequest
import hr.algebra.iis_client_app.AppStateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.Base64

class AuthenticationViewModel(
    private val authApi: AuthApi,
    private val appStateManager: AppStateManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthenticationUiState())
    val uiState: StateFlow<AuthenticationUiState> = _uiState.asStateFlow()

    fun toggleMode() {
        _uiState.update { 
            it.copy(
                isRegisterMode = !it.isRegisterMode, 
                error = null, 
                confirmPassword = "" 
            ) 
        }
    }

    fun updateUsername(username: String) { _uiState.update { it.copy(username = username) } }
    fun updatePassword(password: String) { _uiState.update { it.copy(password = password) } }
    fun updateConfirmPassword(password: String) { _uiState.update { it.copy(confirmPassword = password) } }

    fun submit() {
        val currentState = _uiState.value
        
        if (currentState.username.isBlank() || currentState.password.isBlank()) {
            _uiState.update { it.copy(error = "Fields cannot be empty") }
            return
        }

        if (currentState.isRegisterMode && currentState.password != currentState.confirmPassword) {
            _uiState.update { it.copy(error = "Passwords do not match") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            try {
                val request = LoginRequest(currentState.username, currentState.password)
                val response = if (currentState.isRegisterMode) {
                    authApi.register(request)
                } else {
                    authApi.login(request)
                }

                val resolvedRole = extractRoleFromJwt(response.accessToken)
                appStateManager.setTokensAndRole(response.accessToken, response.refreshToken, resolvedRole)
                
            } catch (e: Exception) {
                val errorMsg = if (currentState.isRegisterMode) {
                    "Registration failed. Username might be taken."
                } else {
                    "Login failed! Incorrect credentials."
                }
                _uiState.update { it.copy(error = errorMsg) }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun extractRoleFromJwt(token: String): String {
        return try {
            val parts = token.split(".")
            if (parts.size == 3) {
                val payload = String(Base64.getUrlDecoder().decode(parts[1]))
                val json = Json.parseToJsonElement(payload) as JsonObject
                json["role"]?.jsonPrimitive?.content ?: "USER"
            } else "USER"
        } catch (e: Exception) {
            "USER"
        }
    }
}
