package systems.nonos.shield.ui.screens

import android.content.Context
import systems.nonos.shield.core.Card
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.card

/**
 * Sending the last measurement card to whatever the holder picks. It reads the file the run wrote,
 * so what leaves is what is on disk. An unreadable card does nothing, since an empty share looks
 * like a sent measurement.
 */
internal fun share(context: Context, model: WalletViewModel) {
    val card = model.card() ?: return
    context.startActivity(Card.share(card))
}
