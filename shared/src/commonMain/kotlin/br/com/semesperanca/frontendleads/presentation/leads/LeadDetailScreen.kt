package br.com.semesperanca.frontendleads.presentation.leads

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.semesperanca.frontendleads.presentation.ui.AppColors
import br.com.semesperanca.frontendleads.presentation.ui.BackButton
import br.com.semesperanca.frontendleads.presentation.ui.Formatters
import br.com.semesperanca.frontendleads.presentation.ui.Pill
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun LeadDetailScreen(
    lead: LeadUi,
    onBack: () -> Unit,
    onArchive: (id: String) -> Unit,
    onUnarchive: (id: String) -> Unit,
    viewModel: LeadDetailViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    val activeTab = uiState.activeTab
    val activities = uiState.activities
    val tags = uiState.tags
    val showActivitySheet = uiState.showActivitySheet
    val showTagPicker = uiState.showTagPicker
    val selectedActivity = uiState.selectedActivity
    val editingActivity = uiState.editingActivity

    Box(modifier = Modifier.fillMaxSize().background(AppColors.White)) {
        Column(modifier = Modifier.fillMaxSize()) {
            DetailHeader(
                lead = lead,
                onBack = onBack,
                onUnarchive = { onUnarchive(lead.id); onBack() },
            )

            TabBar(activeTab = activeTab, onTabSelected = { viewModel.selectTab(it) })

            when (activeTab) {
                DetailTab.ACTIVITY -> ActivityTab(
                    activities = activities,
                    onAdd = { viewModel.openActivityForm() },
                    onActivityClick = { viewModel.showActivity(it) },
                )
                DetailTab.INSIGHT -> InsightTab(lead = lead)
                DetailTab.TAGS -> TagsTab(
                    tags = tags,
                    onPickTag = { viewModel.openTagPicker() },
                    onRemoveTag = { tagId -> viewModel.removeTag(tagId) },
                )
                DetailTab.ACTIONS -> ActionsTab(
                    isArchived = lead.isArchived,
                    onArchive = { onArchive(lead.id); onBack() },
                    onUnarchive = { onUnarchive(lead.id); onBack() },
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .size(56.dp)
                .clip(CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { /* TODO("Chat") — em breve */ },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Message,
                contentDescription = "Chat",
                tint = AppColors.White,
                modifier = Modifier.size(26.dp),
            )
        }
    }

    if (selectedActivity != null) {
        ActivityDetailSheet(
            activity = selectedActivity!!,
            onDismiss = { viewModel.dismissActivityDetail() },
            onEdit = { viewModel.startEditActivity(selectedActivity!!) },
            onDelete = { viewModel.deleteActivity(selectedActivity!!) },
        )
    }

    if (showActivitySheet) {
        ActivityFormSheet(
            initial = editingActivity,
            onDismiss = { viewModel.dismissActivitySheet() },
            onSave = { type, title, scheduledAt, description ->
                viewModel.saveActivity(type, title, scheduledAt, description)
            },
        )
    }

    if (showTagPicker) {
        TagPickerSheet(
            available = AVAILABLE_TAGS.filter { tag -> tags.none { it.id == tag.id } },
            onDismiss = { viewModel.dismissTagPicker() },
            onAdd = { tag ->
                viewModel.addTag(tag)
            },
        )
    }
}

@Composable
private fun DetailHeader(lead: LeadUi, onBack: () -> Unit, onUnarchive: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BackButton(onClick = onBack)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = lead.name,
                color = AppColors.Gray900,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Pill(
                text = lead.nature,
                backgroundColor = Formatters.natureColor(lead.nature),
                fontSize = 11,
                verticalPadding = 3,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = lead.email,
                color = AppColors.Gray600,
                fontSize = 13.sp,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = lead.phone,
                color = AppColors.Gray600,
                fontSize = 13.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.End,
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(AppColors.Gray50)
                .padding(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Imóvel", color = AppColors.Gray400, fontSize = 11.sp)
                Text(
                    text = Formatters.formatCurrency(lead.propertyValue),
                    color = AppColors.Emerald700,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = lead.propertyName,
                color = AppColors.Gray900,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
            if (lead.message.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = lead.message,
                    color = AppColors.Gray500,
                    fontSize = 12.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Origem: ${lead.source.ifBlank { "—" }}",
                    color = AppColors.Gray400,
                    fontSize = 11.sp,
                )
                Text(
                    text = "Criação: ${Formatters.formatDate(lead.receivedAt)}",
                    color = AppColors.Gray400,
                    fontSize = 11.sp,
                )
            }
        }

        if (lead.isArchived) {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onUnarchive,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.Emerald600,
                    contentColor = AppColors.White,
                ),
                modifier = Modifier.fillMaxWidth().height(44.dp),
            ) {
                Text(text = "Desarquivar Lead", color = AppColors.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun TabBar(activeTab: DetailTab, onTabSelected: (DetailTab) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        val tabs = listOf(
            DetailTab.ACTIVITY to Icons.Filled.AccessTime,
            DetailTab.INSIGHT to Icons.Filled.Lightbulb,
            DetailTab.TAGS to Icons.Filled.LocalOffer,
            DetailTab.ACTIONS to Icons.Filled.GpsFixed,
        )
        tabs.forEach { (tab, icon) ->
            val selected = activeTab == tab
            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (selected) AppColors.Emerald600 else AppColors.Gray200)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { onTabSelected(tab) },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = tab.labelRes,
                    tint = if (selected) AppColors.White else AppColors.Gray600,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}
