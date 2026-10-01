package systems.nonos.shield.ui.state

/** One profiled proof: its phases in order, in milliseconds, and what the phone said around it. */
internal data class ProfileRun(
    val phases: List<Pair<String, Long>>,
    val totalMs: Long,
    val matched: Boolean,
    val pssKib: Long,
    val peakKib: Long?,
    val thermalBefore: String,
    val thermalAfter: String,
)

/**
 * The runs as a table in text, for the share sheet: a header naming the phone and the threads, a
 * row per run, the first marked cold. Every run is a row, a hot one included.
 */
internal fun profileText(machine: String, threads: String, runs: List<ProfileRun>): String {
    val names = runs.firstOrNull()?.phases?.map { it.first }.orEmpty()
    val head = listOf("run") + names + listOf("total", "match", "PSS MiB", "peak MiB", "thermal")
    val rows = runs.mapIndexed { i, run -> row(if (i == 0) "1 cold" else "${i + 1}", run) }
    return buildString {
        appendLine("NØNOS profile, $machine, $threads")
        appendLine()
        appendLine(head.joinToString(" | ", "| ", " |"))
        appendLine(head.joinToString("|", "|", "|") { "---" })
        rows.forEach { appendLine(it.joinToString(" | ", "| ", " |")) }
    }
}

private fun row(label: String, run: ProfileRun): List<String> =
    listOf(label) + run.phases.map { it.second.toString() } + listOf(
        run.totalMs.toString(),
        if (run.matched) "yes" else "NO",
        mib(run.pssKib),
        run.peakKib?.let(::mib) ?: "not reported",
        "${run.thermalBefore} to ${run.thermalAfter}",
    )

private fun mib(kib: Long): String = (kib / KIB_PER_MIB).toString()

private const val KIB_PER_MIB = 1024L
