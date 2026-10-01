package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.core.PublicShield
import systems.nonos.shield.ui.parts.Fact
import systems.nonos.shield.ui.parts.Facts
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Secondary
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.LocalAuthenticator
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.activeName
import systems.nonos.shield.ui.state.clearShield
import systems.nonos.shield.ui.state.confirmShield

/**
 * A deposit from this account, worked out by the core: the approval the pool needs first, or the
 * deposit itself, with what the note will hold and the most the network can charge.
 */
@Composable
internal fun ShieldReviewView(
    state: UiState,
    review: PublicShield,
    unit: String,
    model: WalletViewModel,
) {
    val auth = LocalAuthenticator.current
    val idle = state.busy == Busy.Idle
    val refusal = review.refusal
    if (refusal != null) {
        Statement(refusal)
    } else {
        if (review.approval) {
            Statement("First, let the pool take exactly ${review.amount} $unit. Then the deposit.")
        }
        Facts {
            Fact("from", state.activeName)
            Fact("you shield", "${review.amount} $unit")
            Fact("pool fee", "${review.poolFee} $unit")
            Fact("the note holds", "${review.shielded} $unit")
            Fact("network fee, at most", "${review.maxNetworkFee} ETH")
        }
        val title = when {
            state.busy == Busy.Sending -> "Sending"
            review.approval -> "Confirm approval"
            else -> "Confirm and shield"
        }
        Primary(title, enabled = idle) { model.confirmShield(auth) }
    }
    Secondary("Change it", enabled = idle) { model.clearShield() }
}
