package systems.nonos.shield.ui.state

import org.junit.Assert.assertEquals
import org.junit.Test

class ProfileTextTest {
    private fun run(fri: Long, matched: Boolean, after: String) = ProfileRun(
        phases = listOf("Request" to 5L, "Fri" to fri),
        totalMs = 5 + fri,
        matched = matched,
        pssKib = 2048,
        peakKib = null,
        thermalBefore = "none",
        thermalAfter = after,
    )

    @Test
    fun every_run_is_a_row_the_first_cold_and_a_hot_one_kept() {
        val text = profileText(
            "Pixel 7, Android 15, arm64-v8a, 1080x2400, 8.1 GB RAM",
            "all cores",
            listOf(run(100, true, "none"), run(120, false, "severe")),
        )
        assertEquals(
            """
            NØNOS profile, Pixel 7, Android 15, arm64-v8a, 1080x2400, 8.1 GB RAM, all cores

            | run | Request | Fri | total | match | PSS MiB | peak MiB | thermal |
            |---|---|---|---|---|---|---|---|
            | 1 cold | 5 | 100 | 105 | yes | 2 | not reported | none to none |
            | 2 | 5 | 120 | 125 | NO | 2 | not reported | none to severe |
            """.trimIndent() + "\n",
            text,
        )
    }
}
