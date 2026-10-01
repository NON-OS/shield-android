package systems.nonos.shield.ui.parts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import systems.nonos.shield.core.Coin

/**
 * An amount, as the subject of a screen instead of a line of text. The number and unit are the
 * core's, on one baseline. The raw integer of the smallest unit sits under it in mono, since the
 * rounded decimal hides the value actually spent.
 */
@Composable
internal fun Amount(text: String, unit: String, caption: String, coin: Coin? = null) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (coin != null) CoinLogo(coin)
            Text(text = text, style = MaterialTheme.typography.displayMedium)
            Text(
                text = unit,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 6.dp),
            )
        }
        if (caption.isNotEmpty()) {
            Text(
                text = caption,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
