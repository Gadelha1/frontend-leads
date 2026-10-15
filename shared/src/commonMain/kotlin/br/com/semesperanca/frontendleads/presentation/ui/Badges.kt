package br.com.semesperanca.frontendleads.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Pill(
    text: String,
    backgroundColor: Color,
    textColor: Color = AppColors.White,
    modifier: Modifier = Modifier,
    fontSize: Int = 12,
    horizontalPadding: Int = 12,
    verticalPadding: Int = 4,
) {
    Text(
        text = text,
        color = textColor,
        fontSize = fontSize.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .padding(horizontal = horizontalPadding.dp, vertical = verticalPadding.dp),
    )
}

@Composable
fun FilterTitle(label: String) {
    Text(
        text = label,
        color = AppColors.Gray400,
        fontSize = 14.sp,
        fontWeight = FontWeight.Light,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp),
    )
}
