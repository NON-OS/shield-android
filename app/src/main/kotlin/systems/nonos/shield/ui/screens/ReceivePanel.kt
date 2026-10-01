package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.Fact
import systems.nonos.shield.ui.parts.Facts
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.QrCode
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.Secondary
import systems.nonos.shield.ui.parts.Value
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.copyAddress

/**
 * The address to receive at. A copy is marked sensitive and cleared a minute later, and the screen
 * says while it sits in the clipboard. It carries the spend key and an X-Wing key, X25519 with
 * ML-KEM-768, that a sender seals each note's opening to. The public 0x address stays one tap
 * away, for a faucet or an ordinary send.
 */
@Composable
internal fun ReceivePanel(
    state: UiState,
    model: WalletViewModel,
    onPublic: () -> Unit,
    onBack: () -> Unit,
) {
    Screen(
        frame("03", "Receive", state, model).over(Backdrop.Receive),
        onBack = onBack,
        footer = {
            Primary("Copy address", enabled = state.receiving.isNotEmpty()) { model.copyAddress() }
            Secondary("Receive at the 0x address", onClick = onPublic)
        },
    ) {
        Lead("Give this to the sender. It says nothing about the balance or the notes held.")
        if (state.receiving.isNotEmpty()) {
            QrCode(state.receiving)
        }
        Value(state.receiving)
        Facts {
            Fact("sealed with", "X-Wing, X25519 + ML-KEM-768")
            Fact("clipboard", if (state.copied) "cleared after a minute" else "empty")
        }
    }
}
