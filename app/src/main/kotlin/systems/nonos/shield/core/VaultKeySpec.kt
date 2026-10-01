package systems.nonos.shield.core

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties

/** The policy protecting the seed: an unlocked device and a user the keystore itself confirms. */
internal object VaultKeySpec {
    /** A confirmation covers only the next call, a few seconds, not a session. */
    private const val WINDOW_SECONDS = 30

    private const val KEY_BITS = 256

    fun of(alias: String, strongBox: Boolean): KeyGenParameterSpec {
        val builder = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(KEY_BITS)
            .setUnlockedDeviceRequired(true)
            .setUserAuthenticationRequired(true)
            // Enrolling a finger keeps the key. Whoever can enrol one already knows the credential,
            // so invalidating would protect nothing and lose the vault.
            .setInvalidatedByBiometricEnrollment(false)
            .setIsStrongBoxBacked(strongBox)
        return window(builder).build()
    }

    // Before API 30 a validity window covers any credential or biometric, the same pair.
    private fun window(builder: KeyGenParameterSpec.Builder): KeyGenParameterSpec.Builder =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            builder.setUserAuthenticationParameters(
                WINDOW_SECONDS,
                KeyProperties.AUTH_BIOMETRIC_STRONG or KeyProperties.AUTH_DEVICE_CREDENTIAL,
            )
        } else {
            @Suppress("DEPRECATION")
            builder.setUserAuthenticationValidityDurationSeconds(WINDOW_SECONDS)
        }
}
