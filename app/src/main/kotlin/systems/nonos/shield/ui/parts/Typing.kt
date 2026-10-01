package systems.nonos.shield.ui.parts

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

/**
 * How the keyboard behaves in this app's fields. A keyboard learns what it is shown, so correction
 * and capitalisation are off to keep addresses and phrase words out of its dictionary. Exact text
 * is a visible password field, which stops learning but not a keyboard reading keys.
 */
internal object Typing {
    /** An address or a phrase: text the keyboard must not learn. */
    val exact = KeyboardOptions(
        capitalization = KeyboardCapitalization.None,
        autoCorrectEnabled = false,
        keyboardType = KeyboardType.Password,
    )

    /** An amount, which has a decimal point. */
    val amount = KeyboardOptions(
        autoCorrectEnabled = false,
        keyboardType = KeyboardType.Decimal,
    )
}
