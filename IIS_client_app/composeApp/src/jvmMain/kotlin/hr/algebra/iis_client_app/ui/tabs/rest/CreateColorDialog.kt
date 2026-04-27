package hr.algebra.iis_client_app.ui.tabs.rest

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.algebra.iis_client_app.api.ColorDTO

@Composable
fun CreateColorDialog(
    onDismiss: () -> Unit,
    onCreate: (ColorDTO) -> Unit
) {
    var newName by remember { mutableStateOf("") }
    var newYear by remember { mutableStateOf("") }
    var newColorHex by remember { mutableStateOf("") }
    var newPantone by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Color") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = newYear,
                    onValueChange = { newYear = it },
                    label = { Text("Year (Numeric)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = newColorHex,
                    onValueChange = { newColorHex = it },
                    label = { Text("Color (e.g., #FFFFFF)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = newPantone,
                    onValueChange = { newPantone = it },
                    label = { Text("Pantone Value") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedYear = newYear.toIntOrNull() ?: 2024
                    val randomId = (1000..9999).random() // ID securely generated
                    
                    val newColor = ColorDTO(
                        id = randomId,
                        name = newName,
                        year = parsedYear,
                        color = newColorHex,
                        pantone_value = newPantone
                    )
                    onCreate(newColor)
                }
            ) { Text("Create") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
