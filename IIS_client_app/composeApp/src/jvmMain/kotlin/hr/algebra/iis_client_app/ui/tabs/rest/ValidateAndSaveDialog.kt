package hr.algebra.iis_client_app.ui.tabs.rest

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ValidateAndSaveDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, Boolean) -> Unit,
    validationMessage: String?
) {
    val randomId = remember { (1000..9999).random() }

    val defaultJson = """
        {
          "id": $randomId,
          "name": "sample color",
          "year": 2024,
          "color": "#FFFFFF",
          "pantone_value": "11-1111"
        }
    """.trimIndent()

    val defaultXml = """
        <Color>
            <id>$randomId</id>
            <name>sample color</name>
            <year>2024</year>
            <color>#FFFFFF</color>
            <pantone_value>11-1111</pantone_value>
        </Color>
    """.trimIndent()

    var isXml by remember { mutableStateOf(false) }
    var payload by remember { mutableStateOf(defaultJson) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Validate and Save (JSON/XML)") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = !isXml,
                        onClick = {
                            isXml = false
                            payload = defaultJson
                        }
                    )
                    Text("JSON")
                    Spacer(Modifier.width(16.dp))
                    RadioButton(
                        selected = isXml,
                        onClick = {
                            isXml = true
                            payload = defaultXml
                        }
                    )
                    Text("XML")
                }

                OutlinedTextField(
                    value = payload,
                    onValueChange = { payload = it },
                    label = { Text("Payload") },
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    maxLines = 15
                )

                if (validationMessage != null) {
                    val isSuccess = validationMessage.contains("Success", ignoreCase = true)
                    Text(
                        text = validationMessage,
                        color = if (isSuccess) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSubmit(payload, isXml) }) {
                Text("Validate & Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}