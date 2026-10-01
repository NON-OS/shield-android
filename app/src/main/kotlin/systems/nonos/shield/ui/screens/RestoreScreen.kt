package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import systems.nonos.shield.ui.parts.Ask
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.Field
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Quiet
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.parts.Typing
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.LocalAuthenticator
import systems.nonos.shield.ui.state.Stage
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.restore
import systems.nonos.shield.ui.state.restoreFromKey
import systems.nonos.shield.ui.state.show

/** How many lines of phrase the box shows before it scrolls. */
private const val PHRASE_LINES = 5

/**
 * Restoring from a phrase. The words are split here and checked by the core,
 * which owns the word list and the checksum.
 */
@Composable
internal fun RestoreScreen(state: UiState, model: WalletViewModel) {
    val auth = LocalAuthenticator.current
    var text by remember { mutableStateOf("") }
    var byKey by remember { mutableStateOf(false) }
    Screen(
        frame("01", "Restore a wallet", state, model).over(Backdrop.Backup),
        onBack = { model.show(Stage.NeedsWallet) },
        footer = {
            Primary(
                text = "Restore",
                enabled = state.busy == Busy.Idle && text.isNotBlank(),
            ) {
                if (byKey) {
                    model.restoreFromKey(auth, text.trim())
                } else {
                    model.restore(auth, text.split(Regex("\\s+")).filter(String::isNotEmpty))
                }
            }
        },
    ) {
        if (byKey) {
            KeyRestore(text) { text = it }
        } else {
            Statement("Type the recovery words: 12, 15, 18, 21 or 24.")
            Lead("In order, separated by spaces. The core owns the word list and the checksum.")
            Field(Ask("Recovery phrase", Typing.exact, PHRASE_LINES), text) { text = it }
        }
        Quiet(if (byKey) "Use recovery words instead" else "Use a private key instead") {
            byKey = !byKey
            text = ""
        }
        if (state.busy == Busy.Finding) {
            Lead(
                "Looking over Tor for the other accounts these words were used with. " +
                    "Looked at ${state.searched} so far.",
            )
        }
    }
}
