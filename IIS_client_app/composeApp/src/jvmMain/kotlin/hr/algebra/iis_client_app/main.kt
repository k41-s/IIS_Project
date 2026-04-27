package hr.algebra.iis_client_app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "iis_client_app",
    ) {
        App()
    }
}