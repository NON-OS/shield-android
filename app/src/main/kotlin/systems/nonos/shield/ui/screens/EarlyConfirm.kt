package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import systems.nonos.shield.ui.parts.Ask
import systems.nonos.shield.ui.parts.Field
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Secondary
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.parts.Typing
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.SpendOrder
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.spend

/** The word the owner types to spend notes that have not waited. */
private const val WORD = "EARLY"

/**
 * The notes that would pay are too new. Waiting keeps the deposit and this spend apart. Going
 * early is allowed, but only by typing the word, so it is never a tap made in passing.
 */
@Composable
internal fun EarlyConfirm(state: UiState, model: WalletViewModel, order: SpendOrder) {
    var typed by remember { mutableStateOf("") }
    Statement("These notes are still new.")
    Lead(
        "The pool must add 20 more notes and six hours must pass before they blend in. Sent now, " +
            "anyone watching can link your deposit to this spend by its timing.",
    )
    Field(Ask("Type $WORD to send now anyway", Typing.exact), typed) { typed = it.trim() }
    val idle = state.busy == Busy.Idle
    Primary("Send now anyway", enabled = idle && typed == WORD) {
        model.spend(order.copy(early = true))
    }
    Secondary("Wait instead", enabled = idle) { model.set { it.copy(tooSoon = null) } }
}
