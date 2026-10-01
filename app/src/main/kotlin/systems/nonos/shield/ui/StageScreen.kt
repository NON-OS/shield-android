package systems.nonos.shield.ui

import androidx.compose.runtime.Composable
import systems.nonos.shield.ui.screens.BalanceScreen
import systems.nonos.shield.ui.screens.LicencesScreen
import systems.nonos.shield.ui.screens.LockedScreen
import systems.nonos.shield.ui.screens.MeasureScreen
import systems.nonos.shield.ui.screens.PhraseScreen
import systems.nonos.shield.ui.screens.RestoreScreen
import systems.nonos.shield.ui.screens.WelcomeScreen
import systems.nonos.shield.ui.state.Stage
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel

/**
 * The stage to screen mapping, and nothing else. It is exhaustive over Stage, so a new stage
 * without a screen fails to compile.
 */
@Composable
internal fun StageScreen(state: UiState, model: WalletViewModel) = when (state.stage) {
    Stage.Opening -> WelcomeScreen(state, model, opening = true)
    Stage.NeedsWallet -> WelcomeScreen(state, model, opening = false)
    Stage.ShowingPhrase -> PhraseScreen(state, model)
    Stage.Restoring -> RestoreScreen(state, model)
    Stage.Locked, Stage.KeyGone -> LockedScreen(state, model)
    Stage.Ready -> BalanceScreen(state, model)
    Stage.Measuring -> MeasureScreen(state, model)
    Stage.ShowingLicences -> LicencesScreen(state, model)
}
