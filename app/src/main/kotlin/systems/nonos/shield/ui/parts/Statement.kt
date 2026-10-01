package systems.nonos.shield.ui.parts

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.sp

/**
 * The line a screen is about, at title size, for screens whose subject is a sentence. It resolves
 * in the mono face, so each character lands in its final cell. The finished line is laid out
 * invisibly underneath, so the block never grows while it types. A screen reader hears the finished
 * line once, never the copy underneath or a half typed one.
 */

/** Loose enough that two lines of the mono face read as a sentence. */
private val LINE = 38.sp

@Composable
internal fun Statement(text: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.clearAndSetSemantics { contentDescription = text }) {
        Line(text = text, modifier = Modifier.alpha(0f))
        Line(text = resolved(text))
    }
}

@Composable
private fun Line(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onBackground,
        lineHeight = LINE,
        modifier = modifier,
    )
}
