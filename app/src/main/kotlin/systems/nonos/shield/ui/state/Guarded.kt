package systems.nonos.shield.ui.state

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import systems.nonos.shield.core.AuthFailure
import systems.nonos.shield.core.Authenticator
import systems.nonos.shield.core.KeyProbe

/**
 * Run a core call that needs the wrapping key, confirming the user first. The keystore does the
 * check, and this prompt lets it pass. A dismissed prompt returns to idle with no failure. A
 * destroyed key is told apart from a refusal, since only the phrase recovers it.
 */
internal fun WalletViewModel.launchGuarded(
    busy: Busy,
    auth: Authenticator,
    reason: String,
    block: () -> Unit,
) {
    set { it.copy(busy = busy, failure = null) }
    viewModelScope.launch(Dispatchers.Default) {
        runCatching {
            auth.confirm(reason)
            block()
        }.onFailure { cause -> set { failed(it, cause) } }
    }
}

/** The state after a guarded call failed. A locked wallet whose key is gone moves to KeyGone. */
private fun failed(state: UiState, cause: Throwable): UiState {
    if (cause is AuthFailure.Cancelled) return state.copy(busy = Busy.Idle, failure = null)
    if (!KeyProbe.invalidated()) return state.copy(busy = Busy.Idle, failure = describe(cause))
    return state.copy(
        busy = Busy.Idle,
        failure = "This device no longer has the key that sealed this wallet. " +
            "Restore from the recovery words.",
        stage = if (state.stage == Stage.Locked) Stage.KeyGone else state.stage,
    )
}
