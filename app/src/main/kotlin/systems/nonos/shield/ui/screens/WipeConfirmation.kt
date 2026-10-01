package systems.nonos.shield.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.wipe

/**
 * The words before the action that loses money. It names what goes and what stays. The destructive
 * button is outlined and the safe one filled, so tapping the obvious button keeps the wallet.
 */
@Composable
internal fun WipeConfirmation(state: UiState, model: WalletViewModel, onCancel: () -> Unit) {
    Text(
        text = "This removes the seed, the notes and the key that seals them from this " +
            "device. The 24 words are the only way back. Without them the balance is gone.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Button(
        onClick = onCancel,
        enabled = state.busy == Busy.Idle,
        modifier = Modifier.fillMaxWidth(),
    ) { Text("Keep this wallet") }
    OutlinedButton(
        onClick = { model.wipe() },
        enabled = state.busy == Busy.Idle,
        modifier = Modifier.fillMaxWidth(),
    ) { Text("Destroy it") }
}
