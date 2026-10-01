package systems.nonos.shield.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import systems.nonos.shield.core.Coin
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.CoinRow
import systems.nonos.shield.ui.parts.Rule
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.Tile
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.publicHere
import systems.nonos.shield.ui.state.readAccount
import systems.nonos.shield.ui.theme.NoxLogo

/**
 * Home: the network, the coins the public account holds there, and the four
 * things a holder does. The shield has its own screen and settings its own
 * button. Every figure is the core's.
 */
@Composable
internal fun OverviewPanel(state: UiState, model: WalletViewModel, onPanel: (Panel) -> Unit) {
    val here = state.publicHere
    Screen(frame("03", "Wallet", state, model).over(Backdrop.Home)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            NoxLogo(height = 18.dp)
            TextButton(onClick = { onPanel(Panel.Settings) }) { Text("Settings") }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            AccountPill(state) { onPanel(Panel.Accounts) }
            NetworkPill(state, model)
        }
        Tile {
            CoinRow(Coin.ETH, here?.eth ?: "–", note(state))
            Rule()
            CoinRow(Coin.NOX, here?.nox ?: "–")
            Rule()
            CoinRow(Coin.USDC, here?.usdc ?: "–")
        }
        HomeActions(state, onPanel)
        TextButton(onClick = { model.readAccount() }, enabled = state.busy == Busy.Idle) {
            Text(if (state.busy == Busy.Reading) "Reading over Tor" else "Read again")
        }
    }
}

private fun note(state: UiState): String = when {
    state.busy == Busy.Reading -> "Reading over Tor"
    state.publicHere == null -> "Not read yet"
    else -> ""
}
