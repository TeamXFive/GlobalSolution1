package com.example.satellitetracker.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satellitetracker.data.AlertsData
import com.example.satellitetracker.data.Repository
import com.example.satellitetracker.data.UiState
import com.example.satellitetracker.data.apiErrorMessage
import com.example.satellitetracker.ui.components.ErrorState
import com.example.satellitetracker.ui.components.LoadingState
import com.example.satellitetracker.ui.components.PrimaryActionButton
import com.example.satellitetracker.ui.components.ScreenHeader
import com.example.satellitetracker.ui.components.SpaceBackground
import com.example.satellitetracker.ui.components.SpaceCard
import com.example.satellitetracker.ui.theme.StarGold
import com.example.satellitetracker.ui.theme.SuccessGreen

/**
 * Tela com as leituras dos sensores IoT (vindas da API: GET /leituras),
 * a lógica que decide se a observação é favorável e o alerta ativo (GET /alertas).
 */
@Composable
fun AlertsScreen(onBack: () -> Unit) {
    var reloadKey by remember { mutableStateOf(0) }
    var state by remember { mutableStateOf<UiState<AlertsData>>(UiState.Loading) }

    LaunchedEffect(reloadKey) {
        state = UiState.Loading
        state = try {
            UiState.Success(Repository.getAlertsData())
        } catch (e: Exception) {
            UiState.Error(apiErrorMessage(e))
        }
    }

    SpaceBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ScreenHeader(title = "Alertas e Sensores IoT", onBack = onBack)

            when (val current = state) {
                is UiState.Loading -> LoadingState()
                is UiState.Error -> ErrorState(message = current.message) { reloadKey++ }
                is UiState.Success -> AlertsContent(data = current.data)
            }

            PrimaryActionButton(text = "Voltar", onClick = onBack)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AlertsContent(data: AlertsData) {
    val reading = data.reading

    Text(
        text = "Leituras dos sensores da estação de ${reading?.location ?: "sua região"} 📡",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    if (reading != null) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SensorTile("🌡️", "Temperatura", "${reading.temperature}°C", Modifier.weight(1f))
            SensorTile("🌧️", "Chuva", if (reading.isRaining) "Sim" else "Não", Modifier.weight(1f))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SensorTile("☁️", "Nebulosidade", "${reading.cloudiness}%", Modifier.weight(1f))
            SensorTile("💡", "Luminosidade", "${reading.luminosity}%", Modifier.weight(1f))
        }

        // Card que demonstra a regra usada para decidir a observação
        SpaceCard {
            Text(
                text = "Lógica de observação",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            ConditionRow("Sem chuva no momento", isMet = !reading.isRaining)
            ConditionRow(
                "Nebulosidade ${reading.cloudiness}% (limite 40%)",
                isMet = reading.cloudiness < 40
            )
            ConditionRow(
                "Luminosidade ${reading.luminosity}% (limite 50%)",
                isMet = reading.luminosity < 50
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

            val favorable = reading.isObservationFavorable
            Text(
                text = if (favorable) "✅ Observação favorável" else "⚠️ Observação desfavorável",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = if (favorable) SuccessGreen else MaterialTheme.colorScheme.error
            )
        }
    } else {
        Text(
            text = "Nenhuma leitura de sensor disponível no momento.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    // Alerta ativo vindo da API
    SpaceCard(border = BorderStroke(1.dp, StarGold.copy(alpha = 0.6f))) {
        Text(
            text = "🔔 Alerta de passagem",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = StarGold
        )
        Text(
            text = data.alertMessage ?: "Nenhum alerta ativo no momento.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/** Card pequeno com a leitura de um sensor IoT. */
@Composable
private fun SensorTile(
    emoji: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    SpaceCard(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = emoji, fontSize = 26.sp)
        Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** Linha que mostra se uma condição da regra de observação foi atendida. */
@Composable
private fun ConditionRow(description: String, isMet: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(text = if (isMet) "✅" else "❌")
    }
}
