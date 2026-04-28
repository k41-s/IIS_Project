package hr.algebra.iis_client_app.ui.tabs.rest

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.algebra.iis_client_app.ApiMode
import hr.algebra.iis_client_app.api.*
import hr.algebra.iis_client_app.createHttpClient
import hr.algebra.iis_client_app.globalAppState

@Composable
fun RestColorsTab() {
    val client = remember { createHttpClient(globalAppState) }
    val colorApi = remember { ColorApi(client) }
    val viewModel = remember { RestColorsViewModel(colorApi) }
    val uiState by viewModel.uiState.collectAsState()
    val state by globalAppState.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.apiMode) {
        viewModel.fetchColors()
    }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbarMessage()
        }
    }

    if (uiState.showCreateDialog) {
        CreateColorDialog(
            onDismiss = viewModel::hideCreateDialog,
            onCreate = viewModel::createColor
        )
    }

    if (uiState.showValidateDialog) {
        ValidateAndSaveDialog(
            onDismiss = viewModel::hideValidateDialog,
            onSubmit = viewModel::validateAndSave,
            validationMessage = uiState.validationMessage
        )
    }

    if (uiState.colorToUpdate != null) {
        UpdateColorDialog(
            color = uiState.colorToUpdate!!,
            onDismiss = viewModel::hideUpdateDialog,
            onUpdate = { updatedColor -> viewModel.updateColor(updatedColor.id, updatedColor) }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                
                Text("REST Colors API", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = viewModel::fetchColors, enabled = !uiState.isLoading) {
                        Text("Refresh")
                    }

                    if (state.role.canEdit && state.apiMode == ApiMode.CUSTOM) {
                        Button(
                            onClick = viewModel::showCreateDialog,
                            enabled = !uiState.isLoading
                        ) { Text("Create Color") }

                        Button(
                            onClick = viewModel::showValidateDialog,
                            enabled = !uiState.isLoading
                        ) { Text("Validate & Save Payload") }
                    }
                }
            }

            uiState.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.colors) { color ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("ID: ${color.id} - ${color.name}", style = MaterialTheme.typography.titleMedium)
                                    Text("Year: ${color.year} | Hex: ${color.color} | Pantone: ${color.pantone_value}")
                                }

                                if (state.role.canEdit && state.apiMode == ApiMode.CUSTOM) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = { viewModel.showUpdateDialog(color) }
                                        ) { Text("Update") }
                                        Button(
                                            onClick = { viewModel.deleteColor(color.id) }
                                        ) { Text("Delete") }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
        )
    }
}
