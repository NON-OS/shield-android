package systems.nonos.shield.core

import android.content.Context
import java.io.IOException

/**
 * Everything in the package and what it is licensed under, read from a file the build generated. It
 * is written from the resolved classpath and crate graph, so it cannot drift from what ships. The
 * notice texts travel with the binary, as MIT, BSD and Apache require.
 */
internal object Licences {
    private const val FILE = "licences.txt"

    private const val TEXTS = "licence-texts.txt"

    fun of(context: Context): List<String> = read(context, FILE).lines().filter { it.isNotBlank() }

    /** The notice texts, one block per distinct text, each headed by its crates. */
    fun texts(context: Context): List<String> =
        read(context, TEXTS).split("\n\u000C\n").map { it.trim() }.filter { it.isNotEmpty() }

    private fun read(context: Context, name: String): String = try {
        context.assets.open(name).bufferedReader().use { it.readText() }
    } catch (_: IOException) {
        ""
    }
}
