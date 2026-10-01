package systems.nonos.shield.ui.state

/** Read the core's addresses and balances, then the public account on the shown network. */
internal fun WalletViewModel.refresh() {
    val open = wallet ?: return
    set {
        it.copy(
            stage = Stage.Ready,
            account = open.publicAddress(),
            balances = open.balances(),
            receiving = open.receivingAddress(),
            accountList = open.accounts(),
            watched = open.watchedAccounts(),
            fromKey = open.fromKey(),
            freshAddress = open.freshWithdrawalAddress(),
            busy = Busy.Idle,
        )
    }
    readAccount()
}
