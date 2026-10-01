package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.PublicBalances
import systems.nonos.shield.core.amountUnit
import systems.nonos.shield.ui.parts.Ask
import systems.nonos.shield.ui.parts.CoinPicker
import systems.nonos.shield.ui.parts.Field
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Typing
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.label
import systems.nonos.shield.ui.state.publicHere
import systems.nonos.shield.ui.state.reviewSend

/**
 * What a public send needs: which coin, where, and how much. The core reads
 * every field and the chain before anything is shown, and refuses in a
 * sentence what the network or the token would refuse.
 */
@Composable
internal fun AccountSendForm(state: UiState, model: WalletViewModel) {
    var coin by remember { mutableStateOf(Coin.NOX) }
    var to by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    val unit = amountUnit(coin)
    CoinPicker(coin, listOf(Coin.NOX, Coin.ETH, Coin.USDC)) { coin = it }
    Field(Ask("To 0x address", Typing.exact), to) { to = it.trim() }
    Field(Ask("Amount in $unit", Typing.amount), amount) { amount = it }
    held(state.publicHere, coin)?.let {
        Lead("This account holds $it $unit on ${state.network.label}.")
    }
    val reviewing = state.busy == Busy.Reviewing
    Primary(
        text = if (reviewing) "Working it out" else "Review",
        enabled = state.busy == Busy.Idle && to.isNotEmpty() && amount.isNotEmpty(),
    ) { model.reviewSend(coin, to, amount) }
}

/** What the account holds of `coin`, as the core formatted it. */
private fun held(here: PublicBalances?, coin: Coin): String? = when (coin) {
    Coin.ETH -> here?.eth
    Coin.NOX -> here?.nox
    Coin.USDC -> here?.usdc
}
