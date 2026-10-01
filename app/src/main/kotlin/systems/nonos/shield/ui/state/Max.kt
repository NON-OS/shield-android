package systems.nonos.shield.ui.state

import systems.nonos.shield.core.Coin

/**
 * The Max amount, from the core: all of a token, and ether less the most a
 * swap fee can be, so a swap of everything still pays for itself.
 */
internal fun WalletViewModel.fillMax(coin: Coin, into: (String) -> Unit) =
    launchCore(Busy.Reading) {
        val open = wallet ?: return@launchCore
        val max = open.publicMax(current().network, coin)
        set { it.copy(busy = Busy.Idle) }
        into(max)
    }
