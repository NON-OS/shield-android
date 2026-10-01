package systems.nonos.shield.ui.parts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * The machine's own line, at the bottom of every screen: facts a holder can check at a glance and
 * the app cannot fake. It is small and dim so it never competes with the screen's subject.
 */
@Composable
internal fun StatusLine(parts: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = SIDE, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = parts.joinToString("  ·  "),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = SMALL,
            letterSpacing = 0.6.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/** Smaller than any other type in the app, because it is never the point. */
private val SMALL = 12.sp
