package hr.algebra.iis_client_app

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ApiMode(val headerValue: String) {
    PUBLIC("public"),
    CUSTOM("custom")
}

enum class UserRole {
    READ_ONLY,
    FULL_ACCESS,
    GUEST;
    
    val canEdit: Boolean
        get() = this == FULL_ACCESS
}

data class AppState(
    val accessToken: String? = null,
    val role: UserRole = UserRole.GUEST,
    val apiMode: ApiMode = ApiMode.PUBLIC
)

class AppStateManager {
    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state.asStateFlow()

    fun setTokenAndRole(token: String, role: String) {
        val parsedRole = when (role.uppercase()) {
            "ADMIN", "FULL_ACCESS" -> UserRole.FULL_ACCESS
            "USER", "READ_ONLY" -> UserRole.READ_ONLY
            else -> UserRole.GUEST
        }
        _state.value = _state.value.copy(
            accessToken = token,
            role = parsedRole
        )
    }

    fun logout() {
        _state.value = _state.value.copy(
            accessToken = null,
            role = UserRole.GUEST
        )
    }

    fun toggleApiMode() {
        val newMode = if (_state.value.apiMode == ApiMode.PUBLIC) ApiMode.CUSTOM else ApiMode.PUBLIC
        _state.value = _state.value.copy(apiMode = newMode)
    }
}

// Global instance (can also be provided via DI like Koin/Dagger)
val globalAppState = AppStateManager()
