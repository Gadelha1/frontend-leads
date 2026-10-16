package br.com.semesperanca.frontendleads.presentation.leads

import androidx.compose.ui.graphics.Color
import br.com.semesperanca.frontendleads.presentation.ui.AppColors
import br.com.semesperanca.frontendleads.resources.Res
import br.com.semesperanca.frontendleads.resources.call
import br.com.semesperanca.frontendleads.resources.history
import br.com.semesperanca.frontendleads.resources.house
import br.com.semesperanca.frontendleads.resources.schedule
import br.com.semesperanca.frontendleads.resources.task_alt
import org.jetbrains.compose.resources.DrawableResource

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

internal fun activityTypeIcon(type: String): DrawableResource = when (type) {
    "VISITA" -> Res.drawable.house
    "LIGACAO" -> Res.drawable.call
    "RETORNO" -> Res.drawable.history
    "PROPOSTA" -> Res.drawable.task_alt
    else -> Res.drawable.schedule
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
