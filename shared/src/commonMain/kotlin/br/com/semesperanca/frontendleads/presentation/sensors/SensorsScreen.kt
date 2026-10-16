package br.com.semesperanca.frontendleads.presentation.sensors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.semesperanca.frontendleads.presentation.ui.AppColors

@Composable
fun SensorsScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Sensors,
            contentDescription = null,
            tint = AppColors.Gray300,
            modifier = Modifier.size(56.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Nenhum sensor associado",
            color = AppColors.Gray400,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
        )
    }
}
