package systems.nonos.shield.ui.parts

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import systems.nonos.shield.R
import systems.nonos.shield.ui.theme.Ink

/**
 * The photograph at the head of each section: Etna in eruption, recoloured to the brand's Ink, Deep
 * Teal and Cyan. Nothing is written on it, since text over lava fails contrast. It scrolls away
 * with the content and TalkBack skips it.
 */
internal enum class Backdrop(@DrawableRes val image: Int) {
    Welcome(R.drawable.etna_welcome),
    Home(R.drawable.etna_home),
    Send(R.drawable.etna_send),
    Receive(R.drawable.etna_receive),
    Deposit(R.drawable.etna_deposit),
    Withdraw(R.drawable.etna_withdraw),
    Proving(R.drawable.etna_proving),
    History(R.drawable.etna_history),
    Settings(R.drawable.etna_settings),
    Backup(R.drawable.etna_backup),
}

/** The header photographs are 1290 by 860. */
private const val ASPECT = 1290f / 860f

/* A light shade at the top, the photograph clear in the middle, a fade to Ink below. */
private const val TOP_SHADE = 0.35f
private const val CLEAR_FROM = 0.3f
private const val CLEAR_TO = 0.55f

@Composable
internal fun BackdropBand(backdrop: Backdrop) {
    Box(modifier = Modifier.fillMaxWidth().aspectRatio(ASPECT)) {
        Image(
            painter = painterResource(backdrop.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize(),
        )
        Box(
            modifier = Modifier.matchParentSize().background(
                Brush.verticalGradient(
                    0f to Ink.copy(alpha = TOP_SHADE),
                    CLEAR_FROM to Ink.copy(alpha = 0f),
                    CLEAR_TO to Ink.copy(alpha = 0f),
                    1f to Ink,
                ),
            ),
        )
    }
}
