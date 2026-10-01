package systems.nonos.shield.ui.state

import systems.nonos.shield.core.Authenticator
import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.Network
import systems.nonos.shield.core.SendStatus

/**
 * Shielding from this wallet's own account on Sepolia: the core reviews the approval or the
 * deposit, the owner confirms it, and the send is followed like any other.
 */
internal fun WalletViewModel.reviewShield(coin: Coin, amount: String) = launchCore(Busy.Reviewing) {
    set { it.copy(sent = null, sentStatus = null) }
    val review = wallet?.reviewShield(coin, amount.trim()) ?: return@launchCore
    set { it.copy(shielding = review, busy = Busy.Idle) }
}

internal fun WalletViewModel.confirmShield(auth: Authenticator) {
    val review = current().shielding ?: return
    if (review.refusal != null) return
    val reason = if (review.approval) {
        "Let the pool take ${review.amount} on Sepolia"
    } else {
        "Shield ${review.amount} on Sepolia"
    }
    launchGuarded(Busy.Sending, auth, reason) {
        val sent = wallet?.confirmPublicSend(review.id) ?: return@launchGuarded
        set { it.copy(sent = sent, sentStatus = SendStatus.PENDING, busy = Busy.Idle) }
        watch(sent, Network.SEPOLIA)
    }
}

internal fun WalletViewModel.clearShield() =
    set { it.copy(shielding = null, sent = null, sentStatus = null) }
