package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel

/**
 * Sending and withdrawing, in three states: the form, the proof in progress, and the sealed spend
 * waiting for a relayer. A transfer goes to another wallet and a withdrawal to an EVM address, both
 * proved here and settled by a relayer for the fee in the proof.
 */
@Composable
internal fun SpendPanel(
    state: UiState,
    model: WalletViewModel,
    withdraw: Boolean,
    onBack: () -> Unit,
) {
    val title = if (withdraw) "Withdraw" else "Send"
    val photo = if (withdraw) Backdrop.Withdraw else Backdrop.Send
    Screen(frame("03", title, state, model).over(photo), onBack = onBack) {
        val proving = state.busy == Busy.Proving || state.busy == Busy.Cancelling
        val ticket = state.spend
        when {
            proving -> ProvingNotice(state, model)
            ticket != null -> SpendTicketView(state, ticket, model, onBack)
            else -> SpendForm(state, model, withdraw)
        }
    }
}
