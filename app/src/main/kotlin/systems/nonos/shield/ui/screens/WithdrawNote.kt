package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.state.UiState

/**
 * Where a withdrawal goes, said plainly. It is public, so the app fills in an unused address of
 * this wallet, and warns when the address typed instead has been used before.
 */
@Composable
internal fun WithdrawNote(state: UiState, withdraw: Boolean, to: String) {
    if (!withdraw) return
    val text =
        when {
            to.isNotEmpty() && to == state.freshAddress ->
                "A fresh address of this wallet, never used. " +
                    "The words restore it as the next account."
            state.usedAddress ->
                "This address has been used before. " +
                    "A withdrawal to it can be linked to its past. A fresh one is safer."
            else ->
                "A withdrawal is public: its amount and the address it pays " +
                    "can be seen by anyone."
        }
    Lead(text)
}
