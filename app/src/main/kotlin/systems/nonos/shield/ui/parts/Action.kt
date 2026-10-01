package systems.nonos.shield.ui.parts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * The two weights of action, and no third. Height, corner and mono capitals are set once, so
 * primary and secondary differ in weight only. A disabled action is an outline, since a greyed fill
 * reads as broken. iOS does the same.
 */

/** The least height of every action, so a row of them sits on one line until the font grows. */
private val TALL = 58.dp

/** Capitals need air to stay readable at this size. */
private val TRACK = 1.6.sp

@Composable
internal fun Primary(
    text: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = EDGE,
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        border = if (enabled) null else BorderStroke(HAIR, MaterialTheme.colorScheme.outline),
        modifier = modifier.fillMaxWidth().heightIn(min = TALL),
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            letterSpacing = TRACK,
        )
    }
}

@Composable
internal fun Secondary(
    text: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = EDGE,
        border = BorderStroke(HAIR, MaterialTheme.colorScheme.outline),
        modifier = modifier.fillMaxWidth().heightIn(min = TALL),
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            letterSpacing = TRACK,
        )
    }
}
