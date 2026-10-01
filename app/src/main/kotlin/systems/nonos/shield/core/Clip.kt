package systems.nonos.shield.core

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.PersistableBundle

/** What this app does to a clipboard: it writes and clears, and never reads. */
internal interface Clip {
    fun hold(address: String)
    fun release()
}

/** The device clipboard. Clips are marked sensitive with an empty label, so previews hide them. */
internal class SystemClip(context: Context) : Clip {
    private val clipboard = context.getSystemService(ClipboardManager::class.java)

    override fun hold(address: String) {
        clipboard?.setPrimaryClip(sensitive(ClipData.newPlainText("", address)))
    }

    /** Unconditional: from Android 10 the app cannot read back, and a stale address is worse. */
    override fun release() {
        clipboard?.clearPrimaryClip()
    }

    /** Set by its string name below API 33, where keyboards and previews still honour it. */
    private fun sensitive(clip: ClipData): ClipData {
        val key = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ClipDescription.EXTRA_IS_SENSITIVE
        } else {
            SENSITIVE
        }
        clip.description.extras = PersistableBundle().apply { putBoolean(key, true) }
        return clip
    }

    private companion object {
        const val SENSITIVE = "android.content.extra.IS_SENSITIVE"
    }
}
