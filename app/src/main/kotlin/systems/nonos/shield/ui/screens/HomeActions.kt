package systems.nonos.shield.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import systems.nonos.shield.core.networkHasShield
import systems.nonos.shield.ui.parts.Act
import systems.nonos.shield.ui.parts.Glyph
import systems.nonos.shield.ui.parts.RoundAction
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.publicHere

/** The four things a holder does from home. */
@Composable
internal fun HomeActions(state: UiState, onPanel: (Panel) -> Unit) {
    val ready = state.busy == Busy.Idle && state.publicHere != null
    // Where the shield runs, a payment is received privately unless the owner asks otherwise.
    val shielded = networkHasShield(state.network)
    val acts = listOf(
        Triple(Act("Send", Glyph.Up), ready, Panel.AccountSend),
        Triple(
            Act("Receive", Glyph.Down),
            true,
            if (shielded) Panel.Receive else Panel.AccountReceive,
        ),
        Triple(Act("Swap", Glyph.Swap), true, Panel.Swap),
        Triple(Act("Shield", Glyph.Shield), true, Panel.Shield),
    )
    Row(modifier = Modifier.fillMaxWidth()) {
        for ((act, enabled, panel) in acts) {
            RoundAction(act, enabled, Modifier.weight(1f)) { onPanel(panel) }
        }
    }
}
