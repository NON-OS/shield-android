package systems.nonos.shield.ui.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.dp
import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.amountUnit

private val CHIP = Color.White.copy(alpha = 0.08f)

/** The coin in a swap box: its mark and symbol, and a menu of the three. */
@Composable
internal fun CoinMenu(coin: Coin, onPick: (Coin) -> Unit) {
    var open by remember { mutableStateOf(false) }
    Box {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(CHIP, CircleShape)
                .clickable(role = Role.Button, onClickLabel = "Choose the coin") { open = true }
                .padding(horizontal = 10.dp, vertical = 8.dp),
        ) {
            CoinLogo(coin, side = 24.dp)
            Text(amountUnit(coin), style = MaterialTheme.typography.titleSmall)
        }
        DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
            for (choice in listOf(Coin.ETH, Coin.NOX, Coin.USDC)) {
                DropdownMenuItem(text = { Text(amountUnit(choice)) }, onClick = {
                    open = false
                    onPick(choice)
                })
            }
        }
    }
}
