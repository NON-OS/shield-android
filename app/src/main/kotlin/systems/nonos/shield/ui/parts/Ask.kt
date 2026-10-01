package systems.nonos.shield.ui.parts

import androidx.compose.foundation.text.KeyboardOptions

/**
 * What a field is asking for: its name, the keyboard that fits, and how many
 * lines it needs. The three travel together because they describe one question,
 * and passing them separately made every form take one argument more than a
 * form should.
 */
internal data class Ask(val name: String, val keyboard: KeyboardOptions, val lines: Int = 1)
