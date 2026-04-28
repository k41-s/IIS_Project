package hr.algebra.iis_client_app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import hr.algebra.iis_client_app.ThemeMode

private val LightColorScheme = lightColorScheme(
    primary = ColorMidnightBlue,
    onPrimary = Color.White,

    secondary = ColorBurgundy,
    onSecondary = Color.White,

    tertiary = ColorDeepPlum,
    onTertiary = Color.White,

    background = LightBackground,
    surface = LightSurface,
    onBackground = ColorMidnightBlue,
    onSurface = ColorMidnightBlue
)

private val DarkColorScheme = darkColorScheme(
    primary = ColorBurgundy,
    onPrimary = Color.White,
    
    secondary = ColorDeepPlum,
    onSecondary = Color.White,

    background = ColorMidnightBlue,

    surface = ColorDarkSlateBlue,

    onBackground = Color.White,
    onSurface = Color.White
)

private val MonochromeColorScheme = lightColorScheme(
    primary = MonoBlack,
    onPrimary = MonoWhite,
    secondary = MonoMediumGray,
    onSecondary = MonoWhite,
    background = MonoWhite,
    surface = MonoLightGray,
    onBackground = MonoBlack,
    onSurface = MonoBlack,
    primaryContainer = MonoBlack,
    onPrimaryContainer = MonoWhite,
    surfaceVariant = MonoWhite,
    onSurfaceVariant = MonoBlack
)

@Composable
fun AppTheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val colorScheme = when (themeMode) {
        ThemeMode.SYSTEM -> if (isSystemDark) DarkColorScheme else LightColorScheme
        ThemeMode.LIGHT -> LightColorScheme
        ThemeMode.DARK -> DarkColorScheme
        ThemeMode.MONOCHROME -> MonochromeColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
