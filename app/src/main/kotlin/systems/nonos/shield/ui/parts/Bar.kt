package systems.nonos.shield.ui.parts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.unit.dp

/**
 * The bar at the top of every screen: the way back when there is one, the screen's number and name,
 * then the rule. With no way back the label shifts by the missing target's width, so the number
 * keeps the content's left edge.
 */
@Composable
internal fun Bar(heading: Heading, onBack: (() -> Unit)?) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = HALF, vertical = TIGHT),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (onBack != null) {
                Back(onBack)
            }
            Label(
                text = heading.title,
                number = heading.number,
                modifier = Modifier
                    .offset(x = if (onBack == null) HALF else 0.dp)
                    .clearAndSetSemantics {
                        contentDescription = heading.title
                        this.heading()
                    },
            )
        }
        Rule()
    }
}
