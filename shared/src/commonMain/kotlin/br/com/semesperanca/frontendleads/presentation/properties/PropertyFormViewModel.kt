package br.com.semesperanca.frontendleads.presentation.properties

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PropertyFormUiState(
    val values: Map<String, String> = propertyFormDefaults(),
    val showUnavailableNotice: Boolean = false,
)

class PropertyFormViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PropertyFormUiState())
    val uiState: StateFlow<PropertyFormUiState> = _uiState.asStateFlow()

    fun onFieldChange(key: String, value: String) {
        _uiState.update { it.copy(values = it.values + (key to value)) }
    }

    fun clearFields() {
        _uiState.update { it.copy(values = propertyFormDefaults()) }
    }

    fun onSave() {
        _uiState.update { it.copy(showUnavailableNotice = true) }
    }

    fun dismissNotice() {
        _uiState.update { it.copy(showUnavailableNotice = false) }
    }
}
