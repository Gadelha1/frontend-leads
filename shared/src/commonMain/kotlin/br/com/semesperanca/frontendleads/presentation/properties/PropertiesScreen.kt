package br.com.semesperanca.frontendleads.presentation.properties

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.semesperanca.frontendleads.presentation.ui.AddButton
import br.com.semesperanca.frontendleads.presentation.ui.AppColors
import br.com.semesperanca.frontendleads.resources.Res
import br.com.semesperanca.frontendleads.resources.apartment
import org.jetbrains.compose.resources.painterResource

private sealed interface PropertyOverlay {
    data object None : PropertyOverlay
    data object Create : PropertyOverlay
}

@Composable
fun PropertiesScreen() {
    var overlay by remember { mutableStateOf<PropertyOverlay>(PropertyOverlay.None) }

    Box(modifier = Modifier.fillMaxSize().background(AppColors.Gray50)) {
        when (overlay) {
            PropertyOverlay.Create -> PropertyFormScreen(
                title = "Novo Imóvel",
                onBack = { overlay = PropertyOverlay.None },
            )

            PropertyOverlay.None -> PropertiesEmptyState()
        }

        if (overlay is PropertyOverlay.None) {
            AddButton(
                onClick = { overlay = PropertyOverlay.Create },
                size = 56,
                contentDescription = "Novo imóvel",
                modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
            )
        }
    }
}

@Composable
private fun PropertiesEmptyState() {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(Res.drawable.apartment),
            contentDescription = null,
            tint = AppColors.Gray300,
            modifier = Modifier.size(56.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Nenhum imóvel cadastrado",
            color = AppColors.Gray400,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
        )
    }
}
