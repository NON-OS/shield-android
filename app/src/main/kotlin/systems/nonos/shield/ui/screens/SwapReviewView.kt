package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.core.PublicSwap
import systems.nonos.shield.core.amountUnit
import systems.nonos.shield.ui.parts.CoinRow
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Rule
import systems.nonos.shield.ui.parts.Secondary
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.parts.Tile
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.LocalAuthenticator
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.activeName
import systems.nonos.shield.ui.state.clearSwap
import systems.nonos.shield.ui.state.confirmSwap

/**
 * The swap as it will be signed, or why it will not be: what is paid and what comes back, then
 * every term of the trade in one tile. Every figure is the core's.
 */
@Composable
internal fun SwapReviewView(state: UiState, swap: PublicSwap, model: WalletViewModel) {
    val auth = LocalAuthenticator.current
    val idle = state.busy == Busy.Idle
    val refusal = swap.refusal
    if (refusal != null) {
        Statement(refusal)
    } else {
        if (swap.approval) {
            Statement(
                "First, let Uniswap take exactly ${swap.amountIn} ${amountUnit(swap.from)}. " +
                    "Then review the swap again.",
            )
        }
        Tile {
            CoinRow(swap.from, swap.amountIn, "${state.activeName} pays")
            Rule()
            CoinRow(swap.to, "≈ " + swap.expectedOut, "You receive")
        }
        SwapTerms(swap)
        val confirm = if (swap.approval) "Confirm approval" else "Confirm swap"
        Primary(if (state.busy == Busy.Sending) "Sending" else confirm, enabled = idle) {
            model.confirmSwap(auth)
        }
    }
    Secondary("Change it", enabled = idle) { model.clearSwap() }
}
