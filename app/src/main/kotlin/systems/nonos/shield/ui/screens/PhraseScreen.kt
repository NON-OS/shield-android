package systems.nonos.shield.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.phraseConfirmed

/**
 * The recovery phrase, shown once. The window is secure, so it cannot be captured, and there is no
 * copy path, since these words are the wallet itself.
 */
@Composable
internal fun PhraseScreen(state: UiState, model: WalletViewModel) {
    Screen(
        frame("01", "Recovery phrase", state, model).over(Backdrop.Backup),
        footer = { Primary("I have written them down") { model.phraseConfirmed() } },
    ) {
        Statement("Write these 24 words down.")
        Lead(
            "On paper, not in a photo and not in a manager that syncs. These words " +
                "are the wallet: nothing else can restore it.",
        )
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            state.phrase.forEachIndexed { index, word ->
                Text(
                    text = "${index + 1}. $word",
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}
