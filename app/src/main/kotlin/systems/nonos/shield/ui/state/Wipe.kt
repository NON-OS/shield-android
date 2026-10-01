package systems.nonos.shield.ui.state

import systems.nonos.shield.core.StrongBoxGuard

/**
 * Destroying this wallet on this device. The core overwrites the sealed seed and note log, then
 * this side removes the keystore key, even if the files were gone. It cannot be undone, and the
 * recovery phrase is the only way back.
 */
internal fun WalletViewModel.wipe(reason: String? = null) = launchCore(Busy.Unlocking) {
    wallet?.wipe()
    StrongBoxGuard.destroy()
    set {
        UiState(
            stage = Stage.NeedsWallet,
            deviceSecure = it.deviceSecure,
            strongBox = false,
            userAuth = false,
            failure = reason,
        )
    }
}
