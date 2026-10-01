package systems.nonos.shield.ui.state

/**
 * What happens when the wallet stops being on screen. The session holds keys derived from the seed
 * and the design assumes the phone may be seized unlocked, so leaving the foreground locks, even
 * with work in flight.
 */
internal fun WalletViewModel.leftForeground() {
    when (current().stage) {
        Stage.Ready, Stage.Measuring -> lock()
        Stage.ShowingPhrase -> discardUnwritten()
        Stage.Restoring -> show(Stage.NeedsWallet)
        else -> Unit
    }
}

/** The vault keeps the words, so a phrase left unconfirmed is shown again under Keys. */
private fun WalletViewModel.discardUnwritten() {
    set { it.copy(phrase = emptyList()) }
    lock()
}
