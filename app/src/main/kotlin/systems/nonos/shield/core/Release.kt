package systems.nonos.shield.core

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/**
 * Which build this is, read from the package instead of written down. A constant goes stale, and
 * the installed version is what a person reporting a problem reads off the screen.
 */
internal object Release {
    fun version(context: Context): String = try {
        info(context).versionName ?: UNKNOWN
    } catch (_: PackageManager.NameNotFoundException) {
        UNKNOWN
    }

    /*
     * The flags overload arrived in API 33 and the old call is deprecated there, while this app
     * runs from 28. Both calls are kept so no warning has to be suppressed.
     */
    private fun info(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.PackageInfoFlags.of(0),
            )
        } else {
            legacy(context)
        }

    @Suppress("DEPRECATION")
    private fun legacy(context: Context) =
        context.packageManager.getPackageInfo(context.packageName, 0)

    private const val UNKNOWN = "unknown"
}
