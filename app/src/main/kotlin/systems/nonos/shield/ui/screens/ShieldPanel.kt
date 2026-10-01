package systems.nonos.shield.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.Network
import systems.nonos.shield.core.formatAmount
import systems.nonos.shield.core.networkHasShield
import systems.nonos.shield.ui.parts.Act
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.CoinRow
import systems.nonos.shield.ui.parts.Glyph
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.RoundAction
import systems.nonos.shield.ui.parts.Rule
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.parts.Tile
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.choose
import systems.nonos.shield.ui.state.shield

/**
 * The shield: private balances, on Sepolia only, and what can be done with
 * them. On mainnet it says where the shield runs and offers to switch.
 */
@Composable
internal fun ShieldPanel(
    state: UiState,
    model: WalletViewModel,
    onPanel: (Panel) -> Unit,
    onBack: () -> Unit,
) {
    Screen(frame("03", "Shield", state, model).over(Backdrop.Deposit), onBack = onBack) {
        if (!networkHasShield(state.network)) {
            Statement("The shield runs on Sepolia until its audit.")
            Primary("Switch to Sepolia") { model.choose(Network.SEPOLIA) }
            return@Screen
        }
        val figure = { coin: Coin -> formatAmount(coin, state.shield(coin)?.spendable ?: 0uL) }
        Tile {
            CoinRow(Coin.NOX, figure(Coin.NOX), caption(state))
            Rule()
            CoinRow(Coin.ETH, figure(Coin.ETH))
        }
        PrivacyMeter(state)
        Row(modifier = Modifier.fillMaxWidth()) {
            val acts = listOf(
                Act("Deposit", Glyph.In) to Panel.Deposit,
                Act("Receive", Glyph.Down) to Panel.Receive,
                Act("Send", Glyph.Up) to Panel.Send,
                Act("Withdraw", Glyph.Out) to Panel.Withdraw,
            )
            for ((act, panel) in acts) {
                RoundAction(act, modifier = Modifier.weight(1f)) { onPanel(panel) }
            }
        }
        ShieldMore(state, model)
    }
}
