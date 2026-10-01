package systems.nonos.shield.ui.state

/**
 * Moving between screens the user drives, instead of the core. Each move clears the failure, which
 * belongs to the screen that produced it.
 */
internal fun WalletViewModel.show(stage: Stage) = set {
    it.copy(stage = stage, failure = null)
}
