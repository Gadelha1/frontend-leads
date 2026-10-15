@file:OptIn(kotlin.js.ExperimentalWasmJsInterop::class)

package br.com.semesperanca.frontendleads

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import br.com.semesperanca.frontendleads.di.AppModules

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    AppModules.init()
    ComposeViewport("composeContainer") {
        App()
    }
}