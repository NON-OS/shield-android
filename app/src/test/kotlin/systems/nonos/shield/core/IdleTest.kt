package systems.nonos.shield.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * When an unlocked wallet left alone locks itself. The design assumes a phone can be taken while
 * unlocked, so this is asserted, not tried by hand.
 */
class IdleTest {
    @Test
    fun a_gap_at_the_limit_locks() {
        assertTrue(Idle.OneMinute.expired(now = 1_060, touched = 1_000))
    }

    @Test
    fun a_gap_under_the_limit_does_not() {
        assertFalse(Idle.OneMinute.expired(now = 1_059, touched = 1_000))
    }

    @Test
    fun never_never_locks_however_long_it_has_been() {
        assertFalse(Idle.Never.expired(now = Long.MAX_VALUE, touched = 0))
    }

    @Test
    fun immediately_locks_on_the_first_check() {
        assertTrue(Idle.Immediately.expired(now = 1_000, touched = 1_000))
    }

    /**
     * A clock that went backwards is not a reason to lock a wallet somebody is holding. It happens
     * after a manual time change or across a process restart.
     */
    @Test
    fun a_clock_that_went_backwards_does_not_lock() {
        assertFalse(Idle.FiveMinutes.expired(now = 900, touched = 1_000))
    }

    @Test
    fun the_choices_cycle_and_come_back_round() {
        assertEquals(Idle.OneMinute, Idle.Immediately.next())
        assertEquals(Idle.Immediately, Idle.Never.next())
    }

    @Test
    fun an_unreadable_choice_falls_back_rather_than_failing() {
        assertEquals(Idle.FiveMinutes, Idle.of(null))
        assertEquals(Idle.FiveMinutes, Idle.of(99))
        assertEquals(Idle.Never, Idle.of(-1))
    }
}
