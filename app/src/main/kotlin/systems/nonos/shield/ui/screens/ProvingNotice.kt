package systems.nonos.shield.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import systems.nonos.shield.ui.parts.Fact
import systems.nonos.shield.ui.parts.Facts
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Secondary
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.parts.elapsedSeconds
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.cancelProof

/**
 * What a proof in progress looks like. The bar is indeterminate since the prover reports no
 * progress, and the seconds shown are the shell's own count. A cancel discards the proof when its
 * arithmetic finishes, and the line says so.
 */
@Composable
internal fun ProvingNotice(state: UiState, model: WalletViewModel) {
    val seconds = elapsedSeconds()
    Statement(if (state.busy == Busy.Cancelling) "Finishing, then discarding." else "Proving.")
    Lead(
        if (state.busy == Busy.Cancelling) {
            "The proof will be thrown away rather than sent. It cannot be stopped mid proof."
        } else {
            "On this device, using every core it has. Nothing has been sent."
        },
    )
    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    Facts {
        Fact("elapsed", "$seconds s")
        Fact("circuit", "launch, 19 queries, grind 28")
    }
    val running = state.busy == Busy.Proving || state.busy == Busy.Measuring
    Secondary("Cancel", enabled = running) { model.cancelProof() }
}
