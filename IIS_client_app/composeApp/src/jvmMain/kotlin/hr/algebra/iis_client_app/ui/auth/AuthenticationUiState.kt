package hr.algebra.iis_client_app.ui.auth

data class AuthenticationUiState(
    val isRegisterMode: Boolean = false,
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)