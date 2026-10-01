package systems.nonos.shield.ui.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import systems.nonos.shield.core.Network

/**
 * Which network a screen is acting on, said before anything else on it. On mainnet it says real
 * money on a filled cyan band, since a send there cannot be taken back. On Sepolia it says test,
 * quietly.
 */
@Composable
internal fun RealMoney(network: Network) {
    val real = network == Network.MAINNET
    val colors = MaterialTheme.colorScheme
    Text(
        text = if (real) "REAL MONEY  ·  ETHEREUM MAINNET" else "TEST NETWORK  ·  SEPOLIA",
        style = MaterialTheme.typography.labelSmall,
        letterSpacing = 1.6.sp,
        textAlign = TextAlign.Center,
        color = if (real) colors.onPrimary else colors.onSurfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .background(if (real) colors.primary else Color.Transparent, EDGE)
            .border(HAIR, if (real) Color.Transparent else colors.outline, EDGE)
            .padding(vertical = 10.dp)
            .semantics {
                contentDescription = if (real) "Real money, Ethereum mainnet" else "Test network"
            },
    )
}
