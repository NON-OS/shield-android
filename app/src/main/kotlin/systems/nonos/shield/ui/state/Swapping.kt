package systems.nonos.shield.ui.state

import systems.nonos.shield.core.Authenticator
import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.SendStatus
import systems.nonos.shield.core.amountUnit

/**
 * A swap from the public account: the core's review, the owner's yes, and the same signing call a
 * send uses, so both pass the same checks. An approval lets the router take the swap amount and no
 * more, then the swap is reviewed again.
 */
internal fun WalletViewModel.reviewSwap(from: Coin, to: Coin, amount: String, slippageBps: UShort) =
    launchCore(Busy.Reviewing) {
        val open = wallet ?: return@launchCore
        val swap = open.reviewPublicSwap(current().network, from, to, amount.trim(), slippageBps)
        set { it.copy(swap = swap, busy = Busy.Idle) }
    }

internal fun WalletViewModel.confirmSwap(auth: Authenticator) {
    val swap = current().swap ?: return
    if (swap.refusal != null) return
    val unit = amountUnit(swap.from)
    val what = if (swap.approval) {
        "Let Uniswap take ${swap.amountIn} $unit"
    } else {
        "Swap ${swap.amountIn} $unit"
    }
    launchGuarded(Busy.Sending, auth, if (swap.realMoney) "$what, real money" else what) {
        val open = wallet ?: return@launchGuarded
        val sent = open.confirmPublicSend(swap.id)
        set { it.copy(sent = sent, sentStatus = SendStatus.PENDING, busy = Busy.Idle) }
        watch(sent, swap.network)
    }
}

internal fun WalletViewModel.clearSwap() = set {
    it.copy(swap = null, sent = null, sentStatus = null)
}
