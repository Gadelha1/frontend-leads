package br.com.semesperanca.frontendleads.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

data class FilterItem(
    val label: String,
    val icon: DrawableResource,
)

@Composable
fun FilterRow(
    items: List<FilterItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    itemSize: Dp = 40.dp,
    bottomPadding: Dp = 8.dp,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = bottomPadding),
        horizontalArrangement = Arrangement.Center,
    ) {
        items.forEachIndexed { index, item ->
            val selected = selectedIndex == index
            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(itemSize)
                    .clip(CircleShape)
                    .background(if (selected) AppColors.Emerald600 else AppColors.Gray200)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { onSelect(index) },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(item.icon),
                    contentDescription = item.label,
                    tint = if (selected) AppColors.White else AppColors.Gray600,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}
