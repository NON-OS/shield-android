package systems.nonos.shield.ui.parts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * The way back, in the place a thumb looks for it: a small target at the top left of every screen.
 * The chevron is drawn, not imported, to keep an icon pack out of the wallet. It carries a label so
 * a screen reader can name it.
 */
@Composable
internal fun Back(onClick: () -> Unit) {
    val colour = MaterialTheme.colorScheme.onSurfaceVariant
    Box(
        modifier = Modifier
            .size(TARGET)
            .clickable(onClick = onClick)
            .semantics {
                role = Role.Button
                contentDescription = BACK
            },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(width = ARM, height = RISE)) {
            val chevron = Path().apply {
                moveTo(size.width, 0f)
                lineTo(0f, size.height / 2f)
                lineTo(size.width, size.height)
            }
            drawPath(
                path = chevron,
                color = colour,
                style = Stroke(width = STROKE, cap = StrokeCap.Round, join = StrokeJoin.Round),
            )
        }
    }
}

/** What a screen reader says. The word, not the shape. */
private const val BACK = "Back"

/** A target a thumb hits without aiming. */
private val TARGET = 44.dp

/** The chevron itself, which is much smaller than the target around it. */
private val ARM = 9.dp
private val RISE = 16.dp
private const val STROKE = 4f
