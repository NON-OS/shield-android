package systems.nonos.shield.ui.state

import systems.nonos.shield.core.AuthFailure
import systems.nonos.shield.core.CustodyException
import systems.nonos.shield.core.WalletException

/**
 * Turn a core failure into one line for a screen. The text comes from the error variant, which
 * carries no secret. An unknown failure is shown as itself, never swallowed.
 */
internal fun describe(cause: Throwable): String = when (cause) {
    is WalletException.Insufficient -> "That is more than the balance covers."
    is WalletException.NoSpendPlan -> "Two notes are needed to send. Receive again first."
    is WalletException.Address -> "That recipient address is not valid."
    is WalletException.Amount -> "That amount is out of range."
    is WalletException.ReviewExpired -> "That review has expired. Review the send again."
    is WalletException.NoSuchAccount -> "There is no such account."
    is WalletException.Unavailable -> "That is not in this build yet."
    is WalletException.NotStandard ->
        "Amounts come in standard sizes, 1, 2 or 5 followed by zeros, " +
            "from 1,000 NOX or 0.01 ETH."
    is WalletException.FeeTooHigh ->
        "The pool would not take this fee. Try again in a minute, or update the app."
    is WalletException.TooSoon ->
        "These notes are still new. Wait, or go early from the send screen."
    is WalletException.Custody -> custody(cause)
    is WalletException.Store -> "The note store could not be read."
    is WalletException.Net -> network(cause.source)
    is WalletException.Prove -> proving(cause.source)
    is CustodyException.Guard -> "The device keystore refused."
    is AuthFailure.Refused -> "This device would not confirm it was you."
    else -> cause.message ?: "Something went wrong."
}

private fun custody(cause: WalletException.Custody): String = when (cause.source) {
    is CustodyException.Locked -> "The wallet is locked."
    is CustodyException.Guard -> "The device keystore refused."
    is CustodyException.Mnemonic -> "That recovery phrase did not check out."
    is CustodyException.PhraseLength -> "A recovery phrase is 12, 15, 18, 21 or 24 words."
    is CustodyException.VaultPresent -> "A wallet is already stored on this device."
    is CustodyException.KeyShape -> "That private key is not valid: 64 hex digits of a real key."
    else -> "The stored wallet could not be opened on this device."
}
