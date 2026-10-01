package systems.nonos.shield.ui.parts

/**
 * Breaking a long value into runs a person can actually read back. An unbroken address is a wall
 * nobody checks, and an unchecked value can be altered. Runs of eight, split by a space one mono
 * cell wide, stay in column when the line wraps.
 */
internal const val RUN = 8

internal fun groups(value: String, run: Int = RUN): String = value.chunked(run).joinToString(" ")

/**
 * A long value cut to its ends, for a place that has one line. Both ends stay, since a forged
 * prefix must also match the suffix, and the count says how much was left out.
 */
internal fun shortened(value: String, ends: Int = ENDS): String {
    if (value.length <= ends * 2 + MARK.length) return value
    return value.take(ends) + MARK + value.takeLast(ends)
}

/** How much of each end survives. */
private const val ENDS = 6

/** What stands in for the part that was cut. */
private const val MARK = "…"

/**
 * A hash or an address as the shared tokens write one: `0x`, the first six digits, an ellipsis, the
 * last four, as in 0x6def9b…f3a4.
 */
internal fun hash(value: String): String {
    if (!value.startsWith("0x") || value.length <= HASH_HEAD + HASH_TAIL) return value
    return value.take(HASH_HEAD) + "…" + value.takeLast(HASH_TAIL)
}

/** `0x` and six digits at the head, four at the tail. */
private const val HASH_HEAD = 8
private const val HASH_TAIL = 4
