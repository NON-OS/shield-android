package systems.nonos.shield.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import systems.nonos.shield.ui.parts.Avatar
import systems.nonos.shield.ui.parts.shortened
import systems.nonos.shield.ui.state.Busy
import systems.nonos.shield.ui.state.UiState
import systems.nonos.shield.ui.state.activeName

/**
 * The active account on home: its colour, its name and its short address. It opens the list. A
 * screen reader hears what iOS says, the name and that it changes the account.
 */
@Composable
internal fun AccountPill(state: UiState, onOpen: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(
            enabled = state.busy == Busy.Idle,
            role = Role.Button,
            onClick = onOpen,
        ),
    ) {
        Avatar(state.account)
        Column(
            Modifier.clearAndSetSemantics {
                contentDescription = "${state.activeName}, change account"
            },
        ) {
            Text(
                state.activeName + "  ▾",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
            )
            Text(
                shortened(state.account),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Clip,
            )
        }
    }
}
