package systems.nonos.shield.ui.theme

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import systems.nonos.shield.R

/** The logo is 1428 by 500 units wide and high. */
private const val WIDE = 1428f / 500f

/**
 * The NØNOS logo: the Ø knocked out of a rounded square, and the wordmark.
 * One vector tinted cyan, the colour the brand ships its mark in, the same
 * logo the iOS app draws.
 */
@Composable
internal fun NoxLogo(height: Dp = 24.dp, modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(R.drawable.nonos_logo),
        contentDescription = "NØNOS",
        tint = Cyan,
        modifier = modifier.height(height).width(height * WIDE),
    )
}
