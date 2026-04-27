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
    val refreshToken: String? = null,
    val role: UserRole = UserRole.GUEST,
    val apiMode: ApiMode = ApiMode.PUBLIC
)

class AppStateManager {
    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state.asStateFlow()


    init {
        val savedAccess = TokenStorage.getAccessToken()
        val savedRefresh = TokenStorage.getRefreshToken()
        val savedRole = TokenStorage.getRole()

        if (savedAccess != null && savedRefresh != null && savedRole != null) {
            setTokensAndRole(savedAccess, savedRefresh, savedRole)
        }
    }

    fun setTokensAndRole(accessToken: String, refreshToken: String, role: String) {
        val parsedRole = when (role.uppercase()) {
            "ADMIN", "FULL_ACCESS" -> UserRole.FULL_ACCESS
            "USER", "READ_ONLY" -> UserRole.READ_ONLY
            else -> UserRole.GUEST
        }

        TokenStorage.saveTokens(accessToken, refreshToken, parsedRole.name)

        _state.value = _state.value.copy(
            accessToken = accessToken,
            refreshToken = refreshToken,
            role = parsedRole
        )
    }

    fun logout() {
        TokenStorage.clear()
        _state.value = _state.value.copy(
            accessToken = null,
            refreshToken = null,
            role = UserRole.GUEST
        )
    }

    fun toggleApiMode() {
        val newMode = if (_state.value.apiMode == ApiMode.PUBLIC) ApiMode.CUSTOM else ApiMode.PUBLIC
        _state.value = _state.value.copy(apiMode = newMode)
    }
}

val globalAppState = AppStateManager()
