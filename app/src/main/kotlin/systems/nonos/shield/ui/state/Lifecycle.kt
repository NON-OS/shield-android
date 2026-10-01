package systems.nonos.shield.ui.state

import systems.nonos.shield.core.Authenticator

/**
 * Creating, restoring, unlocking and locking, each one core call. The three that reach the wrapping
 * key confirm the user first and name the action on the prompt.
 */
internal fun WalletViewModel.create(auth: Authenticator) =
    launchGuarded(Busy.Unlocking, auth, "Seal a new wallet on this device") {
        val words = wallet?.create() ?: return@launchGuarded
        set { it.copy(stage = Stage.ShowingPhrase, phrase = words, busy = Busy.Idle) }
    }

/** Move on from the phrase screen, dropping the words from the state. */
internal fun WalletViewModel.phraseConfirmed() = launchCore(Busy.Syncing) {
    set { it.copy(phrase = emptyList()) }
    refresh()
}

internal fun WalletViewModel.restore(auth: Authenticator, words: List<String>) =
    launchGuarded(Busy.Unlocking, auth, "Seal the restored wallet on this device") {
        wallet?.restore(words)
        set { it.copy(busy = Busy.Finding, searched = 0u) }
        val progress = watchSearch()
        val search = runCatching { wallet?.findAccounts() }
        progress.cancel()
        refresh()
        if (search.isFailure) {
            val told = "The other accounts could not be looked for. Add them in Accounts."
            set { it.copy(failure = told) }
        }
    }

/** Restore from a private key: one account, no words, and no other accounts to look for. */
internal fun WalletViewModel.restoreFromKey(auth: Authenticator, key: String) =
    launchGuarded(Busy.Unlocking, auth, "Seal the restored wallet on this device") {
        wallet?.restoreFromKey(key)
        refresh()
    }

internal fun WalletViewModel.unlock(auth: Authenticator) =
    launchGuarded(Busy.Unlocking, auth, "Unlock the wallet on this device") {
        wallet?.unlock()
        refresh()
    }

/** Drop the session, and with it every key derived from the seed. */
internal fun WalletViewModel.lock() = launchCore(Busy.Idle) {
    wallet?.lock()
    set {
        it.copy(
            stage = Stage.Locked,
            balances = emptyList(),
            account = "",
            accounts = emptyMap(),
            review = null,
            sent = null,
            sentStatus = null,
            swap = null,
            receiving = "",
            chain = null,
            deposit = null,
            accountList = emptyList(),
            watched = emptyList(),
            exported = null,
            shielding = null,
            busy = Busy.Idle,
        )
    }
}
