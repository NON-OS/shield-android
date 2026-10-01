package systems.nonos.shield.ui.state

import systems.nonos.shield.core.CancelToken
import systems.nonos.shield.core.WalletException

/** Read the pool's history over Tor, record this wallet's notes, then read the public account. */
internal fun WalletViewModel.sync() = launchCore(Busy.Syncing) {
    val open = wallet ?: return@launchCore
    val summary = open.syncChain()
    set { it.copy(chain = summary, balances = summary.balances, busy = Busy.Idle) }
    readAccount()
}

/** Prove a private transfer or a withdrawal on this device and seal it for a relayer. */
internal fun WalletViewModel.spend(order: SpendOrder) {
    val token = CancelToken()
    cancel = token
    set { it.copy(tooSoon = null) }
    launchCore(Busy.Proving) {
        val open = wallet ?: return@launchCore
        val ticket = try {
            if (order.withdraw) {
                open.withdraw(order.coin, order.to, order.amount, order.early, token)
            } else {
                open.sendPrivate(order.coin, order.to, order.amount, order.early, token)
            }
        } catch (ignored: WalletException.TooSoon) {
            // Not a failure: the screen asks whether to go early, and says what it costs.
            set { it.copy(tooSoon = order, busy = Busy.Idle) }
            return@launchCore
        }
        cancel = null
        set { it.copy(spend = ticket, balances = open.balances(), busy = Busy.Idle) }
    }
}

/** Forget a spend's ticket once it has been handed over. */
internal fun WalletViewModel.clearSpend() = set {
    it.copy(spend = null, relay = null, relayState = null)
}

/** Ask the running proof to stop at the core's next stage boundary. */
internal fun WalletViewModel.cancelProof() {
    cancel?.cancel()
    set { it.copy(busy = Busy.Cancelling) }
}
