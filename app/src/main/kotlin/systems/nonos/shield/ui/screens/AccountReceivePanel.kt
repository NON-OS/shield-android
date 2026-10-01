package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.Fact
import systems.nonos.shield.ui.parts.Facts
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.QrCode
import systems.nonos.shield.ui.parts.RealMoney
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.Value
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.copyValue

/**
 * The public `0x` address, to receive ETH and NOX at. It is the same on every network and anyone
 * can see what it holds, so the shielded address sits beside it.
 */
@Composable
internal fun AccountReceivePanel(state: UiState, model: WalletViewModel, onBack: () -> Unit) {
    Screen(
        frame("03", "Receive", state, model).over(Backdrop.Receive),
        onBack = onBack,
        footer = {
            Primary("Copy address", enabled = state.account.isNotEmpty()) {
                model.copyValue(state.account)
            }
        },
    ) {
        RealMoney(state.network)
        Lead("Public: anyone can see what this address holds and sends.")
        if (state.account.isNotEmpty()) QrCode(state.account)
        Value(state.account)
        Facts {
            Fact("networks", "Ethereum and Sepolia, one address")
            Fact("derived at", "m/44'/60'/0'/0/0")
        }
    }
}
