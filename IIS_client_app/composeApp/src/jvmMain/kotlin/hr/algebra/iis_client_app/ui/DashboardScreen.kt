package hr.algebra.iis_client_app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.algebra.iis_client_app.ThemeMode
import hr.algebra.iis_client_app.globalAppState
import hr.algebra.iis_client_app.ui.tabs.rest.RestColorsTab
import hr.algebra.iis_client_app.ui.tabs.graphql.GraphQlColorsTab
import hr.algebra.iis_client_app.ui.tabs.soap.SoapSearchTab
import hr.algebra.iis_client_app.ui.tabs.grpc.GrpcWeatherTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val state by globalAppState.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard - Role: ${state.role.name}") },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("API Mode: ${state.apiMode.name}", modifier = Modifier.padding(end = 8.dp))
                        
                        // Theme Dropdown Menu
                        var themeDropdownExpanded by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.padding(end = 16.dp)) {
                            OutlinedButton(onClick = { themeDropdownExpanded = true }) {
                                Text("Theme: ${state.themeMode.name}")
                            }
                            DropdownMenu(
                                expanded = themeDropdownExpanded,
                                onDismissRequest = { themeDropdownExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("System Default") },
                                    onClick = { 
                                        globalAppState.setThemeMode(ThemeMode.SYSTEM)
                                        themeDropdownExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Light") },
                                    onClick = { 
                                        globalAppState.setThemeMode(ThemeMode.LIGHT)
                                        themeDropdownExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Dark") },
                                    onClick = { 
                                        globalAppState.setThemeMode(ThemeMode.DARK)
                                        themeDropdownExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Black & White") },
                                    onClick = { 
                                        globalAppState.setThemeMode(ThemeMode.MONOCHROME)
                                        themeDropdownExpanded = false
                                    }
                                )
                            }
                        }

                        Button(onClick = { globalAppState.logout() }, modifier = Modifier.padding(end = 16.dp)) {
                            Text("Logout")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }, text = { Text("REST Colors") })
                Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }, text = { Text("GraphQL Colors") })
                Tab(selected = selectedTabIndex == 2, onClick = { selectedTabIndex = 2 }, text = { Text("SOAP Search") })
                Tab(selected = selectedTabIndex == 3, onClick = { selectedTabIndex = 3 }, text = { Text("gRPC Weather") })
            }
            
            Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                when (selectedTabIndex) {
                    0 -> RestColorsTab()
                    1 -> GraphQlColorsTab()
                    2 -> SoapSearchTab()
                    3 -> GrpcWeatherTab()
                }
            }
        }
    }
}
