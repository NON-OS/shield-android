package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.core.Authenticator
import systems.nonos.shield.core.ViewKind
import systems.nonos.shield.core.viewKeyExport
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Quiet
import systems.nonos.shield.ui.parts.QuietGroup
import systems.nonos.shield.ui.parts.QuietRule
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.LocalAuthenticator
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.activeName
import systems.nonos.shield.ui.state.clearExported
import systems.nonos.shield.ui.state.copyExported
import systems.nonos.shield.ui.state.exportPublicKey
import systems.nonos.shield.ui.state.exportViewKey
import systems.nonos.shield.ui.state.showWords

/**
 * The words and the keys of the active account, each shown only after the device confirms the
 * owner. The screen is secure, so no screenshot or recording holds them. The spend secret alone is
 * never shown: the words are its only backup.
 */
@Composable
internal fun KeysPanel(state: UiState, model: WalletViewModel, onBack: () -> Unit) {
    val auth = LocalAuthenticator.current
    val leave = {
        model.clearExported()
        onBack()
    }
    Screen(frame("06", "Keys", state, model).over(Backdrop.Backup), onBack = leave) {
        val shown = state.exported
        if (shown != null) {
            ExportedView(shown, onCopy = { model.copyExported() }) { model.clearExported() }
            return@Screen
        }
        Lead(
            if (state.fromKey) {
                "Keys of ${state.activeName}. It came from a private key, which is its backup."
            } else {
                "Keys of ${state.activeName}."
            },
        )
        KeyRows(state, model, auth)
        Lead(
            "A view key shows every note of this account, past and future, to whoever holds it. " +
                "It cannot be revoked. The only way to stop it is to move the value to a new " +
                "account.",
        )
    }
}

/** The words when the wallet has them, the account key, and the view keys when exported. */
@Composable
private fun KeyRows(state: UiState, model: WalletViewModel, auth: Authenticator) {
    val idle = state.busy == Busy.Idle
    QuietGroup {
        if (!state.fromKey) {
            Quiet("Show the recovery words", idle) { model.showWords(auth) }
            QuietRule()
        }
        Quiet("Export the account private key", idle) { model.exportPublicKey(auth) }
        if (viewKeyExport()) {
            QuietRule()
            Quiet("Export the incoming view key", idle) {
                model.exportViewKey(ViewKind.INCOMING, auth)
            }
            QuietRule()
            Quiet("Export the full view key", idle) { model.exportViewKey(ViewKind.FULL, auth) }
        }
    }
}
