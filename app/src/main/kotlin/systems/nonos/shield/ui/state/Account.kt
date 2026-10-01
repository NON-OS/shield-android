package systems.nonos.shield.ui.state

import systems.nonos.shield.core.Authenticator
import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.SendStatus
import systems.nonos.shield.core.amountUnit

/**
 * The public `0x` account: read, worked out in full, shown, confirmed by the owner, then signed by
 * the core, which holds the transaction the review shows.
 */
internal fun WalletViewModel.readAccount() {
    val now = current()
    if (now.stage != Stage.Ready || now.busy != Busy.Idle) return
    launchCore(Busy.Reading) {
        val open = wallet ?: return@launchCore
        val read = open.publicBalances(now.network)
        set { it.copy(accounts = it.accounts + (read.network to read), busy = Busy.Idle) }
    }
}

internal fun WalletViewModel.reviewSend(coin: Coin, to: String, amount: String) =
    launchCore(Busy.Reviewing) {
        val open = wallet ?: return@launchCore
        val review = open.reviewPublicSend(current().network, coin, to.trim(), amount.trim())
        set { it.copy(review = review, busy = Busy.Idle) }
    }

/** Ask the owner, then have the core sign and send what the review showed. */
internal fun WalletViewModel.confirmSend(auth: Authenticator) {
    val review = current().review ?: return
    if (review.refusal != null) return
    val unit = amountUnit(review.coin)
    val reason = if (review.realMoney) {
        "Send ${review.amount} $unit of real money"
    } else {
        "Send ${review.amount} $unit on Sepolia"
    }
    launchGuarded(Busy.Sending, auth, reason) {
        val open = wallet ?: return@launchGuarded
        val sent = open.confirmPublicSend(review.id)
        set { it.copy(sent = sent, sentStatus = SendStatus.PENDING, busy = Busy.Idle) }
        watch(sent, review.network)
    }
}

/** Forget the review and what was sent, once the screen is done with them. */
internal fun WalletViewModel.clearSend() =
    set { it.copy(review = null, sent = null, sentStatus = null) }
