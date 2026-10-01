package systems.nonos.shield.core

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Copying an address and then taking it back. An address left on the clipboard links this wallet to
 * whatever reads clipboards next, so it is cleared after a wait. The wait is a parameter so a test
 * can watch the clear happen.
 */
internal suspend fun copyThenClear(
    clip: Clip,
    address: String,
    onCopied: (Boolean) -> Unit,
    wait: Wait,
) {
    if (address.isEmpty()) return
    clip.hold(address)
    onCopied(true)
    wait.sleep(wait.hold)
    clip.release()
    onCopied(false)
}

/**
 * How long to wait and how to wait. They travel together so a caller cannot set a wait and skip the
 * waiting.
 */
internal data class Wait(val hold: Duration = HOLD, val sleep: suspend (Duration) -> Unit)

/**
 * How long the address stays there. Long enough to switch app and paste, short enough to be gone
 * before the phone is handed over.
 */
internal val HOLD = 60.seconds
