package systems.nonos.shield.ui.state

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import systems.nonos.shield.core.SystemClip
import systems.nonos.shield.core.Wait
import systems.nonos.shield.core.copyThenClear

/**
 * Copying the receive address, marked sensitive and cleared a minute later. The policy and the wait
 * live in core, where a test asserts them.
 */
internal fun WalletViewModel.copyAddress() {
    val address = current().receiving
    val clip = SystemClip(context)
    viewModelScope.launch {
        copyThenClear(
            clip = clip,
            address = address,
            onCopied = { held -> set { it.copy(copied = held) } },
            wait = Wait { waited -> delay(waited) },
        )
    }
}

/** Copying an exported key, under the same policy: marked sensitive and cleared a minute later. */
internal fun WalletViewModel.copyExported() {
    val text = current().exported?.takeUnless { it.words }?.text ?: return
    val clip = SystemClip(context)
    viewModelScope.launch {
        copyThenClear(
            clip = clip,
            address = text,
            onCopied = { held -> set { it.copy(copied = held) } },
            wait = Wait { waited -> delay(waited) },
        )
    }
}
