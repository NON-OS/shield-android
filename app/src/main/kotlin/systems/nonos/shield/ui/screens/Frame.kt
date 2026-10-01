package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import systems.nonos.shield.core.Release
import systems.nonos.shield.ui.parts.Frame
import systems.nonos.shield.ui.parts.Heading
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.dismissFailure
import systems.nonos.shield.ui.state.real

/**
 * The frame for a screen, assembled once instead of at every call, so no screen drifts from the
 * others.
 */
@Composable
internal fun frame(number: String, title: String, state: UiState, model: WalletViewModel): Frame {
    val version = Release.version(LocalContext.current)
    return Frame(
        heading = Heading(number, title),
        failure = state.failure,
        status = status(version, state),
    ) { model.dismissFailure() }
}

/**
 * The things the line says: how the traffic leaves, which chain it reads, and which build is
 * running. It shows no soundness figure, since no verifier on the pool accepts the point this phone
 * proves at.
 */
private fun status(version: String, state: UiState): List<String> {
    val network = if (state.network.real) "mainnet, real money" else "sepolia, test"
    return listOf("tor, embedded", network, version)
}
