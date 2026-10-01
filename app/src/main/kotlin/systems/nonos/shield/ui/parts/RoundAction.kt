package systems.nonos.shield.ui.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

/**
 * One of the things a holder does from a screen: a circle with its sign and
 * a word under it. Cyan when it can be pressed, an outline when not.
 */
@Composable
internal fun RoundAction(
    act: Act,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val (title, glyph) = act
    val colors = MaterialTheme.colorScheme
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.clickable(enabled = enabled, role = Role.Button, onClick = onClick),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(56.dp)
                .background(if (enabled) colors.primary else Color.Transparent, CircleShape)
                .border(HAIR, if (enabled) Color.Transparent else colors.outline, CircleShape),
        ) {
            GlyphIcon(glyph, if (enabled) colors.onPrimary else colors.onSurfaceVariant)
        }
        Text(
            title,
            style = MaterialTheme.typography.labelMedium,
            color = if (enabled) colors.onSurface else colors.onSurfaceVariant,
        )
    }
}
