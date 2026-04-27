package hr.algebra.iis_client_app.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun AuthenticationScreen(viewModel: AuthenticationViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.width(320.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = if (uiState.isRegisterMode) "Create Account" else "IIS Unified Client",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.username,
                onValueChange = viewModel::updateUsername,
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::updatePassword,
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = if (uiState.isRegisterMode) ImeAction.Next else ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (!uiState.isRegisterMode) {
                            focusManager.clearFocus()
                            viewModel.submit()
                        }
                    }
                )
            )

            if (uiState.isRegisterMode) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.confirmPassword,
                    onValueChange = viewModel::updateConfirmPassword,
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            viewModel.submit()
                        }
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            uiState.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.submit()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(if (uiState.isRegisterMode) "Register" else "Sign In")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = viewModel::toggleMode,
                enabled = !uiState.isLoading
            ) {
                Text(if (uiState.isRegisterMode) "Already have an account? Sign In" else "Don't have an account? Register")
            }
        }
    }
}
