package systems.nonos.shield.ui.screens

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import systems.nonos.shield.core.Network

/** The menu under the network pill: Sepolia to test, Ethereum where money is real. */
@Composable
internal fun NetworkMenu(open: Boolean, onDismiss: () -> Unit, onChoose: (Network) -> Unit) {
    DropdownMenu(expanded = open, onDismissRequest = onDismiss) {
        DropdownMenuItem(
            text = { Text("Sepolia, test network") },
            onClick = { onChoose(Network.SEPOLIA) },
        )
        DropdownMenuItem(
            text = { Text("Ethereum, real money") },
            onClick = { onChoose(Network.MAINNET) },
        )
    }
}

/** What a screen reader says for the pill, the same as on iOS. */
internal fun spoken(real: Boolean): String =
    if (real) "Network: Ethereum, real money" else "Network: Sepolia, test"
