package systems.nonos.shield.ui.parts

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/**
 * How a screen arrives: it settles. A short fade with a few points of rise, no spring or overshoot,
 * only so the eye knows something changed.
 */
private const val IN = 190
private const val OUT = 120
private val RISE = 8.dp

@Composable
internal fun <T> Settle(value: T, content: @Composable (T) -> Unit) {
    val rise = with(LocalDensity.current) { RISE.roundToPx() }
    AnimatedContent(
        targetState = value,
        transitionSpec = {
            (fadeIn(tween(IN)) + slideInVertically(tween(IN)) { rise })
                .togetherWith(fadeOut(tween(OUT)))
        },
        label = "settle",
    ) { settled ->
        content(settled)
    }
}
