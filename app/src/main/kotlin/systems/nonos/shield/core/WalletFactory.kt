package systems.nonos.shield.core

import android.content.Context
import java.io.File

/**
 * Builds the core's wallet object for this device: the private directory it stores into and the
 * keystore that guards its seed. It relies on the platform keeping `filesDir` readable only by this
 * app.
 */
internal object WalletFactory {
    /**
     * The wallet on this device. No relayer is set until sending opens, since receiving, depositing
     * and reading the pool need none.
     */
    fun open(context: Context): Wallet = Wallet(
        directory = storage(context).absolutePath,
        guard = DeviceGuard(),
    )

    /**
     * App private storage, and not the external or cache directories: the note store has to survive
     * a cache eviction and must not be readable by another app or by a file manager.
     */
    private fun storage(context: Context): File =
        File(context.filesDir, "shield").apply { mkdirs() }
}
