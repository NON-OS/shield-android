package systems.nonos.shield.ui.state

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** About ten seconds between asks, for up to half an hour. */
private const val EVERY_MS = 10_000L
private const val TIMES = 180

/**
 * Handing a proved spend to the lander over its onion service, then following it until it is
 * settled or refused. The core checks that the lander takes a fee paid to whoever submits. After
 * half an hour with no landing, the owner is offered to settle it from the public account.
 */
internal fun WalletViewModel.handToRelayer() = launchCore(Busy.Handing) {
    val open = wallet ?: return@launchCore
    val handed = open.handToRelayer()
    set { it.copy(relay = handed, busy = Busy.Idle) }
    handed.id?.let { follow(it) }
}

private fun WalletViewModel.follow(id: String) {
    viewModelScope.launch(Dispatchers.Default) {
        repeat(TIMES) {
            delay(EVERY_MS)
            if (current().relay?.id != id) return@launch
            val state = runCatching { wallet?.relayerState(id) }.getOrNull() ?: return@repeat
            set { it.copy(relayState = state) }
            if (state.status == "settled" || state.status == "refused") {
                sync()
                return@launch
            }
        }
        set { it.copy(notLanded = true) }
    }
}
