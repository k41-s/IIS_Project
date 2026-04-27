package hr.algebra.iis_client_app.ui.tabs.soap

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.algebra.iis_client_app.api.SoapApi
import hr.algebra.iis_client_app.createHttpClient
import hr.algebra.iis_client_app.globalAppState

@Composable
fun SoapSearchTab() {
    val client = remember { createHttpClient(globalAppState) }
    val soapApi = remember { SoapApi(client) }
    val viewModel = remember { SoapSearchViewModel(soapApi) }
    val uiState by viewModel.uiState.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        Text("SOAP XPath Search", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.xpathQuery,
            onValueChange = viewModel::updateQuery,
            label = { Text("XPath Query String") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = viewModel::executeSearch, enabled = !uiState.isLoading) { 
            Text("Execute SOAP POST") 
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text("Extracted <result> Inner XML:")
        Text(uiState.responseText, modifier = Modifier.padding(8.dp))
    }
}
