package systems.nonos.shield.core

import kotlin.time.Duration.Companion.seconds
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * When the next scan lands. Gaps must vary enough to be no signature and stay bounded so an open
 * wallet keeps looking. A nonsense draw never gives a wait a loop spins on.
 */
class ScheduleTest {
    @Test
    fun the_middle_of_the_window_is_the_period() {
        assertEquals(Schedule.PERIOD, Schedule.next({ 0.5 }))
    }

    @Test
    fun the_ends_of_the_window_are_the_period_either_side_of_the_jitter() {
        assertEquals(Schedule.PERIOD - Schedule.JITTER, Schedule.next({ 0.0 }))
        assertEquals(Schedule.PERIOD + Schedule.JITTER, Schedule.next({ 1.0 }))
    }

    @Test
    fun every_draw_lands_inside_the_window() {
        val earliest = Schedule.PERIOD - Schedule.JITTER
        val latest = Schedule.PERIOD + Schedule.JITTER
        for (step in 0..100) {
            val wait = Schedule.next({ step / 100.0 })
            assertTrue("a draw of ${step / 100.0} left the window", wait in earliest..latest)
        }
    }

    @Test
    fun a_draw_outside_zero_to_one_is_pulled_back_in() {
        assertEquals(Schedule.PERIOD - Schedule.JITTER, Schedule.next({ -5.0 }))
        assertEquals(Schedule.PERIOD + Schedule.JITTER, Schedule.next({ 9.0 }))
    }

    @Test
    fun a_jitter_wider_than_the_period_still_never_spins() {
        val wait = Schedule.next({ 0.0 }, period = 10.seconds, jitter = 600.seconds)
        assertTrue("a loop would spin on $wait", wait >= 1.seconds)
    }
}
