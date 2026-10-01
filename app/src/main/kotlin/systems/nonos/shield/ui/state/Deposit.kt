package systems.nonos.shield.ui.state

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.SystemClip
import systems.nonos.shield.core.Wait
import systems.nonos.shield.core.copyThenClear

/**
 * A deposit from the user's own EVM wallet. The core refuses what the pool would refuse or trap,
 * and writes the note to disk before any transaction comes back.
 */
internal fun WalletViewModel.prepareDeposit(coin: Coin, amount: String, from: String) =
    launchCore(Busy.Depositing) {
        val open = wallet ?: return@launchCore
        val ticket = open.prepareDeposit(coin, amount.trim(), from.trim())
        set { it.copy(deposit = ticket, busy = Busy.Idle) }
    }

internal fun WalletViewModel.clearDeposit() = set { it.copy(deposit = null) }

/** Copy one field of a ticket, kept out of previews and cleared a minute later. */
internal fun WalletViewModel.copyValue(text: String) {
    val clip = SystemClip(context)
    viewModelScope.launch {
        copyThenClear(clip = clip, address = text, onCopied = {}, wait = Wait { delay(it) })
    }
}
