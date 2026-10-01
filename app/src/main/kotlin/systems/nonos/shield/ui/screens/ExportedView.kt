package systems.nonos.shield.ui.screens

import androidx.compose.runtime.Composable
import systems.nonos.shield.ui.parts.Label
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Primary
import systems.nonos.shield.ui.parts.QrCode
import systems.nonos.shield.ui.parts.Secondary
import systems.nonos.shield.ui.parts.Tile
import systems.nonos.shield.ui.parts.TileRow
import systems.nonos.shield.ui.parts.Value
import systems.nonos.shield.ui.state.Exported

/** Words per row of the grid, the way they are written down. */
private const val WORDS_PER_ROW = 3

/**
 * What an export shows. The words are never copied and never drawn as a code, only read and
 * written down. A key can be copied, marked sensitive and cleared a minute later, and a view key
 * is also drawn as a QR code for another wallet to scan.
 */
@Composable
internal fun ExportedView(shown: Exported, onCopy: () -> Unit, onDone: () -> Unit) {
    Label(shown.title)
    when {
        shown.words && shown.text.isEmpty() -> Lead(
            "This wallet keeps its seed and not its words, so they cannot be shown again. " +
                "If the written copy is not certain, create a new wallet, write its words " +
                "down, and move the value to it.",
        )
        shown.words -> Words(shown.text.split(" "))
        else -> {
            Value(shown.text)
            if (shown.text.startsWith("nox")) QrCode(shown.text)
            Secondary("Copy, cleared in a minute", onClick = onCopy)
        }
    }
    Primary("Done", onClick = onDone)
}

@Composable
private fun Words(words: List<String>) {
    Tile {
        words.chunked(WORDS_PER_ROW).forEachIndexed { row, three ->
            val start = row * WORDS_PER_ROW
            TileRow(
                "${start + 1} to ${start + three.size}",
                three.joinToString("  "),
            )
        }
    }
}
