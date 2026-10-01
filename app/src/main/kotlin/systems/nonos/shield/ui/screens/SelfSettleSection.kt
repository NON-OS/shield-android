package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.ui.parts.Fact
import systems.nonos.shield.ui.parts.Facts
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Secondary
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.LocalAuthenticator
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.activeName
import systems.nonos.shield.ui.state.confirmSelfSettle
import systems.nonos.shield.ui.state.hasLander
import systems.nonos.shield.ui.state.reviewSelfSettle

/**
 * Landing the spend from this account, said plainly: it works without anyone else, and it names
 * the account in public next to this spend. The fee in the proof comes back to the account.
 */
@Composable
internal fun SelfSettleSection(state: UiState, model: WalletViewModel) {
    val auth = LocalAuthenticator.current
    val review = state.selfSettle
    val idle = state.busy == Busy.Idle
    if (review == null) {
        Lead(
            "Settling it yourself sends it from your public account, which links that account " +
                "to this spend in public.",
        )
        if (!model.hasLander() || state.notLanded) {
            Primary("Settle it yourself", enabled = idle) { model.reviewSelfSettle() }
        } else {
            Secondary("Settle it yourself", enabled = idle) { model.reviewSelfSettle() }
        }
        return
    }
    val refusal = review.refusal
    if (refusal != null) {
        Statement(refusal)
        return
    }
    Facts {
        Fact("from", state.activeName)
        Fact("network fee, at most", "${review.maxNetworkFee} ETH")
    }
    val title = if (state.busy == Busy.Sending) "Sending" else "Confirm and settle"
    Primary(title, enabled = idle) { model.confirmSelfSettle(auth) }
}
