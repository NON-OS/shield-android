package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.PublicSwap
import systems.nonos.shield.core.amountUnit
import systems.nonos.shield.ui.parts.Rule
import systems.nonos.shield.ui.parts.Tile
import systems.nonos.shield.ui.parts.TileRow

/** Every term of a quoted swap, in the order a trader reads them. */
@Composable
internal fun SwapTerms(swap: PublicSwap) {
    Tile {
        TileRow("Rate", swap.rate)
        TileRow("Minimum received", "${swap.minimumOut} ${amountUnit(swap.to)}")
        TileRow("Price impact", "${swap.priceImpactPercent}%")
        TileRow("Max slippage", "${swap.slippagePercent}%")
        Rule()
        TileRow("Route", swap.route)
        TileRow("Venue", swap.venue)
        for (pool in swap.pools) TileRow(
            if (swap.pools.size > 1) "Pool, 0.3% each" else "Pool, 0.3%",
            pool,
        )
        if (swap.from == Coin.NOX || swap.to == Coin.NOX) {
            TileRow(
                "NOX token fee",
                "${swap.tokenFee} NOX",
            )
        }
        Rule()
        TileRow("Network fee, at most", "${swap.maxNetworkFee} ETH")
        TileRow("Nonce, chain", "${swap.nonce}, ${swap.chainId}")
    }
}
