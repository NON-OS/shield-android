package systems.nonos.shield.core

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import java.util.Locale

/**
 * What a measurement ran on, in one line: model, release, architecture, screen and memory, so a
 * reader can tell a 4 GB phone from an emulator. No serial, advertising id or install id.
 */
internal object Machine {
    fun describe(context: Context): String {
        val abi = Build.SUPPORTED_ABIS.firstOrNull() ?: "unknown"
        val screen = context.resources.displayMetrics
        return "${Build.MODEL}, Android ${Build.VERSION.RELEASE}, $abi, " +
            "${screen.widthPixels}x${screen.heightPixels}, ${memory(context)} RAM"
    }

    private fun memory(context: Context): String {
        val manager = context.getSystemService(ActivityManager::class.java) ?: return "unknown"
        val info = ActivityManager.MemoryInfo().also { manager.getMemoryInfo(it) }
        return String.format(Locale.ROOT, "%.1f GB", info.totalMem / BYTES_PER_GB)
    }

    private const val BYTES_PER_GB = 1_000_000_000.0
}
