package systems.nonos.shield.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/** The slippage choices, in basis points, and how a screen names them. */
private val SLIPPAGES =
    listOf(50.toUShort() to "0.5%", 100.toUShort() to "1.0%", 300.toUShort() to "3.0%")

/** How far below the quote a swap may land, as one quiet row of choices. */
@Composable
internal fun SlippageRow(bps: UShort, onPick: (UShort) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(
            "Max slippage",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Row(horizontalArrangement = Arrangement.End) {
            for ((choice, name) in SLIPPAGES) {
                TextButton(onClick = { onPick(choice) }) {
                    val colors = MaterialTheme.colorScheme
                    Text(
                        name,
                        color = if (choice == bps) colors.primary else colors.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
