package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import systems.nonos.shield.core.Provenance
import systems.nonos.shield.core.Release
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.Fact
import systems.nonos.shield.ui.parts.Facts
import systems.nonos.shield.ui.parts.Quiet
import systems.nonos.shield.ui.parts.QuietGroup
import systems.nonos.shield.ui.parts.QuietRule
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.Stage
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.cycleIdle
import systems.nonos.shield.ui.state.lock
import systems.nonos.shield.ui.state.show
import systems.nonos.shield.ui.state.toggleRecording

/**
 * What a holder can change or find out about this wallet on this device. A row that cannot do its
 * job yet is absent, not disabled. What the wallet is comes from the key, so this screen cannot
 * claim a chip the device is not using.
 */
@Composable
internal fun SettingsPanel(
    state: UiState,
    model: WalletViewModel,
    onKeys: () -> Unit,
    onBack: () -> Unit,
) {
    var confirming by remember { mutableStateOf(false) }
    Screen(frame("04", "Settings", state, model).over(Backdrop.Settings), onBack = onBack) {
        Facts {
            Fact("app", Release.version(LocalContext.current))
            Fact("core", Provenance.of(LocalContext.current).line)
            Fact("rustc", Provenance.of(LocalContext.current).rustc)
            Fact("ndk", Provenance.of(LocalContext.current).ndk)
            Fact("key held in", holder(state))
            Fact("released", if (state.userAuth) "after you are confirmed" else "on request")
            Fact("route", "tor, embedded")
            Fact("relayer", "asked for when sending opens")
        }
        QuietGroup {
            Quiet("Recovery words and keys", state.busy == Busy.Idle) { onKeys() }
            QuietRule()
            Quiet("Lock now", state.busy == Busy.Idle) { model.lock() }
            QuietRule()
            Quiet("Lock when idle: ${state.idle.label}") { model.cycleIdle() }
            QuietRule()
            Quiet(recordingRow(state)) { model.toggleRecording() }
            QuietRule()
            Quiet("Licences") { model.show(Stage.ShowingLicences) }
            QuietRule()
            Quiet("Destroy this wallet", state.busy == Busy.Idle) { confirming = true }
        }
        if (confirming) {
            WipeConfirmation(state, model) { confirming = false }
        }
        Credits()
    }
}

private fun recordingRow(state: UiState): String = if (state.recordingAllowed) {
    "Screen recording and screenshots: allowed, words and keys hidden"
} else {
    "Screen recording and screenshots: hidden"
}
