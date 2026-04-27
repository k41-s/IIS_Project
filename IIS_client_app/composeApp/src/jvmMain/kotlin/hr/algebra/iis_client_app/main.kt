package hr.algebra.iis_client_app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "IIS Unified Client",
    ) {
        App()
    }
}
