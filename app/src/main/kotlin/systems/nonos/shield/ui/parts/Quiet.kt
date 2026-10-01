package systems.nonos.shield.ui.parts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * The actions a screen offers without asking for them, lighter than its main actions and set in
 * mono like everything else the machine offers.
 */

/** A shade larger than a label, because these are targets instead of notes. */
private val ROW = 18.sp

/** What a row that cannot be pressed looks like, as a fraction of full. */
private const val DISABLED = 0.5f

@Composable
internal fun Quiet(title: String, enabled: Boolean = true, onClick: () -> Unit) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        fontSize = ROW,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else DISABLED)
            .clickable(enabled = enabled, onClick = onClick)
            .semantics { role = Role.Button }
            .padding(horizontal = 16.dp, vertical = 14.dp),
    )
}

/** The quiet actions as one block, divided by thin rules instead of gaps. */
@Composable
internal fun QuietGroup(content: @Composable () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = EDGE,
        border = BorderStroke(HAIR, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column { content() }
    }
}

/** A rule between rows of a group, inset so it starts under the text. */
@Composable
internal fun QuietRule() {
    Rule(inset = 16.dp)
}
