package systems.nonos.shield.ui.state

import java.io.File

/**
 * Reading back the card the last measured run wrote. The text comes from that file, so what leaves
 * is what is on disk, and an unreadable card is nothing to share.
 */
internal fun WalletViewModel.card(): String? {
    val path = current().cardPath ?: return null
    return runCatching { File(path).readText() }.getOrNull()?.takeIf { it.isNotBlank() }
}
