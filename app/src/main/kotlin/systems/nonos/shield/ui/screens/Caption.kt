package systems.nonos.shield.ui.screens

import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.amountUnit
import systems.nonos.shield.core.formatAmount
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.shield

/**
 * What the balance figure means, which is a different sentence in each of the four states a wallet
 * can be in. A zero may be a wallet that never looked, so the empty cases say whether to scan or
 * receive.
 */
internal fun caption(state: UiState): String {
    for (coin in listOf(Coin.NOX, Coin.ETH)) {
        val pending = state.shield(coin)?.pending ?: 0uL
        if (pending > 0uL) return "${formatAmount(coin, pending)} ${amountUnit(coin)} in flight"
    }
    return held(state)
}

/**
 * What a figure with nothing in flight means. At zero it is a wallet that never looked, one nobody
 * has sent to, or one whose notes are all spent, and each needs a different action.
 */
private fun held(state: UiState): String {
    if (state.balances.any { it.spendable > 0uL }) {
        return "Shielded, on this device"
    }
    if (state.chain == null) return "Not read from the pool yet"
    return when (state.balances.sumOf { it.noteCount.toULong() }) {
        0uL -> "Nothing received at this address yet"
        else -> "Every note found here has been spent"
    }
}
