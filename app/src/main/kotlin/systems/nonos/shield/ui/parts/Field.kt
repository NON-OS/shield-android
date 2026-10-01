package systems.nonos.shield.ui.parts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A field with its name above it, always, in mono and never floated into the box. The keyboard
 * options are the caller's because they are a privacy decision: no correction, capitalisation or
 * learning. iOS refuses the dictionary in the same place.
 */
@Composable
internal fun Field(ask: Ask, value: String, onValueChange: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        Label(text = ask.name)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = ask.keyboard,
            singleLine = ask.lines == 1,
            minLines = ask.lines,
            shape = EDGE,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
