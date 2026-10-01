package systems.nonos.shield.core

import android.os.Build
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt

/**
 * What the system prompt says and which authenticators it accepts. A strong biometric or the device
 * credential, the same pair the wrapping key requires, so a cut finger never locks a holder out.
 * Below API 30 the pair is asked for through the older call.
 */
internal object AuthPrompt {
    fun info(reason: String): BiometricPrompt.PromptInfo {
        val builder = BiometricPrompt.PromptInfo.Builder()
            .setTitle("NØNOS")
            .setSubtitle(reason)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            builder.setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL).build()
        } else {
            @Suppress("DEPRECATION")
            builder.setDeviceCredentialAllowed(true).build()
        }
    }
}
