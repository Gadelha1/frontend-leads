package br.com.semesperanca.frontendleads.presentation.users

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UsersUiState(
    val users: List<UserUi> = emptyList(),
)

class UsersViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UsersUiState())
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    fun addUser(user: UserUi) {
        _uiState.update { it.copy(users = it.users + user) }
    }
}
