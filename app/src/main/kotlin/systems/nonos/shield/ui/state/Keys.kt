package systems.nonos.shield.ui.state

import systems.nonos.shield.core.Authenticator
import systems.nonos.shield.core.ViewKind

/** The recovery words, after the owner is confirmed. A vault that never held them says so. */
internal fun WalletViewModel.showWords(auth: Authenticator) =
    launchGuarded(Busy.Unlocking, auth, "Show the recovery words") {
        val words = wallet?.recoveryWords()
        show(Exported("Recovery words", words?.joinToString(" ") ?: "", words = true))
    }

/** The private key of the active account's public address, to use in another wallet. */
internal fun WalletViewModel.exportPublicKey(auth: Authenticator) =
    launchGuarded(Busy.Unlocking, auth, "Export the account key") {
        val index = activeIndex()
        val key = wallet?.exportPublicKey(index) ?: return@launchGuarded
        show(Exported("${accountName(index)} key", key, words = false))
    }

internal fun WalletViewModel.exportViewKey(kind: ViewKind, auth: Authenticator) =
    launchGuarded(Busy.Unlocking, auth, "Export a view key") {
        val index = activeIndex()
        val key = wallet?.exportViewKey(index, kind) ?: return@launchGuarded
        val title = if (kind == ViewKind.FULL) "Full view key" else "Incoming view key"
        show(Exported("$title, ${accountName(index)}", key, words = false))
    }

/** Drop the exported text from the state when its screen closes. */
internal fun WalletViewModel.clearExported() = set { it.copy(exported = null) }

private fun WalletViewModel.show(shown: Exported) =
    set { it.copy(busy = Busy.Idle, exported = shown) }

private fun WalletViewModel.activeIndex(): UInt =
    current().accountList.firstOrNull { it.active }?.index ?: 0u
