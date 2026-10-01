package systems.nonos.shield.ui.state

import android.os.SystemClock
import androidx.lifecycle.viewModelScope
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import systems.nonos.shield.core.Idle
import systems.nonos.shield.core.Locking

/** Lock a wallet left untouched, on a clock that sleep and time changes cannot hold back. */
internal fun WalletViewModel.watchIdle() {
    touched()
    viewModelScope.launch {
        while (true) {
            delay(CHECK)
            val limit = current().idle
            val state = current()
            if (state.stage == Stage.Ready && limit.expired(now(), lastTouch)) {
                lock()
            }
        }
    }
}

/** Record that somebody is here. Called from the root on any touch. */
internal fun WalletViewModel.touched() {
    lastTouch = now()
}

/** Move to the next choice and write it down, so it survives a restart. */
internal fun WalletViewModel.cycleIdle() {
    val next = current().idle.next()
    Locking.save(context, next)
    set { it.copy(idle = next) }
    touched()
}

/** The stored choice, read once when the wallet opens. */
internal fun WalletViewModel.readIdle(): Idle = Locking.load(context)

private fun now(): Long = SystemClock.elapsedRealtime() / MILLIS

private const val MILLIS = 1_000L

/** How often the check runs. It locks only a ready wallet, so work in flight finishes. */
private val CHECK = 5.seconds
