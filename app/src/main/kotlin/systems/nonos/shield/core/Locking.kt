package systems.nonos.shield.core

import android.content.Context
import java.io.File

/**
 * Where the idle choice is kept: a file in the app's own directory, not a secret, kept through
 * cache eviction and unreadable by other apps. An unreadable file counts as no choice, so the
 * wallet takes the default and at worst locks sooner than expected.
 */
internal object Locking {
    private const val FILE = "lock.txt"

    fun load(context: Context): Idle {
        val file = File(context.filesDir, FILE)
        if (!file.exists()) return Idle.FiveMinutes
        return Idle.of(file.readText().trim().toLongOrNull())
    }

    fun save(context: Context, idle: Idle) {
        File(context.filesDir, FILE).writeText(idle.seconds.toString())
    }
}
