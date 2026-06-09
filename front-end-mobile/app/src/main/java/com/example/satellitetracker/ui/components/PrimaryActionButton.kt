package com.example.satellitetracker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Botão de ação padrão do app.
 * Use [tonal] = true para ações secundárias (hierarquia visual).
 */
@Composable
fun PrimaryActionButton(
    text: String,
    modifier: Modifier = Modifier,
    tonal: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    val buttonModifier = modifier
        .fillMaxWidth()
        .height(52.dp)

    if (tonal) {
        FilledTonalButton(onClick = onClick, modifier = buttonModifier, shape = shape, enabled = enabled) {
            Text(text = text, style = MaterialTheme.typography.titleSmall)
        }
    } else {
        Button(onClick = onClick, modifier = buttonModifier, shape = shape, enabled = enabled) {
            Text(text = text, style = MaterialTheme.typography.titleSmall)
        }
    }
}
