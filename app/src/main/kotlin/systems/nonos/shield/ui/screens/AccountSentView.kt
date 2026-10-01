package systems.nonos.shield.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import systems.nonos.shield.core.PublicSent
import systems.nonos.shield.core.SendStatus
import systems.nonos.shield.ui.parts.Label
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Secondary
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.parts.Value
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.copyValue

/**
 * A send the network has taken, and where it stands. The hash is the receipt, and the explorer link
 * opens only when the owner taps it.
 */
@Composable
internal fun AccountSentView(
    state: UiState,
    sent: PublicSent,
    model: WalletViewModel,
    onDone: () -> Unit,
) {
    Statement(
        when (state.sentStatus) {
            SendStatus.CONFIRMED -> "Sent and mined."
            SendStatus.FAILED -> "Mined, but it failed. Nothing moved except the network fee."
            else -> "Sent. Waiting for a block."
        },
    )
    Label("Transaction")
    Value(sent.hash)
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        Secondary("Copy hash", modifier = Modifier.weight(1f)) { model.copyValue(sent.hash) }
        Secondary("Copy link", modifier = Modifier.weight(1f)) { model.copyValue(sent.link) }
    }
    Primary("Done") { onDone() }
}
