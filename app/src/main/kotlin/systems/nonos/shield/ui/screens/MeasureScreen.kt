package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.Secondary
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.Stage
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.measureProof

/**
 * The measurement screen. It ships in the app because the gate the product
 * turns on is a proof timed on a real phone, and a number from a laptop is not
 * that. Everything it shows was observed on this device.
 */
@Composable
internal fun MeasureScreen(state: UiState, model: WalletViewModel) {
    val context = LocalContext.current
    Screen(
        frame("05", "Measure", state, model).over(Backdrop.Proving),
        onBack = { model.set { showing -> showing.copy(stage = Stage.Ready) } },
        footer = {
            Primary("Time one proof", enabled = state.busy == Busy.Idle) { model.measureProof() }
            state.bench?.proofPath?.let { path ->
                Secondary("Export the proof") { shareProof(context, path) }
            }
            if (state.cardPath != null) {
                Secondary("Share the card") { share(context, model) }
            }
        },
    ) {
        if (state.busy == Busy.Measuring || state.busy == Busy.Cancelling) {
            ProvingNotice(state, model)
        }
        state.bench?.let { BenchPlate(it) }
    }
}
