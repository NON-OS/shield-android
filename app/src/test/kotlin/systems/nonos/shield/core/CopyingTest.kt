package systems.nonos.shield.core

import kotlin.time.Duration
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * That a copied address is taken back, observed instead of asserted in a comment. A change that
 * dropped the clear, or cleared before a paste, would pass every other test.
 */
class CopyingTest {
    private class Fake : Clip {
        val done = mutableListOf<String>()
        override fun hold(address: String) {
            done += "hold $address"
        }

        override fun release() {
            done += "release"
        }
    }

    @Test
    fun the_address_is_held_and_then_released_after_the_wait() = runBlocking {
        val clip = Fake()
        val waits = mutableListOf<Duration>()
        val flags = mutableListOf<Boolean>()
        copyThenClear(
            clip = clip,
            address = "nox1abc",
            onCopied = { flags += it },
            wait = Wait { waits += it },
        )
        assertEquals(listOf("hold nox1abc", "release"), clip.done)
        assertEquals(listOf(HOLD), waits)
        assertEquals(listOf(true, false), flags)
    }

    @Test
    fun the_wait_is_a_minute() {
        assertEquals(60, HOLD.inWholeSeconds)
    }

    @Test
    fun an_empty_address_touches_nothing() = runBlocking {
        val clip = Fake()
        val flags = mutableListOf<Boolean>()
        copyThenClear(clip = clip, address = "", onCopied = { flags += it }, wait = Wait { })
        assertEquals(emptyList<String>(), clip.done)
        assertEquals(emptyList<Boolean>(), flags)
    }
}
