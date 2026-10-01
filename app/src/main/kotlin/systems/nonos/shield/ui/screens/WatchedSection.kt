package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import systems.nonos.shield.core.ViewKind
import systems.nonos.shield.core.WatchedSummary
import systems.nonos.shield.core.amountUnit
import systems.nonos.shield.core.formatAmount
import systems.nonos.shield.ui.parts.Ask
import systems.nonos.shield.ui.parts.Field
import systems.nonos.shield.ui.parts.Label
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Quiet
import systems.nonos.shield.ui.parts.QuietGroup
import systems.nonos.shield.ui.parts.Rule
import systems.nonos.shield.ui.parts.Tile
import systems.nonos.shield.ui.parts.Typing
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.forgetViewKey
import systems.nonos.shield.ui.state.importViewKey

/** Lines a pasted view key needs to be read at a glance. */
private const val KEY_LINES = 3

/**
 * The accounts somebody shared a view key for. They show notes and never send. An incoming key
 * cannot tell a spent note from an unspent one, so its figure is what arrived, not what is held.
 */
@Composable
internal fun WatchedSection(state: UiState, model: WalletViewModel) {
    var key by remember { mutableStateOf("") }
    val idle = state.busy == Busy.Idle
    Label("View only")
    if (state.watched.isNotEmpty()) {
        Tile {
            state.watched.forEachIndexed { i, watched ->
                if (i > 0) Rule()
                Pick("View only ${watched.index + 1u}", figure(watched), enabled = false) {}
            }
        }
        QuietGroup {
            state.watched.forEach { w ->
                Quiet("Forget view only ${w.index + 1u}", idle) { model.forgetViewKey(w.index) }
            }
        }
    }
    Field(Ask("A noxivk1 or noxfvk1 view key", Typing.exact, KEY_LINES), key) { key = it }
    Primary("Add the view key", enabled = idle && key.isNotBlank()) {
        model.importViewKey(key)
        key = ""
    }
}

private fun figure(watched: WatchedSummary): String {
    val shown = watched.balances.joinToString(" · ") {
        formatAmount(it.coin, it.spendable) + " " + amountUnit(it.coin)
    }
    return if (watched.kind == ViewKind.FULL) shown else "received $shown"
}
