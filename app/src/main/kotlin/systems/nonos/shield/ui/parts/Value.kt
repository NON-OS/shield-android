package systems.nonos.shield.ui.parts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A value the app is showing to be read off the screen: an address, a hash, a path. It sits on the
 * raised surface in runs of eight, with its length underneath so a reader knows they have all of
 * it.
 */
@Composable
internal fun Value(value: String, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = EDGE,
        border = BorderStroke(HAIR, MaterialTheme.colorScheme.outline),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        ) {
            Text(
                text = groups(value),
                style = MaterialTheme.typography.labelSmall,
                lineHeight = LINE,
            )
            Text(
                text = "${value.length} characters",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** Loose enough that wrapped runs read as rows instead of as a block. */
private val LINE = 25.sp
