package systems.nonos.shield.ui.state

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Run a core call off the main thread and fold its outcome into the state. Every failure sets
 * [UiState.failure], so no screen claims a transfer was sent when it was not.
 */
internal fun WalletViewModel.launchCore(busy: Busy, block: () -> Unit) {
    set { it.copy(busy = busy, failure = null) }
    viewModelScope.launch(Dispatchers.Default) {
        runCatching(block).onFailure { cause ->
            set { it.copy(busy = Busy.Idle, failure = describe(cause)) }
        }
    }
}

/** Clear a failure the user has read. */
internal fun WalletViewModel.dismissFailure() = set { it.copy(failure = null) }
