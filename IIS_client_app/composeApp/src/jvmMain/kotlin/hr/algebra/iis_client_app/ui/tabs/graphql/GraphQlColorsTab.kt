package hr.algebra.iis_client_app.ui.tabs.graphql

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.algebra.iis_client_app.api.GraphQLApi
import hr.algebra.iis_client_app.createHttpClient
import hr.algebra.iis_client_app.globalAppState
import hr.algebra.iis_client_app.ui.tabs.rest.CreateColorDialog

@Composable
fun GraphQlColorsTab() {
    val client = remember { createHttpClient(globalAppState) }
    val graphQLApi = remember { GraphQLApi(client) }
    val viewModel = remember { GraphQlColorsViewModel(graphQLApi) }
    val uiState by viewModel.uiState.collectAsState()
    val state by globalAppState.state.collectAsState()
    
    val scrollState = rememberScrollState()

    if (uiState.showCreateDialog) {
        CreateColorDialog(
            onDismiss = viewModel::hideDialog,
            onCreate = viewModel::createColor
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 24.dp)
    ) {
        Text("GraphQL Colors API", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = viewModel::getAllColors, enabled = !uiState.isLoading) {
                Text("Get All Colors (Query)")
            }

            if (state.role.canEdit) {
                Button(onClick = viewModel::showDialog, enabled = !uiState.isLoading) {
                    Text("Create Color (Mutation)")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text("Parsed GraphQL Data:", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))
        
        if (uiState.parsedColors.isEmpty()) {
            Text("No parsed results to show.", modifier = Modifier.padding(8.dp))
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                uiState.parsedColors.forEach { color ->
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
                            
                            if (state.role.canEdit) {
                                Button(
                                    onClick = { viewModel.deleteColor(color.id) },
                                    enabled = !uiState.isLoading
                                ) { Text("Delete") }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("GraphQL Raw Response:", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(uiState.responseText, modifier = Modifier.padding(8.dp))
    }
}
