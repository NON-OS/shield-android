package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Quiet
import systems.nonos.shield.ui.parts.QuietGroup
import systems.nonos.shield.ui.parts.Rule
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.Tile
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.parts.shortened
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.LocalAuthenticator
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.accountName
import systems.nonos.shield.ui.state.addAccount
import systems.nonos.shield.ui.state.selectAccount

/**
 * The accounts of this wallet, each with its own shield keys, `nox1` address and public account,
 * all from the same recovery words. The view-only accounts follow, and none of them can send.
 */
@Composable
internal fun AccountsPanel(state: UiState, model: WalletViewModel, onBack: () -> Unit) {
    val auth = LocalAuthenticator.current
    val idle = state.busy == Busy.Idle
    Screen(frame("05", "Accounts", state, model).over(Backdrop.Settings), onBack = onBack) {
        Tile {
            state.accountList.forEachIndexed { i, account ->
                if (i > 0) Rule()
                Pick(
                    title = accountName(account.index),
                    detail = if (account.active) "In use" else shortened(account.publicAddress),
                    enabled = idle && !account.active,
                ) { model.selectAccount(account.index) }
            }
        }
        QuietGroup { Quiet("Add an account", idle) { model.addAccount(auth) } }
        Lead(
            "Every account comes from the same recovery words. Moving value between two of " +
                "them is a private transfer and pays the relay fee.",
        )
        WatchedSection(state, model)
    }
}
