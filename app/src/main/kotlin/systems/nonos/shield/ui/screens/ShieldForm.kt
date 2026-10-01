package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.amountUnit
import systems.nonos.shield.ui.parts.Ask
import systems.nonos.shield.ui.parts.CoinPicker
import systems.nonos.shield.ui.parts.Field
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Quiet
import systems.nonos.shield.ui.parts.Typing
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.activeName

/** The coin and the amount to shield from this account, and the way to another wallet instead. */
@Composable
internal fun ShieldForm(
    state: UiState,
    draft: DepositDraft,
    onExternal: () -> Unit,
    onReview: () -> Unit,
) {
    val (coin, amount) = draft.coin to draft.amount
    Lead(
        "From ${state.activeName}, signed here. The pool is checked first, and nothing is sent " +
            "if it would refuse.",
    )
    CoinPicker(coin.value) { coin.value = it }
    val unit = amountUnit(coin.value)
    val sizes = if (coin.value == Coin.ETH) ETH_SIZES else NOX_SIZES
    Field(Ask("Amount in $unit: $sizes", Typing.amount), amount.value) { amount.value = it }
    Primary(
        text = if (state.busy == Busy.Reviewing) "Reading the pool" else "Review",
        enabled = state.busy == Busy.Idle && amount.value.isNotEmpty(),
        onClick = onReview,
    )
    Quiet("Deposit from another wallet instead", onClick = onExternal)
}

private const val ETH_SIZES = "0.01, 0.02, 0.05 and so on"
private const val NOX_SIZES = "1,000, 2,000, 5,000 and so on"
