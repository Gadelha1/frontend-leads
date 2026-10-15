package br.com.semesperanca.frontendleads

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import br.com.semesperanca.frontendleads.di.AppModules

fun main() {
    AppModules.init()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "100 Esperança - Leads",
            state = rememberWindowState(width = 480.dp, height = 800.dp),
        ) {
            App()
        }
    }
}