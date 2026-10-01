package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.io.File
import systems.nonos.shield.core.SpendTicket
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Secondary
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.clearSpend
import systems.nonos.shield.ui.state.handToRelayer
import systems.nonos.shield.ui.state.hasLander

/**
 * A spend proved and sealed, and its way to the pool: handed to the lander over its onion service
 * and followed until it settles, or landed from this account when no lander is on or none lands
 * it. The four files can still be shared by hand. Nothing in them is secret.
 */
@Composable
internal fun SpendTicketView(
    state: UiState,
    ticket: SpendTicket,
    model: WalletViewModel,
    onDone: () -> Unit,
) {
    val context = LocalContext.current
    Statement(headline(state))
    SpendTicketFacts(state, ticket, model.hasLander())
    if (model.hasLander() && state.relay?.id == null) {
        Primary(
            if (state.busy == Busy.Handing) "Handing over Tor" else "Send over Tor",
            enabled = state.busy == Busy.Idle,
        ) {
            model.handToRelayer()
        }
    }
    SelfSettleSection(state, model)
    Secondary("Share the files instead") {
        shareFiles(context, ticket.handoffFiles.map(::File), "Hand to a lander")
    }
    Secondary("Done") {
        model.clearSpend()
        model.set { it.copy(selfSettle = null, notLanded = false) }
        onDone()
    }
}
