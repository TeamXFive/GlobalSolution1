package com.example.satellitetracker.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.satellitetracker.ui.theme.DeepSpace
import com.example.satellitetracker.ui.theme.StarWhite
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

private val TWO_PI = (2 * PI).toFloat()

// Posição e brilho de uma estrela; x e y são frações da tela (0 a 1)
private data class Star(val x: Float, val y: Float, val radius: Float, val phase: Float)

/**
 * Fundo usado por todas as telas: gradiente de céu noturno,
 * estrelas que piscam e satélites animados cruzando a tela.
 */
@Composable
fun SpaceBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    // Semente fixa: as estrelas ficam sempre nas mesmas posições
    val stars = remember {
        val random = Random(seed = 42)
        List(90) {
            Star(
                x = random.nextFloat(),
                y = random.nextFloat(),
                radius = 0.6f + random.nextFloat() * 1.4f,
                phase = random.nextFloat() * TWO_PI
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "space")
    val twinkle by transition.animateFloat(
        initialValue = 0f,
        targetValue = TWO_PI,
        animationSpec = infiniteRepeatable(tween(durationMillis = 5000, easing = LinearEasing)),
        label = "twinkle"
    )
    val satellite1 by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 16000, easing = LinearEasing)),
        label = "satellite1"
    )
    val satellite2 by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(durationMillis = 26000, easing = LinearEasing)),
        label = "satellite2"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF02040C), DeepSpace, Color(0xFF0B1838))
                )
            )
    ) {
        // Céu estrelado com brilho pulsante
        Canvas(modifier = Modifier.fillMaxSize()) {
            stars.forEach { star ->
                val alpha = 0.25f + 0.75f * ((sin(twinkle + star.phase) + 1f) / 2f)
                drawCircle(
                    color = StarWhite.copy(alpha = alpha),
                    radius = star.radius.dp.toPx(),
                    center = Offset(star.x * size.width, star.y * size.height)
                )
            }
        }

        // Satélites cruzando o céu em direções opostas
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "🛰️",
                fontSize = 24.sp,
                modifier = Modifier.offset(
                    x = maxWidth * (satellite1 * 1.3f) - maxWidth * 0.15f,
                    y = maxHeight * 0.10f + 12.dp * sin(satellite1 * TWO_PI)
                )
            )
            Text(
                text = "🛰️",
                fontSize = 16.sp,
                modifier = Modifier.offset(
                    x = maxWidth * (1.15f - satellite2 * 1.3f),
                    y = maxHeight * 0.30f + 10.dp * sin(satellite2 * TWO_PI)
                )
            )
        }

        content()
    }
}
