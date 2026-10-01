package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import systems.nonos.shield.ui.parts.Settle
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.clearDeposit

/**
 * The unlocked wallet. The balance is the core's arithmetic over this device's notes, and nothing
 * here adds up money. Each panel settles in when the holder moves between them.
 */
@Composable
internal fun BalanceScreen(state: UiState, model: WalletViewModel) {
    var panel by remember { mutableStateOf(Panel.Overview) }
    Settle(panel) { showing ->
        when (showing) {
            Panel.Send, Panel.Withdraw ->
                SpendPanel(state, model, withdraw = showing == Panel.Withdraw) {
                    panel = Panel.Shield
                }
            Panel.Receive ->
                ReceivePanel(
                    state,
                    model,
                    onPublic = { panel = Panel.AccountReceive },
                    onBack = { panel = Panel.Shield },
                )
            Panel.Deposit -> DepositPanel(state, model) {
                model.clearDeposit()
                panel = Panel.Shield
            }
            Panel.Settings, Panel.Accounts, Panel.Keys ->
                OwnerPanel(showing, state, model) { panel = it }
            Panel.Overview -> OverviewPanel(state, model) { panel = it }
            // The public account's send and receive.
            else -> AccountPanel(showing, state, model) { panel = it }
        }
    }
}
