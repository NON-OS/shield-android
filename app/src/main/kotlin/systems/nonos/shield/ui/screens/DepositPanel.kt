package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.Network
import systems.nonos.shield.core.amountUnit
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.RealMoney
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.clearShield
import systems.nonos.shield.ui.state.reviewShield

/**
 * Moving NOX or ETH into the shield on Sepolia, from this wallet's own account by default. The core
 * reads the pool over Tor and works out every transaction, and the owner confirms each one.
 */
@Composable
internal fun DepositPanel(state: UiState, model: WalletViewModel, onBack: () -> Unit) {
    val coin = remember { mutableStateOf(Coin.NOX) }
    val amount = remember { mutableStateOf("") }
    var external by remember { mutableStateOf(false) }
    val leave = {
        model.clearShield()
        onBack()
    }
    Screen(frame("03", "Deposit", state, model).over(Backdrop.Deposit), onBack = leave) {
        RealMoney(Network.SEPOLIA)
        val ticket = state.deposit
        val review = state.shielding
        val sent = state.sent
        val next = { model.reviewShield(coin.value, amount.value) }
        when {
            ticket != null -> DepositTicketView(ticket, coin.value, model, onBack)
            external -> ExternalDepositForm(state, model, coin.value, amount)
            sent != null && review != null ->
                ShieldSentView(state, sent, model, ShieldSteps(next, leave))
            review != null -> ShieldReviewView(state, review, amountUnit(coin.value), model)
            else -> ShieldForm(state, DepositDraft(coin, amount), { external = true }, next)
        }
    }
}
