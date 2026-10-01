package systems.nonos.shield.ui.parts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

/**
 * How long the thing on screen has been running, counted by the shell. The prover reports no
 * progress, so this is the shell's own wall clock from when it appears. It is never the proving
 * time the core reports.
 */
@Composable
internal fun elapsedSeconds(): Long {
    var seconds by remember { mutableLongStateOf(0L) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1.seconds)
            seconds += 1
        }
    }
    return seconds
}
