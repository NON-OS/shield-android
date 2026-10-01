package systems.nonos.shield.ui.parts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * The frame every screen sits in: a bar, a scrolling subject, and the actions pinned where a thumb
 * reaches them, outside the scroll. The last strip is the machine's status line. The safe drawing
 * inset comes before padding, since API 35 draws every window edge to edge.
 */
@Composable
internal fun Screen(
    frame: Frame,
    onBack: (() -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val status = frame.status
    Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
        Bar(frame.heading, onBack)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            frame.backdrop?.let { BackdropBand(it) }
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = SIDE, vertical = ROOM),
                verticalArrangement = Arrangement.spacedBy(GAP),
            ) {
                if (frame.failure != null) {
                    FailureBanner(text = frame.failure, onDismiss = frame.onDismissFailure)
                }
                content()
            }
        }
        if (footer != null) {
            Rule()
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = SIDE, vertical = ROOM),
                verticalArrangement = Arrangement.spacedBy(TIGHT),
            ) {
                footer()
            }
        }
        if (status.isNotEmpty()) {
            Rule()
            StatusLine(status)
        }
    }
}
