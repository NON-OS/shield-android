package systems.nonos.shield.core

import android.content.Intent

/**
 * Handing the measurement card to another app. What leaves is text, never a file reference, so
 * there is no provider or URI grant and no other file, such as the sealed vault, is reachable.
 * [body] returns a string, so nothing can be attached without changing the signature.
 */
internal object Card {
    /**
     * The card as it will leave the device: what the run wrote, minus trailing blank lines. Nothing
     * is added, since an appended line would be a line the measurement did not produce.
     */
    fun body(card: String): String = card.trimEnd()

    /** The chooser for it. None of its lines names a file. */
    fun share(card: String): Intent {
        val send = Intent(Intent.ACTION_SEND)
        send.type = "text/plain"
        send.putExtra(Intent.EXTRA_TEXT, body(card))
        return Intent.createChooser(send, null)
    }
}
