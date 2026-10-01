package systems.nonos.shield.core

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * What leaves the device when a holder shares a measurement card. No other file is reachable, since
 * only text leaves, and this asserts the text is the run's output and nothing else.
 */
class CardTest {
    private val card = """
        proved: 587415 ms
        verified: 1238 ms, true
        proof: 1017516 bytes
    """.trimIndent()

    @Test
    fun the_card_leaves_exactly_as_the_run_wrote_it() {
        assertEquals(card, Card.body(card))
    }

    @Test
    fun a_trailing_newline_from_the_file_is_not_a_line_of_the_card() {
        assertEquals(card, Card.body("$card\n\n"))
    }

    @Test
    fun nothing_is_added_to_it() {
        val shared = Card.body(card)
        assertEquals(card.lines().size, shared.lines().size)
        assertEquals(card.lines().first(), shared.lines().first())
        assertEquals(card.lines().last(), shared.lines().last())
    }
}
