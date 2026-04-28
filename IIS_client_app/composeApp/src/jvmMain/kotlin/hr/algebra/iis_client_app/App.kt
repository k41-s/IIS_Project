package hr.algebra.iis_client_app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import hr.algebra.iis_client_app.api.AuthApi
import hr.algebra.iis_client_app.ui.DashboardScreen
import hr.algebra.iis_client_app.ui.auth.AuthenticationScreen
import hr.algebra.iis_client_app.ui.auth.AuthenticationViewModel
import hr.algebra.iis_client_app.ui.theme.AppTheme

@Composable
fun App() {
    val state by globalAppState.state.collectAsState()

    AppTheme(themeMode = state.themeMode) {
        // By wrapping the entire app in a Surface, we guarantee the background and default text colors 
        // strictly follow the active MaterialTheme's colorScheme across all screens!
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
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
}
