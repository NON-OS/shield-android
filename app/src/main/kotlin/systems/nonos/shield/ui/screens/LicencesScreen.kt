package systems.nonos.shield.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import systems.nonos.shield.core.Licences
import systems.nonos.shield.ui.parts.Backdrop
import systems.nonos.shield.ui.parts.Label
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Screen
import systems.nonos.shield.ui.parts.over
import systems.nonos.shield.ui.state.Stage
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.show

/** Loose enough that a wrapped coordinate reads as one entry. */
private val LINE = 22.sp

/**
 * What is in the package, and what each part is licensed under, from the dependency set the build
 * resolved. It is a screen, not a link, since reading it online would be a request this app
 * otherwise never makes.
 */
@Composable
internal fun LicencesScreen(state: UiState, model: WalletViewModel) {
    val context = LocalContext.current
    val entries = remember { Licences.of(context) }
    val texts = remember { Licences.texts(context) }
    Screen(
        frame("06", "Licences", state, model).over(Backdrop.Settings),
        onBack = { model.show(Stage.Ready) },
    ) {
        Lead("Generated from what the build resolved, so it is what the package holds.")
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            entries.forEach { Entry(it) }
        }
        Label("Notices")
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            texts.forEach { Entry(it) }
        }
    }
}

@Composable
private fun Entry(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = LINE,
    )
}
