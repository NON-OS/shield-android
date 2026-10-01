package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Secondary
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel

/**
 * The way out of a wallet whose key this device no longer has: destroy it behind the same
 * confirmation as Settings, then restore it from the recovery words. The same words as on iOS.
 */
@Composable
internal fun KeyGoneSection(state: UiState, model: WalletViewModel) {
    var confirming by remember { mutableStateOf(false) }
    Lead(
        "The key that sealed this wallet is gone, so it cannot be opened on this device again. " +
            "Destroy it here, then restore it from the recovery words.",
    )
    if (confirming) {
        WipeConfirmation(state, model) { confirming = false }
    } else {
        Secondary("Destroy this wallet", enabled = state.busy == Busy.Idle) { confirming = true }
    }
}
