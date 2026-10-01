package systems.nonos.shield.ui.state

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import systems.nonos.shield.core.CancelToken
import systems.nonos.shield.core.KeySecurity
import systems.nonos.shield.core.ScreenLock
import systems.nonos.shield.core.Wallet
import systems.nonos.shield.core.WalletFactory

/**
 * The only place the shell holds state. Actions run off the main thread and land as a new
 * [UiState], and the core decides everything, so nothing here caches or computes money.
 */
class WalletViewModel(internal val context: Context) : ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    internal var wallet: Wallet? = null
    internal var cancel: CancelToken? = null

    /**
     * When somebody last touched the screen, on a clock that counts through sleep. It is kept out
     * of the state so a tap does not recompose the wallet.
     */
    internal var lastTouch: Long = 0

    init {
        viewModelScope.launch { open() }
        watchIdle()
        scanOnSchedule()
    }

    internal fun set(update: (UiState) -> UiState) {
        _state.value = update(_state.value)
    }

    internal fun current(): UiState = _state.value

    internal fun open() {
        val opened = WalletFactory.open(context)
        wallet = opened
        val stage = if (opened.stored()) Stage.Locked else Stage.NeedsWallet
        set {
            it.copy(
                stage = stage,
                busy = Busy.Idle,
                strongBox = KeySecurity.strongBoxBacked(),
                userAuth = KeySecurity.userAuthenticationRequired(),
                deviceSecure = ScreenLock.secure(context),
                idle = readIdle(),
                recordingAllowed = readRecording(),
                network = savedNetwork(),
            )
        }
    }

    companion object {
        /** The factory the activity uses, so the model can hold the app context. */
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    WalletViewModel(context) as T
            }
    }
}
