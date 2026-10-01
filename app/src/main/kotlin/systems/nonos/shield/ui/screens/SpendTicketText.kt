package systems.nonos.shield.ui.screens

import systems.nonos.shield.core.SendStatus
import systems.nonos.shield.core.SpendTicket
import systems.nonos.shield.ui.state.UiState

/** Where a proved spend stands, in one sentence, the same as on iOS. */
internal fun headline(state: UiState): String {
    val refusal = state.relay?.refusal
    return when {
        state.sentStatus == SendStatus.CONFIRMED -> "Settled on the pool."
        refusal != null -> "The lander refused: $refusal"
        state.notLanded -> "Not landed yet. You can settle it yourself."
        else -> landing(state.relayState?.status)
    }
}

/** What the lander last said about a spend handed to it. */
private fun landing(status: String?): String = when (status) {
    "settled" -> "Settled on the pool."
    "refused" -> "The lander refused it. Take back returns the notes."
    "settling" -> "The lander is settling it."
    "queued", "scheduled" -> "Handed over Tor. Waiting to be landed."
    else -> "Proved and sealed on this phone."
}

/** The anonymity defaults a spend gave up, by name, or that it kept them all. */
internal fun defaults(ticket: SpendTicket): String =
    if (ticket.weakened.isEmpty()) "all kept" else "gave up " + ticket.weakened.joinToString()

/** The fee a send pays, said before it is proved: flat for a transfer, a percentage out. */
internal fun feeLine(withdraw: Boolean, fee: String, unit: String): String = if (withdraw) {
    "Fee: 0.50% of the amount and a network part, paid from the shield."
} else {
    "Fee from $fee $unit, the same for everyone, with a network part that follows gas, " +
        "paid from the shield."
}
