package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.RealMoney
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel

/**
 * A send from the public account, in three states: the form, the review the
 * core worked out, and what was sent. Nothing is signed until the review has
 * been read and the owner has confirmed it on the device.
 */
@Composable
internal fun AccountSendPanel(state: UiState, model: WalletViewModel, onBack: () -> Unit) {
    Screen(frame("03", "Send", state, model).over(Backdrop.Send), onBack = onBack) {
        RealMoney(state.network)
        val sent = state.sent
        val review = state.review
        when {
            sent != null -> AccountSentView(state, sent, model, onBack)
            review != null -> AccountReviewView(state, review, model)
            else -> AccountSendForm(state, model)
        }
    }
}
