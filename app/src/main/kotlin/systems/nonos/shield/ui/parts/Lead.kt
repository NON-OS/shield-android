package systems.nonos.shield.ui.parts

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * The sentence a screen is allowed. What a screen knows belongs in [Facts], and what it means fits
 * one line above them. There is no second lead.
 */
@Composable
internal fun Lead(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier,
    )
}
