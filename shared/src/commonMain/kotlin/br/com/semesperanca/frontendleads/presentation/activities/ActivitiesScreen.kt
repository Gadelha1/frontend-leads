package br.com.semesperanca.frontendleads.presentation.activities

import androidx.compose.foundation.background
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
import br.com.semesperanca.frontendleads.data.dto.ActivityDto
import br.com.semesperanca.frontendleads.presentation.ui.AppColors
import br.com.semesperanca.frontendleads.presentation.ui.form.DynamicForm
import br.com.semesperanca.frontendleads.resources.Res
import br.com.semesperanca.frontendleads.resources.add
import br.com.semesperanca.frontendleads.resources.calendar_month
import br.com.semesperanca.frontendleads.resources.check_circle
import br.com.semesperanca.frontendleads.resources.close
import br.com.semesperanca.frontendleads.resources.delete
import br.com.semesperanca.frontendleads.resources.edit
import br.com.semesperanca.frontendleads.resources.search
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ActivitiesScreen() {
    val viewModel: ActivitiesViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.successMessage, state.error) {
        state.successMessage?.let { snackbar.showSnackbar(it); viewModel.clearMessages() }
        state.error?.let { snackbar.showSnackbar("Erro: $it"); viewModel.clearMessages() }
    }

    if (state.showForm) {
        val editing = state.editingActivity
        val formValues = remember(editing) {
            mutableStateMapOf<String, String>().apply { putAll(activityFormDefaults(editing)) }
        }
        ActivityFormScreen(
            isEditing = editing != null,
            values = formValues,
            onValueChange = { k, v -> formValues[k] = v },
            isLoading = state.isLoading,
            error = state.error,
            onSubmit = {
                if (editing != null) {
                    viewModel.update(editing.id!!, toUpdateActivityRequest(formValues))
                } else {
                    viewModel.create(toCreateActivityRequest(formValues))
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
                Icon(painterResource(Res.drawable.add), contentDescription = "Nova Atividade", tint = AppColors.White)
            }
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text(
                "Atividades",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AppColors.Gray900,
                modifier = Modifier.padding(bottom = 12.dp),
            )

            ActivitySearchBar(
                onSearch = { viewModel.loadActivities(search = it) },
                onClear = { viewModel.loadActivities() },
            )

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AppColors.Emerald600)
                }
            } else if (state.activities.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhuma atividade encontrada.", color = AppColors.Gray500)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.activities) { activity ->
                        ActivityCard(
                            activity = activity,
                            onEdit = { viewModel.showEditForm(activity) },
                            onDelete = { activity.id?.let { viewModel.delete(it) } },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivitySearchBar(onSearch: (String) -> Unit, onClear: () -> Unit) {
    var query by remember { mutableStateOf("") }
    OutlinedTextField(
        value = query,
        onValueChange = {
            query = it
            if (it.isBlank()) onClear() else onSearch(it)
        },
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        placeholder = { Text("Buscar por título...", color = AppColors.Gray400) },
        leadingIcon = { Icon(painterResource(Res.drawable.search), contentDescription = null, tint = AppColors.Gray500) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { query = ""; onClear() }) {
                    Icon(painterResource(Res.drawable.close), contentDescription = "Limpar", tint = AppColors.Gray500)
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
private fun ActivityCard(activity: ActivityDto, onEdit: () -> Unit, onDelete: () -> Unit) {
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
                    .background(
                        if (activity.completed) AppColors.Emerald50 else AppColors.Amber50,
                        RoundedCornerShape(8.dp),
                    )
                    .padding(8.dp),
            ) {
                Icon(
                    painter = painterResource(if (activity.completed) Res.drawable.check_circle else Res.drawable.calendar_month),
                    contentDescription = null,
                    tint = if (activity.completed) AppColors.Emerald600 else AppColors.Amber400,
                )
            }
            Column {
                Text(
                    text = activity.title ?: "Sem título",
                    fontWeight = FontWeight.SemiBold,
                    color = AppColors.Gray900,
                    fontSize = 15.sp,
                )
                activity.description?.takeIf { it.isNotBlank() }?.let {
                    Text(
                        text = it.take(60) + if (it.length > 60) "..." else "",
                        color = AppColors.Gray500,
                        fontSize = 12.sp,
                    )
                }
                activity.activityDate?.take(16)?.let {
                    Text(it.replace("T", " às "), color = AppColors.Gray400, fontSize = 11.sp)
                }
            }
        }
        Row {
            IconButton(onClick = onEdit) {
                Icon(painterResource(Res.drawable.edit), contentDescription = "Editar", tint = AppColors.Emerald600)
            }
            IconButton(onClick = onDelete) {
                Icon(painterResource(Res.drawable.delete), contentDescription = "Excluir", tint = AppColors.Red400)
            }
        }
    }
}

@Composable
private fun ActivityFormScreen(
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
                Icon(painterResource(Res.drawable.close), contentDescription = "Fechar", tint = AppColors.Gray700)
            }
            Text(
                text = if (isEditing) "Editar Atividade" else "Nova Atividade",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.Gray900,
            )
        }
        DynamicForm(
            fields = activityFormFields,
            values = values,
            onValueChange = onValueChange,
            onSubmit = onSubmit,
            onClear = { activityFormFields.forEach { onValueChange(it.key, "") } },
            submitLabel = "Gravar",
            isLoading = isLoading,
            errorMessage = error,
        )
    }
}
