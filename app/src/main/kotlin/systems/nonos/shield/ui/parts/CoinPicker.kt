package systems.nonos.shield.ui.parts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.amountUnit

/**
 * Which coin a form is about, as one action per coin of which the chosen one
 * is filled: all three for the public account, ETH and NOX for the shield,
 * which holds no USDC. A menu would hide which one is chosen until opened.
 */
@Composable
internal fun CoinPicker(
    coin: Coin,
    coins: List<Coin> = listOf(Coin.NOX, Coin.ETH),
    onPick: (Coin) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Coin, ${amountUnit(coin)} chosen" },
    ) {
        for (choice in coins) {
            val name = amountUnit(choice)
            if (coin == choice) {
                Primary(name, modifier = Modifier.weight(1f)) { onPick(choice) }
            } else {
                Secondary(name, modifier = Modifier.weight(1f)) { onPick(choice) }
            }
        }
    }
}
