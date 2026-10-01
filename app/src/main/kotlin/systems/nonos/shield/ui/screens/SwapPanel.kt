package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.core.Network
import systems.nonos.shield.core.networkHasSwaps
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.RealMoney
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.choose
import systems.nonos.shield.ui.state.clearSwap

/**
 * Swapping between ETH, NOX and USDC on mainnet, in three states: the form,
 * the core's review, and what was sent. A token going in needs an exact
 * approval first, reviewed and confirmed the same way.
 */
@Composable
internal fun SwapPanel(state: UiState, model: WalletViewModel, onBack: () -> Unit) {
    Screen(frame("03", "Swap", state, model).over(Backdrop.Send), onBack = onBack) {
        RealMoney(state.network)
        if (!networkHasSwaps(state.network)) {
            Statement("Swaps run on Ethereum mainnet, where the pools are.")
            Primary("Switch to Ethereum") { model.choose(Network.MAINNET) }
            return@Screen
        }
        val sent = state.sent
        val swap = state.swap
        when {
            // After an approval, back to the form to review the swap itself.
            sent != null -> AccountSentView(state, sent, model) {
                if (swap?.approval == true) model.clearSwap() else onBack()
            }
            swap != null -> SwapReviewView(state, swap, model)
            else -> SwapForm(state, model)
        }
    }
}
