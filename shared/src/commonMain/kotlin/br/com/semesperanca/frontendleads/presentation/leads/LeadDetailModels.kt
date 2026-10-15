package br.com.semesperanca.frontendleads.presentation.leads

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.LocalPhone
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import br.com.semesperanca.frontendleads.presentation.ui.AppColors

internal enum class DetailTab(val labelRes: String) {
    ACTIVITY("Atividades"),
    INSIGHT("Insight"),
    TAGS("Etiquetas"),
    ACTIONS("Ações"),
}

internal data class LocalActivity(
    val id: String,
    val type: String,
    val title: String,
    val description: String,
    val scheduledAt: String,
    val createdAt: String,
    val status: String,
)

internal data class LocalTag(
    val id: String,
    val label: String,
    val color: Color,
)

internal val AVAILABLE_TAGS = listOf(
    LocalTag("1", "Jardim", Color(0xFF4CAF50)),
    LocalTag("2", "Piscina", Color(0xFF2196F3)),
    LocalTag("3", "Suíte", Color(0xFF9E9E9E)),
    LocalTag("4", "Academia", Color(0xFFF44336)),
    LocalTag("5", "Salão de Festas", Color(0xFFCDDC39)),
    LocalTag("6", "Metrô", Color(0xFF3F51B5)),
)

internal val AVAILABLE_ACTIVITY_TYPES = listOf("VISITA", "LIGACAO", "RETORNO", "PROPOSTA", "OUTRO")

internal fun activityTypeIcon(type: String): ImageVector = when (type) {
    "VISITA" -> Icons.Filled.House
    "LIGACAO" -> Icons.Filled.LocalPhone
    "RETORNO" -> Icons.Filled.Restore
    "PROPOSTA" -> Icons.Filled.TaskAlt
    else -> Icons.Filled.AccessTime
}

internal fun activityTypeLabel(type: String): String = when (type) {
    "VISITA" -> "Visita"
    "LIGACAO" -> "Ligação"
    "RETORNO" -> "Retorno"
    "PROPOSTA" -> "Proposta"
    else -> "Outro"
}

internal fun statusConfig(status: String): Pair<String, Color> = when (status) {
    "COMPLETED" -> "Concluída" to AppColors.Green500
    "CANCELLED" -> "Cancelada" to AppColors.Red500
    else -> "Pendente" to AppColors.Amber600
}
