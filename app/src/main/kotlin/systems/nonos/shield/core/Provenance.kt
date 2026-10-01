package systems.nonos.shield.core

import android.content.Context
import java.io.IOException

/**
 * Which core this build carries, read from a file the build wrote. It names the core commit, the
 * rustc and NDK versions a reproducer must match, and whether the tree was clean. Nothing is typed
 * by hand, and a missing file shows unknown.
 */
internal data class Provenance(
    val commit: String,
    val state: String,
    val rustc: String,
    val ndk: String,
) {
    /** One line for a fact row: the commit, and the state when it matters. */
    val line: String get() = if (state == "clean") commit else "$commit ($state)"

    companion object {
        private const val FILE = "core.txt"
        private val UNKNOWN = Provenance("unknown", "unknown", "unknown", "unknown")

        fun of(context: Context): Provenance = try {
            read(context.assets.open(FILE).bufferedReader().use { it.readText() })
        } catch (_: IOException) {
            UNKNOWN
        }

        /** Three words to a line, in the order the build writes them. */
        internal fun read(text: String): Provenance {
            val fields = text.lineSequence()
                .mapNotNull { line -> line.trim().split(' ', limit = 2).takeIf { it.size == 2 } }
                .associate { it[0] to it[1] }
            return Provenance(
                commit = fields["commit"] ?: UNKNOWN.commit,
                state = fields["state"] ?: UNKNOWN.state,
                rustc = fields["rustc"] ?: UNKNOWN.rustc,
                ndk = fields["ndk"] ?: UNKNOWN.ndk,
            )
        }
    }
}
