package systems.nonos.shield.ui.parts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** A stroke, as the points it runs through, each a fraction of the square. */
private typealias Line = List<Pair<Float, Float>>

private val UP: List<Line> = listOf(
    listOf(0.5f to 0.8f, 0.5f to 0.2f),
    listOf(0.32f to 0.38f, 0.5f to 0.2f, 0.68f to 0.38f),
)
private val DOWN: List<Line> = listOf(
    listOf(0.5f to 0.2f, 0.5f to 0.8f),
    listOf(0.32f to 0.62f, 0.5f to 0.8f, 0.68f to 0.62f),
)
private val SWAP: List<Line> = listOf(
    listOf(0.33f to 0.8f, 0.33f to 0.2f),
    listOf(0.18f to 0.36f, 0.33f to 0.2f, 0.48f to 0.36f),
    listOf(0.67f to 0.2f, 0.67f to 0.8f),
    listOf(0.52f to 0.64f, 0.67f to 0.8f, 0.82f to 0.64f),
)
private val IN: List<Line> = listOf(
    listOf(0.5f to 0.15f, 0.5f to 0.65f),
    listOf(0.32f to 0.48f, 0.5f to 0.65f, 0.68f to 0.48f),
    listOf(0.2f to 0.85f, 0.8f to 0.85f),
)
private val OUT: List<Line> = listOf(
    listOf(0.5f to 0.85f, 0.5f to 0.35f),
    listOf(0.32f to 0.52f, 0.5f to 0.35f, 0.68f to 0.52f),
    listOf(0.2f to 0.15f, 0.8f to 0.15f),
)
private val SHIELD: List<Line> = listOf(
    listOf(
        0.5f to 0.1f, 0.8f to 0.24f, 0.8f to 0.5f, 0.72f to 0.7f, 0.5f to 0.9f,
        0.28f to 0.7f, 0.2f to 0.5f, 0.2f to 0.24f, 0.5f to 0.1f,
    ),
)

/** The signs on the round actions, drawn as strokes so no icon library ships. */
internal enum class Glyph(val lines: List<Line>) {
    Up(UP),
    Down(DOWN),
    Swap(SWAP),
    Flip(SWAP),
    Shield(SHIELD),
    In(IN),
    Out(OUT),
}

/** What a round action is called and the sign it carries. */
internal data class Act(val title: String, val glyph: Glyph)

/** How thick a stroke is, as a share of the square. */
private const val WEIGHT = 0.1f

@Composable
internal fun GlyphIcon(glyph: Glyph, color: Color, side: Dp = 22.dp) {
    Canvas(Modifier.size(side)) {
        val at = { p: Pair<Float, Float> -> Offset(size.width * p.first, size.height * p.second) }
        for (line in glyph.lines) {
            line.zipWithNext { a, b ->
                drawLine(
                    color,
                    at(a),
                    at(b),
                    size.width * WEIGHT,
                    StrokeCap.Round,
                )
            }
        }
    }
}
