package systems.nonos.shield.ui.state

import systems.nonos.shield.core.Coin

/**
 * What a send or withdraw screen collects. The relayer and its fee are the pool's, never typed.
 * `early` is set only after the owner typed the confirmation to spend notes that have not waited.
 */
data class SpendOrder(
    val coin: Coin,
    /** A wallet address for a transfer, an EVM address for a withdrawal. */
    val to: String,
    val amount: String,
    val withdraw: Boolean,
    val early: Boolean = false,
)
