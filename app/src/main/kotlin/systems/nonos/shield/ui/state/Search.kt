package systems.nonos.shield.ui.state

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/** How often the restore screen reads the count of accounts the search has looked at. */
private const val POLL_MS = 1_000L

/** Read the search progress each second until the returned job is cancelled. */
internal fun WalletViewModel.watchSearch(): Job = viewModelScope.launch {
    while (isActive) {
        delay(POLL_MS)
        val seen = wallet?.accountSearchProgress() ?: break
        set { it.copy(searched = seen) }
    }
}
