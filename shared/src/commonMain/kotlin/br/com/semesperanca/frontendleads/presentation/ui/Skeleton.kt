package br.com.semesperanca.frontendleads.presentation.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun SkeletonPulse(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val transition = rememberInfiniteTransition(label = "skeleton")
    val alpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "skeletonAlpha",
    )
    Box(modifier = modifier.alpha(alpha)) {
        content()
    }
}

@Composable
fun SkeletonBlock(
    modifier: Modifier = Modifier,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(8.dp),
) {
    Box(
        modifier = modifier.clip(shape).background(AppColors.Gray200),
    )
}

@Composable
fun LeadSkeletonList(count: Int = 5) {
    SkeletonPulse {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(count) {
                LeadSkeletonCard()
            }
        }
    }
}

@Composable
fun LeadSkeletonCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(AppColors.White)
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            SkeletonBlock(modifier = Modifier.size(28.dp), shape = CircleShape)
            Spacer(modifier = Modifier.width(10.dp))
            SkeletonBlock(modifier = Modifier.height(14.dp).width(120.dp))
            Spacer(modifier = Modifier.weight(1f))
            SkeletonBlock(modifier = Modifier.height(12.dp).width(80.dp))
        }
        Spacer(modifier = Modifier.height(14.dp))
        SkeletonBlock(modifier = Modifier.height(12.dp).fillMaxWidth(0.5f))
        Spacer(modifier = Modifier.height(10.dp))
        SkeletonBlock(modifier = Modifier.height(12.dp).fillMaxWidth(0.7f))
        Spacer(modifier = Modifier.height(14.dp))
        SkeletonBlock(modifier = Modifier.height(26.dp).width(72.dp), shape = RoundedCornerShape(50))
    }
}
