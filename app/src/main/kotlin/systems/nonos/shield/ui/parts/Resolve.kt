package systems.nonos.shield.ui.parts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

/**
 * A line that resolves character by character, which is the brand's own rule for a title arriving.
 * The whole reveal is capped, so long and short lines take the same time. It runs once when the
 * line appears, never on recomposition.
 */
@Composable
internal fun resolved(text: String): String {
    var shown by remember(text) { mutableIntStateOf(0) }
    LaunchedEffect(text) {
        val step = (WHOLE / text.length.coerceAtLeast(1)).coerceAtMost(STEP)
        while (shown < text.length) {
            delay(step)
            shown += 1
        }
    }
    return text.take(shown)
}

/** The longest the whole line may take, in milliseconds. */
private const val WHOLE = 420L

/** The longest a single character may take, so short lines are not sluggish. */
private const val STEP = 22L
