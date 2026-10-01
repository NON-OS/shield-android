package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.Fact
import systems.nonos.shield.ui.parts.Facts
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.LocalAuthenticator
import systems.nonos.shield.ui.state.Stage
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.unlock

/**
 * The locked wallet. Unlocking confirms the user, then the keystore unwraps the vault key, and the
 * phrase is not asked for. Where the key lives and whether it needs the user are read from the key,
 * so the screen cannot claim a chip the device lacks.
 */
@Composable
internal fun LockedScreen(state: UiState, model: WalletViewModel) {
    val auth = LocalAuthenticator.current
    Screen(
        frame("02", "Locked", state, model).over(Backdrop.History),
        footer = {
            Primary(
                text = if (state.busy == Busy.Unlocking) "Unlocking" else "Unlock",
                enabled = state.busy == Busy.Idle,
            ) { model.unlock(auth) }
        },
    ) {
        Statement("This wallet is sealed.")
        Lead("The seed is under a key this device holds. Unlocking asks the device to unwrap it.")
        Facts {
            Fact("key held in", holder(state))
            Fact("released", if (state.userAuth) "after you are confirmed" else "on request")
        }
        if (state.stage == Stage.KeyGone) KeyGoneSection(state, model)
    }
}

/** Which rung the key actually landed on, as the key reported it. */
internal fun holder(state: UiState): String =
    if (state.strongBox) "the security chip" else "the keystore"
