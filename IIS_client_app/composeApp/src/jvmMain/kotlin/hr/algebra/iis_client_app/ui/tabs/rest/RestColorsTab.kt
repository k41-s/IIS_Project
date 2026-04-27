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

    LaunchedEffect(state.apiMode) {
        viewModel.fetchColors()
    }

    if (uiState.showCreateDialog) {
        CreateColorDialog(
            onDismiss = viewModel::hideDialog,
            onCreate = viewModel::createColor
        )
    }

    if (uiState.colorToUpdate != null) {
        UpdateColorDialog(
            color = uiState.colorToUpdate!!,
            onDismiss = viewModel::hideUpdateDialog,
            onUpdate = { updatedColor -> viewModel.updateColor(updatedColor.id, updatedColor) }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("REST Colors API", style = MaterialTheme.typography.titleLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = viewModel::fetchColors, enabled = !uiState.isLoading) {
                    Text("Refresh")
                }

                if (state.role.canEdit && state.apiMode == ApiMode.CUSTOM) {
                    Button(
                        onClick = viewModel::showDialog, 
                        enabled = !uiState.isLoading
                    ) { Text("Create Color") }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Switch API mode:", style = MaterialTheme.typography.titleLarge)
                Switch(
                    checked = state.apiMode.name == "CUSTOM",
                    onCheckedChange = { globalAppState.toggleApiMode() },
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }
        
        uiState.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        if (uiState.isLoading) CircularProgressIndicator()
        else {
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
}
