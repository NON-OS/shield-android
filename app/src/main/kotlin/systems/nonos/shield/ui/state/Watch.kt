package systems.nonos.shield.ui.state

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import systems.nonos.shield.core.Network
import systems.nonos.shield.core.PublicSent
import systems.nonos.shield.core.SendStatus

/** About one block. */
private const val EVERY_MS = 12_000L

/** Half an hour of blocks. */
private const val TIMES = 150

/**
 * Following a sent transaction until the network settles it, about once a block for up to half an
 * hour. A private relay can hide a transaction until it is mined, so pending may last a while.
 */
internal fun WalletViewModel.watch(sent: PublicSent, network: Network) {
    viewModelScope.launch(Dispatchers.Default) {
        repeat(TIMES) {
            delay(EVERY_MS)
            if (current().sent?.hash != sent.hash) return@launch
            val status = runCatching { wallet?.publicSendStatus(network, sent.hash) }.getOrNull()
            if (status != null && status != SendStatus.PENDING) {
                set { it.copy(sentStatus = status) }
                readAccount()
                return@launch
            }
        }
    }
}
