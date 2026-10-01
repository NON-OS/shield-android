package systems.nonos.shield.ui.parts

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * How a long value is broken up for reading, asserted instead of eyeballed. An address is compared
 * against something written down, so runs are stable, the last may be short, and no character is
 * dropped or padded.
 */
class GroupsTest {
    @Test
    fun runs_are_eight_characters_and_separated_by_one_space() {
        assertEquals("abcdefgh ijklmnop", groups("abcdefghijklmnop"))
    }

    @Test
    fun the_last_run_is_allowed_to_be_short() {
        assertEquals("abcdefgh ijk", groups("abcdefghijk"))
    }

    @Test
    fun nothing_is_added_or_lost() {
        val value = "nox1" + "q".repeat(122)
        assertEquals(value, groups(value).filterNot { it == ' ' })
    }

    @Test
    fun a_value_shorter_than_one_run_is_left_alone() {
        assertEquals("nox1", groups("nox1"))
    }

    @Test
    fun a_shortened_value_keeps_both_ends_and_marks_the_cut() {
        assertEquals("2gzyxa…n53wid", shortened("2gzyxa5ihm7nsggclen53wid", ends = 6))
    }

    @Test
    fun a_value_that_already_fits_is_not_cut() {
        assertEquals("127.0.0.1", shortened("127.0.0.1", ends = 6))
    }
}
