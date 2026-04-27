package hr.algebra.iis_client_app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import hr.algebra.iis_client_app.api.AuthApi
import hr.algebra.iis_client_app.ui.DashboardScreen
import hr.algebra.iis_client_app.ui.auth.AuthenticationScreen
import hr.algebra.iis_client_app.ui.auth.AuthenticationViewModel

@Composable
fun App() {
    val state by globalAppState.state.collectAsState()

    MaterialTheme {
        if (state.accessToken == null) {
            // Instantiate dependencies (normally handled by a DI framework like Koin)
            val client = remember { createHttpClient(globalAppState) }
            val authApi = remember { AuthApi(client) }
            
            // Traditional KMP ViewModel usage
            val authViewModel = remember { AuthenticationViewModel(authApi, globalAppState) }
            
            AuthenticationScreen(viewModel = authViewModel)
        } else {
            DashboardScreen()
        }
    }
}
