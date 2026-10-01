package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.state.UiState

/**
 * How far the newest money in the shield is from blending in: notes still to land in the pool and
 * minutes still to pass. Nothing is shown before a first sync, or once everything is ready.
 */
@Composable
internal fun PrivacyMeter(state: UiState) {
    val chain = state.chain ?: return
    if (state.balances.all { it.spendable == 0uL }) return
    val (notes, minutes) = chain.waitLeaves to chain.waitMinutes
    if (notes == 0u && minutes == 0u) {
        Lead("Ready to spend privately.")
        return
    }
    val parts = listOfNotNull(
        if (notes > 0u) "$notes more notes in the pool" else null,
        if (minutes > 0u) "about ${(minutes + 59u) / 60u} hours" else null,
    )
    val after = parts.joinToString(" and ")
    Lead("Ready to spend privately after $after. Spending sooner can be linked by timing.")
}
