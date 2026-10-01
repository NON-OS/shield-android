package systems.nonos.shield.ui.state

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import systems.nonos.shield.core.Authenticator

/** How an account is named on screen: counted from 1, from the key at index 0. */
internal fun accountName(index: UInt): String = "Account ${index + 1u}"

/** The active account, named, for every screen that says whose money it shows. */
internal val UiState.activeName: String
    get() = accountName(accountList.firstOrNull { it.active }?.index ?: 0u)

/** Read the list of accounts and view-only accounts. Both are local, so nothing is asked of Tor. */
internal fun WalletViewModel.readAccounts() {
    val open = wallet ?: return
    set { it.copy(accountList = open.accounts(), watched = open.watchedAccounts()) }
}

/** Add the next account. The core opens the vault again, so the device confirms the owner. */
internal fun WalletViewModel.addAccount(auth: Authenticator) =
    launchGuarded(Busy.Unlocking, auth, "Add an account to this wallet") {
        wallet?.addAccount()
        switched()
    }

internal fun WalletViewModel.selectAccount(index: UInt) = launchCore(Busy.Reading) {
    wallet?.selectAccount(index)
    switched()
}

/** Forget what the last account showed, then read the new one. A review never outlives a switch. */
private fun WalletViewModel.switched() {
    set {
        it.copy(
            accounts = emptyMap(),
            review = null,
            sent = null,
            sentStatus = null,
            swap = null,
            deposit = null,
            spend = null,
            relay = null,
            relayState = null,
            shielding = null,
        )
    }
    refresh()
}

/** Add a view-only account from a pasted `noxivk1` or `noxfvk1` key. */
internal fun WalletViewModel.importViewKey(text: String) = launchCore(Busy.Reading) {
    wallet?.importViewKey(text)
    readAccounts()
    set { it.copy(busy = Busy.Idle) }
}

internal fun WalletViewModel.forgetViewKey(index: UInt) = launchCore(Busy.Reading) {
    wallet?.forgetViewKey(index)
    readAccounts()
    set { it.copy(busy = Busy.Idle) }
}

/** Whether `to` is an account of this wallet. Quick and local, so the form asks as it is typed. */
internal fun WalletViewModel.checkTarget(to: String) {
    val open = wallet ?: return
    val own = runCatching { open.ownAccountOf(to) }.getOrNull()
    val recent = own != null && runCatching { open.arrivedRecently() }.getOrDefault(false)
    set { it.copy(ownTarget = own, arrivedRecently = recent) }
}

/** Whether a typed withdrawal address was used before, read over Tor off the main thread. */
internal fun WalletViewModel.checkUsed(address: String) {
    val open = wallet ?: return
    if (address == current().freshAddress || address.length != ADDRESS_CHARS) {
        set { it.copy(usedAddress = false) }
        return
    }
    viewModelScope.launch(Dispatchers.Default) {
        val used = runCatching { open.addressUsed(address) }.getOrDefault(false)
        set { it.copy(usedAddress = used) }
    }
}

/** `0x` and 40 hex digits. */
private const val ADDRESS_CHARS = 42
