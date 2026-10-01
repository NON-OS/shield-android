package systems.nonos.shield.ui.state

import systems.nonos.shield.core.Authenticator
import systems.nonos.shield.core.Network
import systems.nonos.shield.core.SendStatus

/**
 * Landing a proved spend from this wallet's own public account, when no lander is on or none
 * has landed it. The core reviews the settlement, the owner confirms it, and it is followed like
 * a send.
 */
internal fun WalletViewModel.reviewSelfSettle() = launchCore(Busy.Reviewing) {
    set { it.copy(sent = null, sentStatus = null) }
    val review = wallet?.reviewSelfSettle() ?: return@launchCore
    set { it.copy(selfSettle = review, busy = Busy.Idle) }
}

internal fun WalletViewModel.confirmSelfSettle(auth: Authenticator) {
    val review = current().selfSettle ?: return
    if (review.refusal != null) return
    launchGuarded(Busy.Sending, auth, "Settle this spend from your public account") {
        val sent = wallet?.confirmPublicSend(review.id) ?: return@launchGuarded
        set { it.copy(sent = sent, sentStatus = SendStatus.PENDING, busy = Busy.Idle) }
        watch(sent, Network.SEPOLIA)
    }
}

/** Whether this build hands proofs to a lander. With none, the owner lands every spend. */
internal fun WalletViewModel.hasLander(): Boolean = wallet?.hasLander() ?: false
