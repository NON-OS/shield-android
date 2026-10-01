package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.core.SpendTicket
import systems.nonos.shield.ui.parts.Tile
import systems.nonos.shield.ui.parts.TileRow
import systems.nonos.shield.ui.parts.hash
import systems.nonos.shield.ui.state.UiState

/** Who lands a proved spend, what the lander last said, and the defaults it gave up. */
@Composable
internal fun SpendTicketFacts(state: UiState, ticket: SpendTicket, lander: Boolean) {
    val relayed = state.relayState
    Tile {
        TileRow("Lander", if (lander) "onion service, over Tor" else "you")
        if (relayed != null) {
            TileRow("Status", relayed.status)
            relayed.tx?.let { TileRow("Settlement", hash(it)) }
            relayed.reason?.let { TileRow("Reason", it) }
        }
        TileRow("Anonymity defaults", defaults(ticket))
    }
}
