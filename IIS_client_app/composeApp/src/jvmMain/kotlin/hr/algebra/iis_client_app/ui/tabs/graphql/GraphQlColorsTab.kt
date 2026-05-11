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

    var fieldsDropdownExpanded by remember { mutableStateOf(false) }
    val availableFields = listOf("id", "name", "year", "color", "pantone_value")

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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("GraphQL Colors API", style = MaterialTheme.typography.titleLarge)

            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "Uses Custom API Only",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Select Fields to Query:", style = MaterialTheme.typography.labelLarge)
        Box(modifier = Modifier.padding(vertical = 8.dp)) {
            OutlinedButton(onClick = { fieldsDropdownExpanded = true }) {
                Text("Selected Fields (${uiState.selectedFields.size}/5) ▼")
            }

            DropdownMenu(
                expanded = fieldsDropdownExpanded,
                onDismissRequest = { fieldsDropdownExpanded = false }
            ) {
                availableFields.forEach { field ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = uiState.selectedFields.contains(field),
                                    onCheckedChange = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(field)
                            }
                        },
                        onClick = { viewModel.toggleField(field) }
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = viewModel::getAllColors, enabled = !uiState.isLoading) {
                Text("Get All Colors (Dynamic Query)")
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
                                if ("id" in uiState.selectedFields || "name" in uiState.selectedFields) {
                                    val titleParts = mutableListOf<String>()
                                    if ("id" in uiState.selectedFields) titleParts.add("ID: ${color.id}")
                                    if ("name" in uiState.selectedFields) titleParts.add(color.name)

                                    Text(
                                        text = titleParts.joinToString(" - "),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }

                                val subtitleParts = mutableListOf<String>()
                                if ("year" in uiState.selectedFields)
                                    subtitleParts.add("Year: ${color.year}")
                                if ("color" in uiState.selectedFields)
                                    subtitleParts.add("Hex: ${color.color}")
                                if ("pantone_value" in uiState.selectedFields)
                                    subtitleParts.add("Pantone: ${color.pantone_value}")

                                if (subtitleParts.isNotEmpty()) {
                                    Text(text = subtitleParts.joinToString(" | "))
                                }
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
