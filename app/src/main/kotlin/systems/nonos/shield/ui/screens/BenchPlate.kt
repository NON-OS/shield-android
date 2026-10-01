package systems.nonos.shield.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import systems.nonos.shield.core.BenchReport
import systems.nonos.shield.core.formatSeconds
import systems.nonos.shield.ui.parts.Fact
import systems.nonos.shield.ui.parts.Facts
import systems.nonos.shield.ui.parts.Statement
import systems.nonos.shield.ui.parts.shortened

/**
 * What a proof on this device actually cost, with the number as the subject. Everything under it is
 * the instance the time was for, so it can be reproduced. A failed verification voids the timing,
 * and the statement says so.
 */
@Composable
internal fun BenchPlate(report: BenchReport) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp), modifier = Modifier.fillMaxWidth()) {
        Statement(headline(report))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = formatSeconds(report.provedMs),
                style = MaterialTheme.typography.displayLarge,
            )
            Text(
                text = "seconds to prove",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 6.dp),
            )
        }
        Facts {
            Fact("circuit", if (report.launchPoint) "launch, the pool's" else "NOT the launch")
            Fact("params id", shortened(report.paramsId))
            Fact("verified here", if (report.verified) "inside the time" else "FALSE")
            Fact("witness built", formatSeconds(report.builtMs) + " s")
            Fact("proof", "${report.proofBytes} bytes")
            Fact("peak memory", report.peakKib?.let { "$it KiB" } ?: "not reported here")
            Fact("rows", "2 ^ ${report.shape.logTraceLen}")
            Fact("columns", "${report.shape.traceWidth}")
            Fact("queries", "${report.shape.queries} at grind ${report.shape.grindBits}")
            Fact("soundness", "${report.shape.securityBits} bits")
        }
    }
}

/**
 * What the proof is, before how long it took: a fast number from any circuit but the launch one is
 * not the number that matters.
 */
private fun headline(report: BenchReport): String = when {
    !report.verified -> "The proof did not verify."
    report.launchPoint -> "A real transfer, proved on this device."
    else -> "Proved, but not on the launch circuit."
}
