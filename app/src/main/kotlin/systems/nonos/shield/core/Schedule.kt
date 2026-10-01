package systems.nonos.shield.core

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * When the next scan happens, if nobody asks for one. On a schedule a scan says the app is open,
 * not that a person picked up the phone. The interval is jittered so the gaps are no signature, and
 * the jitter protects no secret.
 */
internal object Schedule {
    /** How often a scan runs while the wallet is open and unlocked. */
    val PERIOD = 300.seconds

    /** How far either side of it a scan may land. */
    val JITTER = 60.seconds

    /**
     * The wait before the next scan. [draw] returns a fraction in zero to one, which this maps onto
     * the window around the period. It never returns a zero or negative wait, so a scan loop cannot
     * spin.
     */
    fun next(draw: () -> Double, period: Duration = PERIOD, jitter: Duration = JITTER): Duration {
        val fraction = draw().coerceIn(0.0, 1.0)
        val offset = jitter * (fraction * 2 - 1)
        val wait = period + offset
        return if (wait > FLOOR) wait else FLOOR
    }

    /** The shortest wait this will ever hand back. */
    private val FLOOR = 1.seconds
}
