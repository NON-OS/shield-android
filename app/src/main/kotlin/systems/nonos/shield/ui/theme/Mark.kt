package systems.nonos.shield.ui.theme

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import systems.nonos.shield.R

/**
 * The NØNOS icon from the brand kit, drawn from its vectors and tinted by the theme, so one asset
 * serves every surface at any size. The tint is the cyan token, the colour the brand ships the mark
 * in.
 */
@Composable
internal fun NoxMark(size: Dp = 28.dp, modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(R.drawable.nonos_icon),
        contentDescription = null,
        tint = Cyan,
        modifier = modifier.size(size),
    )
}
