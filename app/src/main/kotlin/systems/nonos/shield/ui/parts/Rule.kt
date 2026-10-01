package systems.nonos.shield.ui.parts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A thin rule that divides without shouting, and draws itself in. It lands on one device pixel at
 * any density, in the outline colour. The inset is for lists, where a rule under the text keeps the
 * rows one list.
 */
@Composable
internal fun Rule(modifier: Modifier = Modifier, inset: Dp = 0.dp) {
    val colour = MaterialTheme.colorScheme.outline
    var drawn by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { drawn = true }
    val extent by animateFloatAsState(
        targetValue = if (drawn) 1f else 0f,
        animationSpec = tween(durationMillis = DRAW),
        label = "rule",
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .drawBehind {
                val start = inset.toPx()
                val end = start + (size.width - start) * extent
                drawLine(
                    color = colour,
                    start = Offset(start, 0f),
                    end = Offset(end, 0f),
                    strokeWidth = size.height,
                )
            },
    )
}

/** Long enough to be seen drawing, short enough not to be waited for. */
private const val DRAW = 260
