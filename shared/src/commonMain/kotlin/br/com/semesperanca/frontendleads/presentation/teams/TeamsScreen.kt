package br.com.semesperanca.frontendleads.presentation.teams

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.semesperanca.frontendleads.data.dto.TeamDto
import br.com.semesperanca.frontendleads.presentation.ui.AppColors
import br.com.semesperanca.frontendleads.presentation.ui.form.DynamicForm
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TeamsScreen() {
    val viewModel: TeamsViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.successMessage, state.error) {
        state.successMessage?.let { snackbar.showSnackbar(it); viewModel.clearMessages() }
        state.error?.let { snackbar.showSnackbar("Erro: $it"); viewModel.clearMessages() }
    }

    if (state.showForm) {
        val editing = state.editingTeam
        val formValues = remember(editing) {
            mutableStateMapOf<String, String>().apply { putAll(teamFormDefaults(editing)) }
        }
        TeamFormScreen(
            isEditing = editing != null,
            values = formValues,
            onValueChange = { k, v -> formValues[k] = v },
            isLoading = state.isLoading,
            error = state.error,
            onSubmit = {
                if (editing != null) {
                    viewModel.update(editing.id!!, toUpdateTeamRequest(formValues))
                } else {
                    viewModel.create(toCreateTeamRequest(formValues))
                }
            },
            onDismiss = { viewModel.dismissForm() },
        )
        return
    }

    Scaffold(
        containerColor = AppColors.Gray50,
        snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showCreateForm() },
                containerColor = AppColors.Emerald600,
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Novo Time", tint = AppColors.White)
            }
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text(
                "Times",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.Gray900,
                modifier = Modifier.padding(bottom = 12.dp),
            )

            SearchBar(onSearch = { viewModel.search(it) }, onClear = { viewModel.loadTeams() })

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppColors.Emerald600)
                }
            } else if (state.teams.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum time encontrado.", color = AppColors.Gray500)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.teams) { team ->
                        TeamCard(
                            team = team,
                            onEdit = { viewModel.showEditForm(team) },
                            onDelete = { team.id?.let { viewModel.delete(it) } },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBar(onSearch: (String) -> Unit, onClear: () -> Unit) {
    var query by remember { mutableStateOf("") }
    OutlinedTextField(
        value = query,
        onValueChange = {
            query = it
            if (it.isBlank()) onClear() else onSearch(it)
        },
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        placeholder = { Text("Buscar por nome...", color = AppColors.Gray400) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = AppColors.Gray500) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { query = ""; onClear() }) {
                    Icon(Icons.Filled.Close, contentDescription = "Limpar", tint = AppColors.Gray500)
                }
            }
        },
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppColors.Emerald500,
            unfocusedBorderColor = AppColors.Gray200,
        ),
    )
}

@Composable
private fun TeamCard(team: TeamDto, onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .background(AppColors.White, RoundedCornerShape(12.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .background(AppColors.Emerald50, RoundedCornerShape(8.dp))
                    .padding(8.dp),
            ) {
                Icon(Icons.Filled.Groups, contentDescription = null, tint = AppColors.Emerald600)
            }
            Column {
                Text(
                    text = team.name ?: "Sem nome",
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.Gray900,
                    fontSize = 15.sp,
                )
                team.description?.takeIf { it.isNotBlank() }?.let {
                    Text(it, color = AppColors.Gray500, fontSize = 12.sp)
                }
                team.maxAgents?.let {
                    Text("Máx. $it corretores", color = AppColors.Gray400, fontSize = 12.sp)
                }
            }
        }
        Row {
            IconButton(onClick = onEdit) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = AppColors.Emerald600)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Excluir", tint = AppColors.Red400)
            }
        }
    }
}

@Composable
private fun TeamFormScreen(
    isEditing: Boolean,
    values: Map<String, String>,
    onValueChange: (String, String) -> Unit,
    isLoading: Boolean,
    error: String?,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Gray50),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppColors.White)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onDismiss) {
                Icon(Icons.Filled.Close, contentDescription = "Fechar", tint = AppColors.Gray700)
            }
            Text(
                text = if (isEditing) "Editar Time" else "Novo Time",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.Gray900,
            )
        }
        DynamicForm(
            fields = teamFormFields,
            values = values,
            onValueChange = onValueChange,
            onSubmit = onSubmit,
            submitLabel = if (isEditing) "Atualizar" else "Criar Time",
            isLoading = isLoading,
            errorMessage = error,
        )
    }
}
