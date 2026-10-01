package systems.nonos.shield.core

import android.os.Debug
import java.io.File

/** The memory of this process as the platform counts it, in KiB, read right after a proof. */
internal object ProcessMemory {
    /** Proportional set size now, the share of shared pages included, from the platform. */
    fun pssKib(): Long = Debug.MemoryInfo().also { Debug.getMemoryInfo(it) }.totalPss.toLong()

    /** The highest resident set since the process started, or null where the kernel hides it. */
    fun peakResidentKib(): Long? = runCatching {
        File("/proc/self/status").readLines()
            .firstOrNull { it.startsWith("VmHWM:") }
            ?.let(::kib)
    }.getOrNull()

    /** `VmHWM:    123456 kB` as 123456. */
    internal fun kib(line: String): Long? =
        line.substringAfter(':').trim().removeSuffix("kB").trim().toLongOrNull()
}
