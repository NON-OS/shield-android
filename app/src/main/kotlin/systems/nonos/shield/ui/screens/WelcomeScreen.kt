package systems.nonos.shield.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import systems.nonos.shield.core.Authenticator
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.Secondary
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.LocalAuthenticator
import systems.nonos.shield.ui.state.Stage
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.create
import systems.nonos.shield.ui.state.show
import systems.nonos.shield.ui.theme.NoxLogo

/**
 * The first screen: create a wallet or restore one. While the core is still
 * opening, the same screen shows without its actions.
 */
@Composable
internal fun WelcomeScreen(state: UiState, model: WalletViewModel, opening: Boolean) {
    val auth = LocalAuthenticator.current
    Screen(
        frame("01", "NØNOS", state, model).over(Backdrop.Welcome),
        footer = { if (!opening) Start(state, model, auth) },
    ) {
        NoxLogo(height = 44.dp)
        Statement("Shielded NOX. The keys never leave this phone.")
        Lead(
            "The balance is worked out here, from the pool itself, read over Tor. " +
                "No server is told which notes are yours.",
        )
        if (opening) {
            Lead("Opening")
        }
    }
}

@Composable
private fun Start(state: UiState, model: WalletViewModel, auth: Authenticator) {
    val ready = state.busy == Busy.Idle && state.deviceSecure
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        if (!state.deviceSecure) {
            Lead("Set a screen lock first. The keystore will not seal a seed without one.")
        }
        Primary("Create a wallet", enabled = ready) { model.create(auth) }
        Secondary("Restore from a phrase", enabled = ready) { model.show(Stage.Restoring) }
    }
}
