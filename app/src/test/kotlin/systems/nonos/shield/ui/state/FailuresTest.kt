package systems.nonos.shield.ui.state

import org.junit.Assert.assertEquals
import org.junit.Test
import systems.nonos.shield.core.AuthFailure
import systems.nonos.shield.core.CustodyException
import systems.nonos.shield.core.NetException
import systems.nonos.shield.core.WalletException

/**
 * The table of core failure to sentence, asserted. The iOS suite asserts the same table word for
 * word, so both shells say the same thing. No sentence carries detail from the failure, so nothing
 * leaks onto a filmed screen.
 */
class FailuresTest {
    @Test
    fun every_wallet_failure_has_its_sentence() {
        assertEquals(
            "That is more than the balance covers.",
            describe(WalletException.Insufficient()),
        )
        assertEquals(
            "Two notes are needed to send. Receive again first.",
            describe(WalletException.NoSpendPlan()),
        )
        assertEquals("That recipient address is not valid.", describe(WalletException.Address()))
        assertEquals("That amount is out of range.", describe(WalletException.Amount()))
        assertEquals(
            "That review has expired. Review the send again.",
            describe(WalletException.ReviewExpired()),
        )
        assertEquals("There is no such account.", describe(WalletException.NoSuchAccount()))
        assertEquals("That is not in this build yet.", describe(WalletException.Unavailable()))
        assertEquals(
            "The pool would not take this fee. Try again in a minute, or update the app.",
            describe(WalletException.FeeTooHigh()),
        )
    }

    @Test
    fun a_network_failure_says_nothing_was_sent() {
        assertEquals(
            "A server answered for the wrong network. Nothing was signed.",
            describe(WalletException.Net(NetException.WrongChain())),
        )
        assertEquals(
            "The connection over Tor dropped before an answer came. Try again.",
            describe(WalletException.Net(NetException.Transport())),
        )
    }

    @Test
    fun a_custody_failure_names_what_refused() {
        assertEquals(
            "The wallet is locked.",
            describe(WalletException.Custody(CustodyException.Locked())),
        )
        assertEquals(
            "The device keystore refused.",
            describe(WalletException.Custody(CustodyException.Guard())),
        )
        assertEquals(
            "That recovery phrase did not check out.",
            describe(WalletException.Custody(CustodyException.Mnemonic())),
        )
        assertEquals(
            "A recovery phrase is 12, 15, 18, 21 or 24 words.",
            describe(WalletException.Custody(CustodyException.PhraseLength())),
        )
        assertEquals(
            "A wallet is already stored on this device.",
            describe(WalletException.Custody(CustodyException.VaultPresent())),
        )
    }

    @Test
    fun a_custody_failure_this_shell_does_not_name_is_still_reported() {
        assertEquals(
            "The stored wallet could not be opened on this device.",
            describe(WalletException.Custody(CustodyException.VaultShape())),
        )
    }

    @Test
    fun the_device_refusing_to_confirm_is_its_own_sentence() {
        assertEquals("This device would not confirm it was you.", describe(AuthFailure.Refused()))
    }

    @Test
    fun a_failure_from_nowhere_is_reported_rather_than_swallowed() {
        assertEquals("Something went wrong.", describe(IllegalStateException()))
    }
}
