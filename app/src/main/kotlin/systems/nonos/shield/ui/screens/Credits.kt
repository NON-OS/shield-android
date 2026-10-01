package systems.nonos.shield.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import systems.nonos.shield.ui.parts.Label
import systems.nonos.shield.ui.parts.Lead

/**
 * About, and the acknowledgement the photographs' licence asks for. The Etna photographs are CC BY
 * 2.0, credited here and nowhere else, in the brand's words.
 */
@Composable
internal fun Credits() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Label("About")
        Lead(
            "NØNOS Wallet: private payments on Ethereum. NOX is the token; " +
                "NOX Shield is the private layer.",
        )
        Lead("Designed after Etna, the volcano of Sicily, where NØNOS began.")
        Label("Acknowledgements")
        Lead("Etna photographs: gnuckx, CC BY 2.0, recoloured by NØNOS")
    }
}
