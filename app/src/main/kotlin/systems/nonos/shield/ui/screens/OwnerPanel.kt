package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel

/** The panels about the owner and not the money: settings, the accounts, and the keys. */
@Composable
internal fun OwnerPanel(
    showing: Panel,
    state: UiState,
    model: WalletViewModel,
    onPanel: (Panel) -> Unit,
) {
    when (showing) {
        Panel.Accounts -> AccountsPanel(state, model) { onPanel(Panel.Overview) }
        Panel.Keys -> KeysPanel(state, model) { onPanel(Panel.Settings) }
        else -> SettingsPanel(state, model, onKeys = { onPanel(Panel.Keys) }) {
            onPanel(Panel.Overview)
        }
    }
}
