package systems.nonos.shield.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import systems.nonos.shield.core.Coin
import systems.nonos.shield.ui.parts.Act
import systems.nonos.shield.ui.parts.Ask
import systems.nonos.shield.ui.parts.CoinMenu
import systems.nonos.shield.ui.parts.Field
import systems.nonos.shield.ui.parts.Glyph
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.RoundAction
import systems.nonos.shield.ui.parts.Tile
import systems.nonos.shield.ui.parts.Typing
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.fillMax
import systems.nonos.shield.ui.state.reviewSwap

/**
 * What a swap needs, as the familiar pair of boxes: what is paid, with Max, a
 * flip between them, what comes back, and the slippage allowed. The core
 * reads both pools and quotes when the holder asks for the review.
 */
@Composable
internal fun SwapForm(state: UiState, model: WalletViewModel) {
    var from by remember { mutableStateOf(Coin.ETH) }
    var to by remember { mutableStateOf(Coin.NOX) }
    var amount by remember { mutableStateOf("") }
    var slippage by remember { mutableStateOf(50.toUShort()) }
    SwapBox("You pay", from, { from = it }) {
        Field(Ask("Amount", Typing.amount), amount) { amount = it }
        TextButton(
            onClick = { model.fillMax(from) { amount = it } },
            enabled = state.busy == Busy.Idle,
        ) {
            Text("Max")
        }
    }
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        RoundAction(Act("Flip", Glyph.Flip)) {
            val was = from
            from = to
            to = was
        }
    }
    SwapBox("You receive", to, { to = it }) {
        Text("Quoted on review", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    SlippageRow(slippage) { slippage = it }
    Primary(
        text = if (state.busy == Busy.Reviewing) "Reading the pools" else "Review swap",
        enabled = state.busy == Busy.Idle && amount.isNotEmpty() && from != to,
    ) { model.reviewSwap(from, to, amount, slippage) }
}

/** One side of a swap: what it is, the coin, and the amount. */
@Composable
private fun SwapBox(
    title: String,
    coin: Coin,
    onPick: (Coin) -> Unit,
    amount: @Composable () -> Unit,
) {
    Tile {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) { amount() }
                CoinMenu(coin, onPick)
            }
        }
    }
}
