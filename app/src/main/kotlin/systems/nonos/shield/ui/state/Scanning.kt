package systems.nonos.shield.ui.state

import androidx.lifecycle.viewModelScope
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import systems.nonos.shield.core.Schedule
import systems.nonos.shield.core.networkHasShield

/**
 * Scanning on a schedule instead of only when somebody taps, so a scan's timing says the app is
 * open, not that a person picked up the phone. It runs only while unlocked and idle, and the idle
 * lock still ends it.
 */
internal fun WalletViewModel.scanOnSchedule() {
    viewModelScope.launch {
        while (true) {
            delay(Schedule.next({ Random.nextDouble() }))
            val state = current()
            if (state.stage == Stage.Ready && state.busy == Busy.Idle) {
                if (networkHasShield(state.network)) sync() else readAccount()
            }
        }
    }
}
