package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.amountUnit
import systems.nonos.shield.ui.parts.Ask
import systems.nonos.shield.ui.parts.Field
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Typing
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.prepareDeposit

/**
 * A deposit signed in another EVM wallet: the core reads the pool, refuses in a sentence or keeps
 * the note, and returns the two transactions. The screen never builds calldata.
 */
@Composable
internal fun ExternalDepositForm(
    state: UiState,
    model: WalletViewModel,
    coin: Coin,
    amount: MutableState<String>,
) {
    var from by remember { mutableStateOf(state.account) }
    Lead(
        "The EVM address you will send from, and how much. The pool is checked first, and " +
            "nothing is kept if it would refuse.",
    )
    Field(Ask("From EVM address", Typing.exact), from) { from = it.trim() }
    Field(Ask("Amount in ${amountUnit(coin)}", Typing.amount), amount.value) { amount.value = it }
    Primary(
        text = if (state.busy == Busy.Depositing) "Reading the pool" else "Check the pool",
        enabled = state.busy == Busy.Idle && amount.value.isNotEmpty() && from.isNotEmpty(),
    ) { model.prepareDeposit(coin, amount.value, from) }
}
