package com.example.proyecto_plagas_android.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Agro-Tech Brand Core Palette
val ForestGreen = Color(0xFF1C3A27)
val Emerald = Color(0xFF2E7D32)
val MintGreen = Color(0xFF00E676)
val MintLime = Color(0xFF81C784)
val LimeAccent = Color(0xFF66BB6A)

// Backgrounds & Surfaces
val LightBackground = Color(0xFFF5F7F5)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFE2E8E2)

val DarkBackground = Color(0xFF111A14)
val DarkSurface = Color(0xFF1A261E)
val DarkSurfaceVariant = Color(0xFF27382B)

// Severity Colors
val SeverityLowGreen = Color(0xFF2E7D32)
val SeverityLowContainerGreen = Color(0xFFE8F5E9)
val SeverityMediumAmber = Color(0xFFFFA000)
val SeverityMediumContainerAmber = Color(0xFFFFF8E1)
val SeverityHighRed = Color(0xFFD32F2F)
val SeverityHighContainerRed = Color(0xFFFFEBEE)
val SeverityCriticalDarkRed = Color(0xFFB71C1C)

// Material 3 Tokens - Light Theme
val md_theme_light_primary = Emerald
val md_theme_light_onPrimary = Color.White
val md_theme_light_primaryContainer = Color(0xFFA5D6A7)
val md_theme_light_onPrimaryContainer = ForestGreen
val md_theme_light_secondary = ForestGreen
val md_theme_light_onSecondary = Color.White
val md_theme_light_secondaryContainer = Color(0xFFC8E6C9)
val md_theme_light_onSecondaryContainer = Color(0xFF0A1F13)
val md_theme_light_tertiary = LimeAccent
val md_theme_light_onTertiary = ForestGreen
val md_theme_light_tertiaryContainer = Color(0xFFB9F6CA)
val md_theme_light_onTertiaryContainer = Color(0xFF003816)
val md_theme_light_background = LightBackground
val md_theme_light_onBackground = Color(0xFF191C19)
val md_theme_light_surface = LightSurface
val md_theme_light_onSurface = Color(0xFF191C19)
val md_theme_light_surfaceVariant = LightSurfaceVariant
val md_theme_light_onSurfaceVariant = Color(0xFF424942)
val md_theme_light_outline = Color(0xFF727971)

// Material 3 Tokens - Dark Theme
val md_theme_dark_primary = MintLime
val md_theme_dark_onPrimary = DarkBackground
val md_theme_dark_primaryContainer = ForestGreen
val md_theme_dark_onPrimaryContainer = Color(0xFFA5D6A7)
val md_theme_dark_secondary = LimeAccent
val md_theme_dark_onSecondary = DarkBackground
val md_theme_dark_secondaryContainer = Color(0xFF1C3A27)
val md_theme_dark_onSecondaryContainer = Color(0xFFC8E6C9)
val md_theme_dark_tertiary = MintGreen
val md_theme_dark_onTertiary = DarkBackground
val md_theme_dark_tertiaryContainer = Color(0xFF005221)
val md_theme_dark_onTertiaryContainer = Color(0xFFB9F6CA)
val md_theme_dark_background = DarkBackground
val md_theme_dark_onBackground = Color(0xFFE1E3DF)
val md_theme_dark_surface = DarkSurface
val md_theme_dark_onSurface = Color(0xFFE1E3DF)
val md_theme_dark_surfaceVariant = DarkSurfaceVariant
val md_theme_dark_onSurfaceVariant = Color(0xFFC2C9C2)
val md_theme_dark_outline = Color(0xFF8C938B)

@Immutable
data class SeverityColors(
    val low: Color = SeverityLowGreen,
    val lowContainer: Color = SeverityLowContainerGreen,
    val medium: Color = SeverityMediumAmber,
    val mediumContainer: Color = SeverityMediumContainerAmber,
    val high: Color = SeverityHighRed,
    val highContainer: Color = SeverityHighContainerRed,
    val critical: Color = SeverityCriticalDarkRed,
)

val LocalSeverityColors = staticCompositionLocalOf { SeverityColors() }
