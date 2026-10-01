package systems.nonos.shield.core

import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/** The keystore key wrapping the vault key, in StrongBox where present, never exportable. */
internal object StrongBoxGuard {
    private const val KEYSTORE = "AndroidKeyStore"
    private const val ALIAS = "nox-shield-vault-v1"

    /** Generated on first use. Null when the keystore refuses, shown as a guard failure. */
    fun key(): SecretKey? {
        val store = runCatching { KeyStore.getInstance(KEYSTORE).apply { load(null) } }
            .getOrNull() ?: return null
        (runCatching { store.getKey(ALIAS, null) }.getOrNull() as? SecretKey)?.let { return it }
        return generate(strongBox = true) ?: generate(strongBox = false)
    }

    /** Remove the wrapping key, which makes a wipe final. A refusal is reported, never hidden. */
    fun destroy(): Boolean = runCatching {
        KeyStore.getInstance(KEYSTORE).apply { load(null) }.deleteEntry(ALIAS)
        true
    }.getOrDefault(false)

    // A device with no security chip still has a TEE, so retry without StrongBox. A key
    // the keystore keeps in software is deleted and refused: it is only as safe as the app files.
    private fun generate(strongBox: Boolean): SecretKey? {
        val key = runCatching {
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE)
                .apply { init(VaultKeySpec.of(ALIAS, strongBox)) }
                .generateKey()
        }.getOrNull() ?: return null
        if (KeySecurity.inHardware(key)) return key
        destroy()
        return null
    }
}
