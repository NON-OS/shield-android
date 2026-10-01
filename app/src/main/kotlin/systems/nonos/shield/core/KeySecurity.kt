package systems.nonos.shield.core

import android.os.Build
import android.security.keystore.KeyInfo
import android.security.keystore.KeyProperties
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory

/** Where the wrapping key lives and what guards it, read back from the key itself. */
internal object KeySecurity {
    /** True when the existing wrapping key sits in a separate security chip. */
    fun strongBoxBacked(): Boolean {
        val info = info() ?: return false
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            info.securityLevel == KeyProperties.SECURITY_LEVEL_STRONGBOX
        } else {
            @Suppress("DEPRECATION")
            info.isInsideSecureHardware
        }
    }

    /** True when the keystore will not use the wrapping key until the user is confirmed. */
    fun userAuthenticationRequired(): Boolean = info()?.isUserAuthenticationRequired ?: false

    /** True when [key] sits in a TEE or StrongBox. A key the keystore keeps in software is not. */
    fun inHardware(key: SecretKey): Boolean {
        val info = info(key) ?: return false
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            info.securityLevel == KeyProperties.SECURITY_LEVEL_TRUSTED_ENVIRONMENT ||
                info.securityLevel == KeyProperties.SECURITY_LEVEL_STRONGBOX
        } else {
            @Suppress("DEPRECATION")
            info.isInsideSecureHardware
        }
    }

    private fun info(): KeyInfo? = StrongBoxGuard.key()?.let(::info)

    private fun info(key: SecretKey): KeyInfo? {
        return runCatching {
            SecretKeyFactory.getInstance(key.algorithm, "AndroidKeyStore")
                .getKeySpec(key, KeyInfo::class.java) as KeyInfo
        }.getOrNull()
    }
}
