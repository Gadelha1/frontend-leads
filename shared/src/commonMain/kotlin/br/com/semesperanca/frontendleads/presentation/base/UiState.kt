package br.com.semesperanca.frontendleads.presentation.base

/**
 * Convenções do padrão MVI (Model-View-Intent) usado no projeto.
 *
 * **Model** — cada tela tem um único [kotlinx.coroutines.flow.StateFlow] de um `data class`
 * `XUiState` imutável, exposto via `asStateFlow()` e atualizado apenas com `copy(...)`.
 *
 * **View** — composables sem estado (stateless) que leem `uiState` e disparam intents.
 *
 * **Intent** — métodos públicos no ViewModel (ex.: `onSearchChange`, `toggleFavorite`),
 * chamados pela view. O ViewModel consome a intent, executa side-effects e emite novo estado.
 *
 * Documentação de referência — não define contrato de runtime; as convenções são aplicadas
 * diretamente em cada ViewModel e UiState.
 */