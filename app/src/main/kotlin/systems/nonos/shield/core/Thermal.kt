package systems.nonos.shield.core

import android.content.Context
import android.os.Build
import android.os.PowerManager

/** How hot the phone says it is, in the words of `PowerManager`, before and after a run. */
internal object Thermal {
    fun status(context: Context): String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return "not reported"
        val power = context.getSystemService(PowerManager::class.java) ?: return "not reported"
        return word(power.currentThermalStatus)
    }

    /** The platform constants 0 to 6, by name. */
    internal fun word(status: Int): String = WORDS.getOrNull(status) ?: "unknown"

    private val WORDS =
        listOf("none", "light", "moderate", "severe", "critical", "emergency", "shutdown")
}
