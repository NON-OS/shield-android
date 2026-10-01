package systems.nonos.shield.core

import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec

/**
 * The platform half of the wallet's custody. The core hands over its vault key and gets back a blob
 * only this device can open. No key material is kept in a field, and every failure is a guard
 * error, since a half-done wrap would write a vault nothing can open.
 */
class DeviceGuard : HardwareGuard {
    override fun wrap(key: ByteArray): ByteArray {
        val secret = StrongBoxGuard.key() ?: throw CustodyException.Guard()
        return runCatching {
            val cipher = Cipher.getInstance(TRANSFORM).apply { init(Cipher.ENCRYPT_MODE, secret) }
            val sealed = cipher.doFinal(key)
            cipher.iv + sealed
        }.getOrElse { throw CustodyException.Guard() }.also { KeyProbe.mismatched = false }
    }

    override fun unwrapKey(blob: ByteArray): ByteArray {
        val secret = StrongBoxGuard.key() ?: throw CustodyException.Guard()
        if (blob.size <= IV_BYTES) throw CustodyException.Guard()
        return runCatching {
            val spec = GCMParameterSpec(TAG_BITS, blob, 0, IV_BYTES)
            val cipher = Cipher.getInstance(TRANSFORM)
                .apply { init(Cipher.DECRYPT_MODE, secret, spec) }
            cipher.doFinal(blob, IV_BYTES, blob.size - IV_BYTES)
        }.getOrElse { failure ->
            if (failure is AEADBadTagException) KeyProbe.mismatched = true
            throw CustodyException.Guard()
        }.also { KeyProbe.mismatched = false }
    }

    private companion object {
        const val TRANSFORM = "AES/GCM/NoPadding"
        const val IV_BYTES = 12
        const val TAG_BITS = 128
    }
}
