package br.com.semesperanca.frontendleads.presentation.leads

import br.com.semesperanca.frontendleads.presentation.ui.form.FormField
import br.com.semesperanca.frontendleads.presentation.ui.form.FormFieldType
import br.com.semesperanca.frontendleads.presentation.ui.form.FormOption

private enum class TransactionType(val value: String, val label: String) {
    VENDA("VENDA", "Venda"),
    ALUGUEL("ALUGUEL", "Aluguel"),
}

private enum class LeadTemperature(val value: String, val label: String) {
    QUENTE("QUENTE", "Quente"),
    MORNA("MORNA", "Morna"),
    FRIA("FRIA", "Fria"),
}

val leadFormFields: List<FormField> = listOf(
    FormField(key = "name", label = "Nome", required = true, maxLength = 120),
    FormField(key = "email", label = "E-mail", required = true),
    FormField(key = "phone", label = "Telefone", type = FormFieldType.INTEGER),
    FormField(
        key = "transactionType",
        label = "Tipo de Transação",
        type = FormFieldType.DROPDOWN,
        options = TransactionType.entries.map { FormOption(value = it.value, label = it.label) },
    ),
    FormField(
        key = "temperature",
        label = "Temperatura",
        type = FormFieldType.DROPDOWN,
        options = LeadTemperature.entries.map { FormOption(value = it.value, label = it.label) },
    ),
    FormField(key = "leadOrigin", label = "Origem"),
    FormField(key = "message", label = "Mensagem", type = FormFieldType.MULTILINE),
)

fun leadFormDefaults(): Map<String, String> = leadFormFields.associate { it.key to "" }
