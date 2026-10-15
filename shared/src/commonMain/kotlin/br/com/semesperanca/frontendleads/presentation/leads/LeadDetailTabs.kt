package br.com.semesperanca.frontendleads.presentation.leads

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.semesperanca.frontendleads.presentation.ui.AddButton
import br.com.semesperanca.frontendleads.presentation.ui.AppColors
import br.com.semesperanca.frontendleads.presentation.ui.Formatters
import br.com.semesperanca.frontendleads.presentation.ui.Pill

@Composable
internal fun ActivityTab(
    activities: List<LocalActivity>,
    onAdd: () -> Unit,
    onActivityClick: (LocalActivity) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Text(
            text = "Atividades do Lead",
            color = AppColors.Gray700,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 16.dp),
        )

        if (activities.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Nenhuma atividade registrada.",
                    color = AppColors.Gray400,
                    fontSize = 13.sp,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(activities, key = { it.id }) { activity ->
                    ActivityCardRow(activity = activity, onClick = { onActivityClick(activity) })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        AddButton(
            onClick = onAdd,
            contentDescription = "Nova atividade",
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ActivityCardRow(activity: LocalActivity, onClick: () -> Unit) {
    val (statusLabel, statusColor) = statusConfig(activity.status)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(AppColors.Gray50)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(AppColors.Emerald100),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = activityTypeIcon(activity.type),
                contentDescription = null,
                tint = AppColors.Emerald700,
                modifier = Modifier.size(18.dp),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = activity.title,
                color = AppColors.Gray900,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = Formatters.formatActivityDate(activity.scheduledAt),
                color = AppColors.Gray400,
                fontSize = 12.sp,
            )
        }
        Pill(
            text = statusLabel,
            backgroundColor = statusColor,
            fontSize = 9,
            horizontalPadding = 8,
            verticalPadding = 2,
        )
    }
}

@Composable
internal fun InsightTab(lead: LeadUi) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 12.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Lightbulb,
                contentDescription = null,
                tint = AppColors.Amber500,
                modifier = Modifier.size(22.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Insight do Lead",
                color = AppColors.Gray700,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        TemperatureCard(lead = lead)

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(AppColors.Gray50)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Análise detalhada em breve",
                color = AppColors.Gray600,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "A geração automática de insights estará disponível em breve.",
                color = AppColors.Gray400,
                fontSize = 13.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }
    }
}

@Composable
private fun TemperatureCard(lead: LeadUi) {
    val temp = lead.temperature.uppercase()
    val cfg = when (temp) {
        "ALTA" -> Triple("Quente", AppColors.Red500, 0.9f)
        "MEDIA" -> Triple("Médio", AppColors.Amber500, 0.6f)
        "BAIXA" -> Triple("Frio", AppColors.Blue500, 0.3f)
        else -> Triple("Sem temperatura", AppColors.Gray500, 0.0f)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Pill(
            text = cfg.first,
            backgroundColor = cfg.second,
            fontSize = 12,
            verticalPadding = 5,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            LinearProgressIndicator(
                progress = { cfg.third },
                color = cfg.second,
                trackColor = AppColors.Gray200,
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(50)),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${(cfg.third * 100).toInt()}%",
                color = AppColors.Gray500,
                fontSize = 11.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
internal fun TagsTab(
    tags: List<LocalTag>,
    onPickTag: () -> Unit,
    onRemoveTag: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Text(
            text = "Etiquetas",
            color = AppColors.Gray700,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 16.dp),
        )

        if (tags.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Nenhuma etiqueta adicionada.",
                    color = AppColors.Gray400,
                    fontSize = 13.sp,
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppColors.Gray50)
                    .padding(12.dp),
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    tags.forEach { tag ->
                        Row(
                            modifier = Modifier
                                .width(96.dp)
                                .clip(RoundedCornerShape(50))
                                .background(tag.color)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) { onRemoveTag(tag.id) }
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = tag.label,
                                color = AppColors.White,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.weight(1f),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "×",
                                color = AppColors.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        AddButton(
            onClick = onPickTag,
            contentDescription = "Adicionar etiqueta",
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
internal fun ActionsTab(
    isArchived: Boolean,
    onArchive: () -> Unit,
    onUnarchive: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = if (isArchived) "Lead Arquivado" else "Ações De Atendimento",
            color = AppColors.Gray700,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 16.dp),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isArchived) {
                OutlineActionButton(text = "Desarquivar Lead", color = AppColors.Emerald600, onClick = onUnarchive)
            } else {
                OutlineActionButton(text = "Cadastrar Proposta", color = AppColors.Emerald600)
                OutlineActionButton(text = "Encaminhar Lead", color = AppColors.Emerald600)
                OutlineActionButton(text = "Arquivar Lead", color = AppColors.Gray400, onClick = onArchive)
            }
        }
    }
}

@Composable
private fun OutlineActionButton(text: String, color: Color, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = color,
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, color),
        modifier = Modifier.width(220.dp).height(46.dp),
    ) {
        Text(text = text, color = color, fontWeight = FontWeight.SemiBold)
    }
}
