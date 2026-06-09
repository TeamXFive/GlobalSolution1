package com.example.satellitetracker.ui.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

// O app usa sempre um tema escuro fixo para reforçar o visual de céu noturno
private val SpaceColorScheme = darkColorScheme(
    primary = StarBlue,
    onPrimary = DeepSpace,
    secondary = NebulaPurple,
    onSecondary = DeepSpace,
    tertiary = StarGold,
    onTertiary = DeepSpace,
    background = DeepSpace,
    onBackground = StarWhite,
    surface = SpaceSurface,
    onSurface = StarWhite,
    surfaceVariant = SpaceSurfaceVariant,
    onSurfaceVariant = MoonGray,
    outline = HorizonBlue,
    error = AlertRed,
    onError = DeepSpace
)

@Composable
fun SatelliteTrackerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SpaceColorScheme,
        typography = Typography
    ) {
        // Por padrão o texto do Material 3 seria preto; aqui forçamos branco,
        // já que as telas desenham direto sobre o fundo escuro (sem Surface).
        CompositionLocalProvider(
            LocalContentColor provides SpaceColorScheme.onBackground,
            content = content
        )
    }
}
