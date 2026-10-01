package systems.nonos.shield.ui.state

import systems.nonos.shield.core.Balance
import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.Network
import systems.nonos.shield.core.PublicBalances

/** The shielded balance of one coin, or nothing before the first read. */
internal fun UiState.shield(coin: Coin): Balance? = balances.firstOrNull { it.coin == coin }

/** The public account on the network being shown. */
internal val UiState.publicHere: PublicBalances? get() = accounts[network]

/** The name a screen gives the network. */
internal val Network.label: String
    get() = when (this) {
        Network.MAINNET -> "Ethereum"
        Network.SEPOLIA -> "Sepolia"
    }

/** Whether money here is real. */
internal val Network.real: Boolean get() = this == Network.MAINNET
