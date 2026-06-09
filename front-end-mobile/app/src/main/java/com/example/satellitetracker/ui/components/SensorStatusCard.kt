package com.example.satellitetracker.ui.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.satellitetracker.data.model.SensorReading
import com.example.satellitetracker.ui.theme.SuccessGreen

/** Card com a condição do céu calculada a partir dos sensores IoT simulados. */
@Composable
fun SensorStatusCard(reading: SensorReading, modifier: Modifier = Modifier) {
    SpaceCard(modifier = modifier) {
        Text(
            text = "🌌 Condição do céu",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        InfoRow(label = "Chuva", value = if (reading.isRaining) "Sim" else "Não")
        InfoRow(label = "Nebulosidade", value = "${reading.cloudiness}%")
        InfoRow(label = "Luminosidade", value = luminosityLabel(reading.luminosity))
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

        val favorable = reading.isObservationFavorable
        Text(
            text = if (favorable) "✅ Observação favorável" else "⚠️ Observação desfavorável",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = if (favorable) SuccessGreen else MaterialTheme.colorScheme.error
        )
    }
}

// Converte o percentual de luminosidade em um rótulo simples
private fun luminosityLabel(luminosity: Int): String = when {
    luminosity < 30 -> "Baixa"
    luminosity < 60 -> "Média"
    else -> "Alta"
}
