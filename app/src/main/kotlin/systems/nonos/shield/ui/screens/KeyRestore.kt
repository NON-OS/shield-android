package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.ui.parts.Ask
import systems.nonos.shield.ui.parts.Field
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.parts.Typing

/** Lines a pasted key needs to be read at a glance. */
private const val KEY_LINES = 2

/**
 * Restoring from a private key. It gives one account, whose public address is that key, and a
 * shield of its own. There are no words, so the key stays the backup.
 */
@Composable
internal fun KeyRestore(key: String, onKey: (String) -> Unit) {
    Statement("Paste the private key.")
    Lead("64 hex digits, 0x optional. The wallet gets one account, and the key stays its backup.")
    Field(Ask("Private key", Typing.exact, KEY_LINES), key, onKey)
}
