package systems.nonos.shield.core

import android.app.KeyguardManager
import android.content.Context

/**
 * Whether this device has a screen lock at all. The keystore will not generate a key requiring the
 * user on a device with no credential, so the welcome screen asks first and can say why.
 */
internal object ScreenLock {
    /** True when a PIN, pattern, password or biometric is enrolled. */
    fun secure(context: Context): Boolean {
        val keyguard = context.getSystemService(KeyguardManager::class.java) ?: return false
        return keyguard.isDeviceSecure
    }
}
