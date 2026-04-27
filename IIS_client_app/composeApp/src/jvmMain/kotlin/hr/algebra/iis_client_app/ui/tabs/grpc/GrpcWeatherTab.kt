package hr.algebra.iis_client_app.ui.tabs.grpc

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.algebra.iis_client_app.api.WeatherGrpcClient

@Composable
fun GrpcWeatherTab() {
    val grpcClient = remember { WeatherGrpcClient() }
    val viewModel = remember { GrpcWeatherViewModel(grpcClient) }
    val uiState by viewModel.uiState.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Weather gRPC Service", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = uiState.cityName,
            onValueChange = viewModel::updateCityName,
            label = { Text("City Name Parameter") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = viewModel::getTemperature, enabled = !uiState.isLoading) { 
            Text("Get Temperature") 
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text("gRPC Protobuf Response:")
        Text(uiState.responseText, modifier = Modifier.padding(8.dp))
    }
}
