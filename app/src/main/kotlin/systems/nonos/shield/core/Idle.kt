package systems.nonos.shield.core

/**
 * How long a wallet may sit unlocked with nobody touching it. Leaving the foreground already locks,
 * and this covers a phone left unlocked on a table with the wallet on screen. The choices are few
 * so nobody has to weigh a number of minutes.
 */
enum class Idle(val seconds: Long, val label: String) {
    Immediately(NONE, "immediately"),
    OneMinute(MINUTE, "after a minute"),
    FiveMinutes(FIVE_MINUTES, "after five minutes"),
    Never(FOREVER, "never"),
    ;

    /** The next choice, so a row can be tapped through them in one place. */
    fun next(): Idle = entries[(ordinal + 1) % entries.size]

    /**
     * Whether a wallet last touched at [touched] should be locked at [now], with both in seconds on
     * any clock that only goes forwards. A negative gap from a clock that jumped back never locks.
     */
    fun expired(now: Long, touched: Long): Boolean {
        if (this == Never) return false
        val gap = now - touched
        if (gap < 0) return false
        return gap >= seconds
    }

    companion object {
        /** Read a stored choice, falling back instead of failing. */
        fun of(seconds: Long?): Idle = entries.firstOrNull { it.seconds == seconds } ?: FiveMinutes
    }
}

/** No wait at all: the next check locks. */
private const val NONE = 0L

/** Seconds in a minute, so the choices read as minutes. */
private const val MINUTE = 60L

/** The default, long enough to read an address and short enough to matter. */
private const val FIVE_MINUTES = 300L

/** Not a duration. Stored as a number no duration can be. */
private const val FOREVER = -1L
