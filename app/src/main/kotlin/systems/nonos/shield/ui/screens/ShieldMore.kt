package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import systems.nonos.shield.ui.parts.Quiet
import systems.nonos.shield.ui.parts.QuietGroup
import systems.nonos.shield.ui.parts.QuietRule
import systems.nonos.shield.ui.parts.Tile
import systems.nonos.shield.ui.parts.TileRow
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.measureProof
import systems.nonos.shield.ui.state.sync
import systems.nonos.shield.ui.state.takeBack

/**
 * The quieter things the shield offers: reading the pool, taking back what a
 * relayer never settled, timing a proof, and the facts behind a balance.
 */
@Composable
internal fun ShieldMore(state: UiState, model: WalletViewModel) {
    var details by remember { mutableStateOf(false) }
    val idle = state.busy == Busy.Idle
    QuietGroup {
        Quiet(
            if (state.busy == Busy.Syncing) "Reading the pool over Tor" else "Read the pool",
            idle,
        ) {
            model.sync()
        }
        if (state.balances.any { it.pending > 0uL }) {
            QuietRule()
            Quiet("Take back what a relayer never settled", idle) { model.takeBack() }
        }
        QuietRule()
        Quiet("Time a proof on this phone", idle) { model.measureProof() }
        QuietRule()
        Quiet(if (details) "Hide details" else "Details", true) { details = !details }
    }
    if (details) {
        Tile {
            TileRow("Pool read at block", state.chain?.head?.toString() ?: "none yet")
            TileRow("Notes held", state.balances.sumOf { it.noteCount.toULong() }.toString())
            TileRow("Key held in", holder(state))
        }
    }
}
