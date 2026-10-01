package systems.nonos.shield.ui.state

import android.content.Context
import systems.nonos.shield.core.Network

/**
 * Which network the public account is shown on, kept between launches. Sepolia is the default, so a
 * fresh install never lands on real money. Switching drops a pending review, which belongs to its
 * own network.
 */
private const val PREFS = "network"

internal fun WalletViewModel.savedNetwork(): Network {
    val saved = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(PREFS, null)
    return if (saved == "mainnet") Network.MAINNET else Network.SEPOLIA
}

internal fun WalletViewModel.choose(network: Network) {
    context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
        .putString(PREFS, if (network == Network.MAINNET) "mainnet" else "sepolia").apply()
    set { it.copy(network = network, review = null, sent = null, sentStatus = null) }
    readAccount()
}
