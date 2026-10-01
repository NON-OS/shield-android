package systems.nonos.shield.ui.parts

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

/**
 * A small label in the mono face, capitals spaced wide, with the screen's number beside it. The
 * number is optional, since the same label names fields, which are not sections.
 */
@Composable
internal fun Label(text: String, number: String = "", modifier: Modifier = Modifier) {
    Text(
        text = if (number.isEmpty()) text.uppercase() else "$number  ${text.uppercase()}",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        letterSpacing = 2.4.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
    )
}
