package systems.nonos.shield.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * The app's theme, mapped from the brand tokens. There is one palette, the dark one, and the system
 * light setting is ignored. Primary is cyan with ink on it. The book gives errors no colour, so a
 * failure is ordinary text on the raised surface.
 */
@Composable
internal fun ShieldTheme(content: @Composable () -> Unit) {
    val scheme = darkColorScheme(
        primary = Cyan,
        onPrimary = Ink,
        primaryContainer = DeepTeal,
        onPrimaryContainer = OnInk,
        secondary = Cyan,
        background = Ink,
        onBackground = OnInk,
        surface = Surface,
        onSurface = OnInk,
        surfaceVariant = Surface,
        onSurfaceVariant = OnInkMuted,
        outline = Line,
        error = Bad,
        onError = Ink,
    )
    MaterialTheme(colorScheme = scheme, typography = ShieldTypography, content = content)
}
