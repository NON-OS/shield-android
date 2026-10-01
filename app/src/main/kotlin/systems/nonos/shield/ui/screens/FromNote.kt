package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.accountName
import systems.nonos.shield.ui.state.activeName

/**
 * Which account a private send spends from, and when it goes to another account of this wallet,
 * that it is a private transfer with a relay fee. Value that arrived only just before can be linked
 * across the two accounts by timing, so that is said too.
 */
@Composable
internal fun FromNote(state: UiState, withdraw: Boolean) {
    Lead("From ${state.activeName}.")
    val own = state.ownTarget ?: return
    if (withdraw) return
    Lead(
        "This moves value to ${accountName(own)} of this wallet, as a private transfer " +
            "that pays the relay fee.",
    )
    if (state.arrivedRecently) {
        Lead(
            "Some of this value arrived only recently. Moving it on soon after can link the two " +
                "accounts by timing. Waiting first makes that harder.",
        )
    }
}
