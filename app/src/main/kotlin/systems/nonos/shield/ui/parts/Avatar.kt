package systems.nonos.shield.ui.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Hex digits of the address that pick each hue, after the `0x`. */
private const val DIGITS = 6
private const val SECOND = 8
private const val DEGREES = 360
private const val HEX = 16
private const val SATURATION = 0.55f
private const val BRIGHTNESS = 0.85f

/**
 * A circle coloured from an address, the same as on iOS, so each account is known at a glance. It
 * carries nothing beyond the address it is drawn from.
 */
@Composable
internal fun Avatar(address: String, size: Dp = 28.dp) {
    Box(
        Modifier
            .size(size)
            .background(gradient(address), CircleShape)
            .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape),
    )
}

private fun gradient(address: String) =
    Brush.linearGradient(listOf(hue(address, 0), hue(address, SECOND)))

private fun hue(address: String, at: Int): Color {
    val digits = address.lowercase().removePrefix("0x").drop(at).take(DIGITS)
    val value = digits.toIntOrNull(HEX) ?: 0
    return Color.hsv((value % DEGREES).toFloat(), SATURATION, BRIGHTNESS)
}
