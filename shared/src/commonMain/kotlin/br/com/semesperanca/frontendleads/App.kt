package br.com.semesperanca.frontendleads

import androidx.compose.runtime.Composable
import br.com.semesperanca.frontendleads.presentation.AppNavigation
import br.com.semesperanca.frontendleads.theme.MobileLeadsTheme

@Composable
fun App() {
    MobileLeadsTheme {
        AppNavigation()
    }
}