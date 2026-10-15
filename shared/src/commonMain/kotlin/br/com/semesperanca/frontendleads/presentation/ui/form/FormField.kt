package br.com.semesperanca.frontendleads.presentation.ui.form

enum class FormFieldType {
    TEXT,
    MULTILINE,
    INTEGER,
    DECIMAL,
    CURRENCY,
    DROPDOWN,
}

data class FormOption(
    val value: String,
    val label: String,
)

data class FormField(
    val key: String,
    val label: String,
    val type: FormFieldType = FormFieldType.TEXT,
    val required: Boolean = false,
    val placeholder: String = "",
    val options: List<FormOption> = emptyList(),
    val maxLength: Int? = null,
)
