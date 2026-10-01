package systems.nonos.shield.ui.state

/**
 * Taking back notes held as in flight whose spend was never settled. The core returns only what the
 * chain shows unspent, and the pool refuses a late hand-off as a second spend, so nothing pays out
 * twice.
 */
internal fun WalletViewModel.takeBack() = launchCore(Busy.Syncing) {
    val open = wallet ?: return@launchCore
    open.takeBackPending()
    set { it.copy(balances = open.balances(), busy = Busy.Idle) }
}
