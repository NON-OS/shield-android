package systems.nonos.shield.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import systems.nonos.shield.ui.parts.Settle
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.touched
import systems.nonos.shield.ui.theme.ShieldTheme

/**
 * The whole navigation: the core's stage picks the screen, so no back stack drifts from the wallet.
 * The root is a Surface so uncoloured text takes the palette, not black.
 */
@Composable
fun ShieldRoot(model: WalletViewModel) {
    val state: UiState by model.state.collectAsStateWithLifecycle()
    ShieldTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxSize()
                // Every touch feeds the idle lock. It watches the initial pass and consumes
                // nothing, so buttons still get their presses.
                .pointerInput(Unit) {
                    while (true) {
                        awaitPointerEventScope { awaitPointerEvent(PointerEventPass.Initial) }
                        model.touched()
                    }
                },
        ) {
            Settle(state.stage) { stage ->
                StageScreen(state.copy(stage = stage), model)
            }
        }
    }
}
