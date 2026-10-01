package systems.nonos.shield.ui.parts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import systems.nonos.shield.core.Coin
import systems.nonos.shield.ui.theme.NoxMark

/** USDC's own blue. */
private val USDC_BLUE = Color(0xFF2775CA)

/** The paper white the brand sets text in. */
private val PAPER = Color(0xFFF2F3F7)

/** The Ethereum diamond's two halves, as fractions of the square it fills. */
private val UPPER = listOf(0.5f to 0.05f, 0.79f to 0.55f, 0.5f to 0.68f, 0.21f to 0.55f)
private val LOWER = listOf(0.21f to 0.61f, 0.5f to 0.95f, 0.79f to 0.61f, 0.5f to 0.74f)

/** How much of its square the Ø and the dollar sign fill. */
private const val MARK_SHARE = 0.8f
private const val SIGN_SHARE = 0.6f

/**
 * Each coin's mark, drawn in code at any size: the Ethereum diamond, the NØNOS Ø for NOX, and
 * USDC's blue disc. The figure beside each names the coin, so the mark is not read out.
 */
@Composable
internal fun CoinLogo(coin: Coin, side: Dp = 28.dp) {
    when (coin) {
        Coin.ETH -> Canvas(Modifier.size(side)) {
            for (half in listOf(UPPER, LOWER)) {
                val path = Path()
                half.forEachIndexed { i, (x, y) ->
                    if (i == 0) {
                        path.moveTo(
                            size.width * x,
                            size.height * y,
                        )
                    } else {
                        path.lineTo(size.width * x, size.height * y)
                    }
                }
                path.close()
                drawPath(path, PAPER)
            }
        }
        Coin.NOX -> NoxMark(size = side * MARK_SHARE)
        Coin.USDC -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(side)
                .background(USDC_BLUE, CircleShape)
                .clearAndSetSemantics {},
        ) {
            Text("$", color = Color.White, fontSize = (side.value * SIGN_SHARE).sp)
        }
    }
}
