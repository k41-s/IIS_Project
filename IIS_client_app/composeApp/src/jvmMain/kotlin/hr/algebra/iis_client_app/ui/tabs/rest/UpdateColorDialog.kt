package hr.algebra.iis_client_app.ui.tabs.rest

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.algebra.iis_client_app.api.ColorDTO

@Composable
fun UpdateColorDialog(
    color: ColorDTO,
    onDismiss: () -> Unit,
    onUpdate: (ColorDTO) -> Unit
) {
    var newName by remember { mutableStateOf(color.name) }
    var newYear by remember { mutableStateOf(color.year.toString()) }
    var newColorHex by remember { mutableStateOf(color.color) }
    var newPantone by remember { mutableStateOf(color.pantone_value) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Color (ID: ${color.id})") },
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
                    val parsedYear = newYear.toIntOrNull() ?: color.year
                    
                    val updatedColor = color.copy(
                        name = newName,
                        year = parsedYear,
                        color = newColorHex,
                        pantone_value = newPantone
                    )
                    onUpdate(updatedColor)
                }
            ) { Text("Update") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
