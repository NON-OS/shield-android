package systems.nonos.shield.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.amountUnit
import systems.nonos.shield.core.relayFee
import systems.nonos.shield.ui.parts.Ask
import systems.nonos.shield.ui.parts.CoinPicker
import systems.nonos.shield.ui.parts.Field
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Typing
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.SpendOrder
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.checkTarget
import systems.nonos.shield.ui.state.checkUsed
import systems.nonos.shield.ui.state.spend

/**
 * What a spend needs: where it goes and how much. The relayer and its fee are the pool's, the same
 * for everyone, so nothing about them is typed. The core checks every field and the amount.
 */
@Composable
internal fun SpendForm(state: UiState, model: WalletViewModel, withdraw: Boolean) {
    var coin by remember { mutableStateOf(Coin.NOX) }
    var to by remember { mutableStateOf(if (withdraw) state.freshAddress.orEmpty() else "") }
    var amount by remember { mutableStateOf("") }
    val unit = amountUnit(coin)
    val early = state.tooSoon
    if (early != null) {
        EarlyConfirm(state, model, early)
        return
    }
    Lead(HONEST)
    CoinPicker(coin) { coin = it }
    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
        Field(Ask(if (withdraw) "To EVM address" else "To wallet address", Typing.exact), to) {
            to = it.trim()
            if (withdraw) model.checkUsed(to) else model.checkTarget(to)
        }
        Field(Ask("Amount in $unit: ${sizes(coin)}", Typing.amount), amount) { amount = it }
    }
    WithdrawNote(state, withdraw, to)
    FromNote(state, withdraw)
    Lead(feeLine(withdraw, relayFee(coin), unit))
    val ready = state.busy == Busy.Idle && to.isNotEmpty() && amount.isNotEmpty()
    Primary(text = "Prove and seal", enabled = ready) {
        model.spend(SpendOrder(coin, to, amount, withdraw))
    }
}

/** The sentence the first send screen owes: what the proof hides, and what it does not. */
private const val HONEST =
    "The proof hides who paid whom and how much. " +
        "Deposits and withdrawals are public standard amounts."

private fun sizes(coin: Coin): String = if (coin == Coin.ETH) {
    "0.01, 0.02, 0.05, 0.1 and so on"
} else {
    "1,000, 2,000, 5,000, 10,000 and so on"
}
