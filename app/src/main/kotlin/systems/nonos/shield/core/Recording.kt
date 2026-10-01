package systems.nonos.shield.core

import android.content.Context
import java.io.File

/**
 * Whether the owner allows the screen to be recorded, kept in a file like the idle choice.
 * Off until chosen, and the words and exported keys stay hidden from a recording either way.
 */
internal object Recording {
    private const val FILE = "recording.txt"
    private const val ALLOWED = "allowed"

    fun load(context: Context): Boolean {
        val file = File(context.filesDir, FILE)
        return file.exists() && file.readText().trim() == ALLOWED
    }

    fun save(context: Context, allowed: Boolean) {
        File(context.filesDir, FILE).writeText(if (allowed) ALLOWED else "hidden")
    }
}
