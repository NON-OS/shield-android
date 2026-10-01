package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.clearSend
import systems.nonos.shield.ui.state.clearSwap

/**
 * The public account's panels: send, receive and swap. Leaving the send panel forgets its review
 * and what was sent, so coming back starts from an empty form.
 */
@Composable
internal fun AccountPanel(
    showing: Panel,
    state: UiState,
    model: WalletViewModel,
    onPanel: (Panel) -> Unit,
) {
    val onBack = { onPanel(Panel.Overview) }
    when (showing) {
        Panel.AccountSend -> AccountSendPanel(state, model) {
            model.clearSend()
            onBack()
        }
        Panel.Swap -> SwapPanel(state, model) {
            model.clearSwap()
            onBack()
        }
        Panel.Shield -> ShieldPanel(state, model, onPanel, onBack)
        else -> AccountReceivePanel(state, model, onBack)
    }
}
