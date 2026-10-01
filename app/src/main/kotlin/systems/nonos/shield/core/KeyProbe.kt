package systems.nonos.shield.core

import android.security.keystore.KeyPermanentlyInvalidatedException
import javax.crypto.Cipher

/**
 * Why the wrapping key would not open the vault. Removing the screen lock destroys the key and the
 * vault for good, which needs a different sentence than a keystore that refused once. It runs only
 * after a failure and encrypts one empty block, so it learns nothing about the seed.
 */
internal object KeyProbe {
    /** Set when the key failed the GCM tag of the vault: a key made after it, as on a reinstall. */
    @Volatile
    internal var mismatched = false

    /** True when the key is gone for good and only the recovery phrase is left. */
    fun invalidated(): Boolean = mismatched || permanentlyInvalidated()

    private fun permanentlyInvalidated(): Boolean {
        val secret = StrongBoxGuard.key() ?: return false
        return try {
            Cipher.getInstance("AES/GCM/NoPadding").init(Cipher.ENCRYPT_MODE, secret)
            false
        } catch (ignored: KeyPermanentlyInvalidatedException) {
            /*
             * The type is the whole answer, and nothing inside it is shown or kept: a keystore
             * message can name the alias, which belongs in no screen and no report.
             */
            true
        }
    }
}
