package br.com.semesperanca.frontendleads.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import br.com.semesperanca.frontendleads.presentation.auth.LoginScreen
import br.com.semesperanca.frontendleads.presentation.auth.LoginViewModel
import br.com.semesperanca.frontendleads.presentation.home.MainTabsScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppRoot() {
    val loginViewModel: LoginViewModel = koinViewModel()
    val uiState by loginViewModel.uiState.collectAsState()

    if (uiState.isAuthenticated) {
        MainTabsScreen()
    } else {
        LoginScreen(viewModel = loginViewModel)
    }
}
