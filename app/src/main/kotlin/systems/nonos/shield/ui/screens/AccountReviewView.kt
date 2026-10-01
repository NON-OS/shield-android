package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.PublicReview
import systems.nonos.shield.core.amountUnit
import systems.nonos.shield.ui.parts.Fact
import systems.nonos.shield.ui.parts.Facts
import systems.nonos.shield.ui.parts.Label
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Secondary
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.parts.Value
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.LocalAuthenticator
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.activeName
import systems.nonos.shield.ui.state.clearSend
import systems.nonos.shield.ui.state.confirmSend

/**
 * The send as it will be signed, or why it will not be. Every figure is the core's. The recipient
 * is shown whole, in runs, because a look-alike forges the ends. The core signs only after the
 * owner confirms on the device.
 */
@Composable
internal fun AccountReviewView(state: UiState, review: PublicReview, model: WalletViewModel) {
    val auth = LocalAuthenticator.current
    val idle = state.busy == Busy.Idle
    val refusal = review.refusal
    if (refusal != null) {
        Statement(refusal)
    } else {
        val unit = amountUnit(review.coin)
        Label("To")
        Value(review.to)
        Facts {
            Fact("from", state.activeName)
            Fact("you send", "${review.amount} $unit")
            Fact("arrives", "${review.arrives} $unit")
            if (review.coin == Coin.NOX) {
                Fact("token fee", "${review.tokenFee} NOX, ${review.tokenFeePercent}%")
            }
            Fact("network fee, at most", "${review.maxNetworkFee} ETH")
            Fact("gas", "${review.gasLimit} at ${review.maxFeePerGasGwei} gwei")
            Fact("nonce, chain", "${review.nonce}, ${review.chainId}")
        }
        if (review.toIsContract) {
            Lead(
                "The recipient is a contract. Make sure it accepts $unit, or what is sent " +
                    "may be stuck there.",
            )
        }
        val sending = state.busy == Busy.Sending
        Primary(if (sending) "Sending" else "Confirm and send", enabled = idle) {
            model.confirmSend(auth)
        }
    }
    Secondary("Change it", enabled = idle) { model.clearSend() }
}
