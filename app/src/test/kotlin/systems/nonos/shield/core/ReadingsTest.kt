package systems.nonos.shield.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReadingsTest {
    @Test
    fun the_peak_line_of_proc_status_reads_as_kib() {
        assertEquals(228_864L, ProcessMemory.kib("VmHWM:\t  228864 kB"))
        assertNull(ProcessMemory.kib("VmHWM:"))
    }

    @Test
    fun thermal_status_reads_as_its_platform_name() {
        assertEquals("none", Thermal.word(0))
        assertEquals("severe", Thermal.word(3))
        assertEquals("unknown", Thermal.word(9))
    }
}
