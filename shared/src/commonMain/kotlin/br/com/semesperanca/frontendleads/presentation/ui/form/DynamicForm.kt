package br.com.semesperanca.frontendleads.presentation.ui.form

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.semesperanca.frontendleads.presentation.ui.AppColors
import br.com.semesperanca.frontendleads.resources.Res
import br.com.semesperanca.frontendleads.resources.arrow_drop_down
import org.jetbrains.compose.resources.painterResource

@Composable
fun DynamicForm(
    fields: List<FormField>,
    values: Map<String, String>,
    onValueChange: (key: String, value: String) -> Unit,
    onSubmit: () -> Unit,
    onClear: () -> Unit,
    submitLabel: String = "Gravar",
    isLoading: Boolean = false,
    errorMessage: String? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        fields.forEach { field ->
            FormFieldItem(
                field = field,
                value = values[field.key] ?: "",
                onValueChange = { onValueChange(field.key, it) },
            )
        }

        errorMessage?.let {
            Text(
                text = it,
                color = AppColors.Red500,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
            )
        }

        // Botão Gravar
        Button(
            onClick = onSubmit,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.Emerald600),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                text = if (isLoading) "Gravando..." else submitLabel,
                color = AppColors.White,
                fontWeight = FontWeight.SemiBold,
            )
        }

        // Botão Limpar Campos
        Button(
            onClick = onClear,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.White),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                text = "Limpar Campos",
                color = AppColors.Gray700,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun FormFieldItem(
    field: FormField,
    value: String,
    onValueChange: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        Text(
            text = if (field.required) "${field.label} *" else field.label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = AppColors.Gray700,
            modifier = Modifier.padding(bottom = 4.dp),
        )

        when (field.type) {
            FormFieldType.DROPDOWN -> DropdownField(field, value, onValueChange)
            FormFieldType.MULTILINE -> OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                colors = textFieldColors(),
                shape = RoundedCornerShape(8.dp),
            )
            FormFieldType.INTEGER -> OutlinedTextField(
                value = value,
                onValueChange = { if (it.all { c -> c.isDigit() }) onValueChange(it) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = textFieldColors(),
                shape = RoundedCornerShape(8.dp),
            )
            FormFieldType.DECIMAL, FormFieldType.CURRENCY -> OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = textFieldColors(),
                shape = RoundedCornerShape(8.dp),
                placeholder = { if (field.type == FormFieldType.CURRENCY) Text("0,00") },
            )
            else -> OutlinedTextField(
                value = value,
                onValueChange = {
                    val v = if (field.maxLength != null) it.take(field.maxLength) else it
                    onValueChange(v)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors(),
                shape = RoundedCornerShape(8.dp),
                placeholder = field.placeholder?.let { { Text(it, color = AppColors.Gray400) } },
            )
        }
    }
}

@Composable
private fun DropdownField(
    field: FormField,
    value: String,
    onValueChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val selected = field.options.firstOrNull { it.value == value }?.label ?: value

    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, AppColors.Gray200, RoundedCornerShape(8.dp))
                .background(AppColors.White, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(horizontal = 14.dp, vertical = 14.dp),
        ) {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            ) {
                Text(
                    text = selected.ifEmpty { "Selecione..." },
                    color = if (selected.isEmpty()) AppColors.Gray400 else AppColors.Gray900,
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                )
                Icon(
                    painter = painterResource(Res.drawable.arrow_drop_down),
                    contentDescription = null,
                    tint = AppColors.Gray500,
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            field.options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        onValueChange(option.value)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun textFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AppColors.Emerald500,
    unfocusedBorderColor = AppColors.Gray200,
    focusedLabelColor = AppColors.Emerald600,
)
