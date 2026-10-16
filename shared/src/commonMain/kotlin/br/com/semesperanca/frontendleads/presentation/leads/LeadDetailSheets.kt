package br.com.semesperanca.frontendleads.presentation.leads

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.semesperanca.frontendleads.presentation.ui.AppColors
import br.com.semesperanca.frontendleads.presentation.ui.Formatters
import br.com.semesperanca.frontendleads.presentation.ui.ModalSheet
import br.com.semesperanca.frontendleads.presentation.ui.Pill
import br.com.semesperanca.frontendleads.presentation.ui.RoundedTextField

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
internal fun ActivityFormSheet(
    initial: LocalActivity?,
    onDismiss: () -> Unit,
    onSave: (type: String, title: String, scheduledAt: String, description: String) -> Unit,
) {
    var type by remember { mutableStateOf(initial?.type ?: "VISITA") }
    var title by remember { mutableStateOf(initial?.title ?: "") }
    var dateIso by remember { mutableStateOf(initial?.scheduledAt?.substringBefore('T') ?: "") }
    var timeMinutes by remember { mutableStateOf(Formatters.timeToMinutes(initial?.scheduledAt)) }
    var description by remember { mutableStateOf(initial?.description ?: "") }
    var error by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var pickingDate by remember { mutableStateOf(false) }

    ModalSheet(onDismiss = onDismiss) {
        Text(
            text = if (initial == null) "Nova Atividade" else "Editar Atividade",
            color = AppColors.Gray900,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Tipo", color = AppColors.Gray400, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            AVAILABLE_ACTIVITY_TYPES.forEach { t ->
                val selected = type == t
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (selected) AppColors.Emerald600 else AppColors.Gray200)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { type = t },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = activityTypeIcon(t),
                        contentDescription = activityTypeLabel(t),
                        tint = if (selected) AppColors.White else AppColors.Gray600,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AppColors.Gray100)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = activityTypeIcon(type),
                contentDescription = null,
                tint = AppColors.Gray500,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = activityTypeLabel(type),
                color = AppColors.Gray500,
                fontSize = 14.sp,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Título", color = AppColors.Gray400, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        RoundedTextField(
            value = title,
            onValueChange = { title = it },
            placeholder = "Ex: Visita ao Imóvel",
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(text = "Agendado para", color = AppColors.Gray400, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        DateTimeField(
            display = Formatters.dateTimeDisplay(dateIso, timeMinutes),
            onClick = {
                pickingDate = true
                showDatePicker = true
            },
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Se vazio, será agendada para agora",
            color = AppColors.Gray500,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(text = "Descrição (opcional)", color = AppColors.Gray400, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        RoundedTextField(
            value = description,
            onValueChange = { description = it },
            placeholder = "Descreva os detalhes da atividade",
            minHeight = 80,
            multiline = true,
        )

        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error!!,
                color = AppColors.Red500,
                fontSize = 13.sp,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.White,
                    contentColor = AppColors.Gray600,
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.Gray300),
                modifier = Modifier.weight(1f).height(46.dp),
            ) {
                Text(text = "Cancelar", color = AppColors.Gray600, fontWeight = FontWeight.SemiBold)
            }
            Button(
                onClick = {
                    if (title.isBlank()) {
                        error = "Informe um título para a atividade"
                        return@Button
                    }
                    val dt = when {
                        dateIso.isNotBlank() && timeMinutes != null ->
                            "$dateIso${if (dateIso.contains('T')) "" else "T"}${Formatters.formatPickerTime(timeMinutes)}"
                        dateIso.isNotBlank() -> "${dateIso}T00:00"
                        else -> ""
                    }
                    onSave(type, title.trim(), dt, description.trim())
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.Emerald600,
                    contentColor = AppColors.White,
                ),
                modifier = Modifier.weight(1f).height(46.dp),
            ) {
                Text(text = "Salvar", color = AppColors.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = Formatters.dateToMillis(dateIso),
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val picked = datePickerState.selectedDateMillis
                    if (picked != null) {
                        dateIso = Formatters.millisToDateIso(picked)
                    }
                    showDatePicker = false
                    if (pickingDate) {
                        pickingDate = false
                        showTimePicker = true
                    }
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            },
            content = {
                DatePicker(state = datePickerState, showModeToggle = false)
            },
        )
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = (timeMinutes ?: 0) / 60,
            initialMinute = (timeMinutes ?: 0) % 60,
            is24Hour = true,
        )
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    timeMinutes = timePickerState.hour * 60 + timePickerState.minute
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") }
            },
            title = { Text("Selecionar hora") },
            content = {
                TimePicker(state = timePickerState)
            },
        )
    }
}

@Composable
private fun DateTimeField(display: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(AppColors.Gray100)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onClick() }
            .padding(start = 16.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = display.ifEmpty { "Selecionar data" },
            color = if (display.isEmpty()) AppColors.Gray400 else AppColors.Gray900,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.Filled.DateRange,
            contentDescription = "Selecionar data",
            tint = AppColors.Gray500,
            modifier = Modifier.size(22.dp),
        )
    }
}

@Composable
internal fun ActivityDetailSheet(
    activity: LocalActivity,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val (statusLabel, statusColor) = statusConfig(activity.status)
    ModalSheet(onDismiss = onDismiss) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AppColors.Emerald100),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = activityTypeIcon(activity.type),
                    contentDescription = null,
                    tint = AppColors.Emerald700,
                    modifier = Modifier.size(20.dp),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.title,
                    color = AppColors.Gray900,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = activityTypeLabel(activity.type),
                    color = AppColors.Gray500,
                    fontSize = 12.sp,
                )
            }
            Pill(
                text = statusLabel,
                backgroundColor = statusColor,
                fontSize = 11,
                horizontalPadding = 10,
                verticalPadding = 3,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SheetInfoRow(label = "Agendado para", value = Formatters.formatActivityDate(activity.scheduledAt))
            if (activity.createdAt.isNotBlank()) {
                SheetInfoRow(label = "Criado em", value = Formatters.formatActivityDate(activity.createdAt))
            }
        }

        if (activity.description.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Descrição", color = AppColors.Gray700, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = activity.description,
                color = AppColors.Gray600,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onEdit,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.White,
                    contentColor = AppColors.Blue500,
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.Blue500),
                modifier = Modifier.weight(1f).height(44.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.ModeEdit,
                    contentDescription = null,
                    tint = AppColors.Blue500,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Editar", color = AppColors.Blue500, fontWeight = FontWeight.SemiBold)
            }
            Button(
                onClick = onDelete,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.White,
                    contentColor = AppColors.Red500,
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, AppColors.Red500),
                modifier = Modifier.weight(1f).height(44.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.DeleteOutline,
                    contentDescription = null,
                    tint = AppColors.Red500,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Remover", color = AppColors.Red500, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun SheetInfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, color = AppColors.Gray400, fontSize = 12.sp)
        Text(text = value, color = AppColors.Gray700, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
internal fun TagPickerSheet(
    available: List<LocalTag>,
    onDismiss: () -> Unit,
    onAdd: (LocalTag) -> Unit,
) {
    ModalSheet(onDismiss = onDismiss) {
        Text(
            text = "Etiquetas",
            color = AppColors.Gray900,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Toque para adicionar",
            color = AppColors.Gray400,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (available.isEmpty()) {
            Text(
                text = "Nenhuma etiqueta disponível.",
                color = AppColors.Gray400,
                fontSize = 13.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
            )
        } else {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                available.forEach { tag ->
                    val shape = RoundedCornerShape(50)
                    Row(
                        modifier = Modifier
                            .width(96.dp)
                            .clip(shape)
                            .background(tag.color)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) { onAdd(tag) }
                            .padding(horizontal = 8.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = tag.label,
                            color = AppColors.White,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onDismiss,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.Emerald600,
                contentColor = AppColors.White,
            ),
            modifier = Modifier.fillMaxWidth().height(46.dp),
        ) {
            Text(text = "Concluir", color = AppColors.White, fontWeight = FontWeight.SemiBold)
        }
    }
}


