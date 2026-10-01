package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.core.PublicSent
import systems.nonos.shield.core.SendStatus
import systems.nonos.shield.ui.parts.Label
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Secondary
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.parts.Value
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.copyValue

/** The approval or the deposit on its way. A mined approval leads to the deposit. */
@Composable
internal fun ShieldSentView(
    state: UiState,
    sent: PublicSent,
    model: WalletViewModel,
    steps: ShieldSteps,
) {
    val (next, onDone) = steps.next to steps.done
    val approval = state.shielding?.approval == true
    Statement(headline(state.sentStatus, approval))
    Label("Transaction")
    Value(sent.hash)
    Secondary("Copy link") { model.copyValue(sent.link) }
    val mined = state.sentStatus == SendStatus.CONFIRMED
    if (approval && mined) {
        Primary("Review the deposit", enabled = state.busy == Busy.Idle, onClick = next)
    } else if (mined) {
        Lead(
            "The note is in the pool. It can be spent once the relayer publishes the next root, " +
                "usually within a minute.",
        )
        Primary("Done", onClick = onDone)
    }
}

private fun headline(status: SendStatus?, approval: Boolean): String = when (status) {
    SendStatus.CONFIRMED -> if (approval) "The pool may take it now." else "Shielded."
    SendStatus.FAILED -> "Mined, but it failed. Nothing moved except the network fee."
    else -> (if (approval) "Approval" else "Deposit") + " sent. Waiting for a block."
}
