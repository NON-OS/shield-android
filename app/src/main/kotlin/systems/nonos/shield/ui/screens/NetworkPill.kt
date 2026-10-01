package systems.nonos.shield.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.choose
import systems.nonos.shield.ui.state.real

/** The pill's own ground: white at a low alpha, as the tiles. */
private val PILL = Color.White.copy(alpha = 0.08f)

/**
 * The network, as a small pill at the top of home: a dot, its name, and a
 * menu to switch. Cyan with "real money" on mainnet, quiet on Sepolia.
 */
@Composable
internal fun NetworkPill(state: UiState, model: WalletViewModel) {
    var open by remember { mutableStateOf(false) }
    val real = state.network.real
    val colors = MaterialTheme.colorScheme
    Box {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(PILL, CircleShape)
                .clickable(enabled = state.busy == Busy.Idle, role = Role.Button) { open = true }
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Box(
                Modifier.size(
                    8.dp,
                ).background(if (real) colors.primary else colors.onSurfaceVariant, CircleShape),
            )
            Text(
                if (real) "Ethereum" else "Sepolia",
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier.semantics { contentDescription = spoken(real) },
            )
        }
        NetworkMenu(open, onDismiss = { open = false }) { network ->
            open = false
            model.choose(network)
        }
    }
}
