package systems.nonos.shield.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import systems.nonos.shield.core.Coin
import systems.nonos.shield.core.DepositTicket
import systems.nonos.shield.core.amountUnit
import systems.nonos.shield.ui.parts.Fact
import systems.nonos.shield.ui.parts.Facts
import systems.nonos.shield.ui.parts.Label
import systems.nonos.shield.ui.parts.Lead
import systems.nonos.shield.ui.parts.Secondary
import systems.nonos.shield.ui.parts.Value
import systems.nonos.shield.ui.state.WalletViewModel
import systems.nonos.shield.ui.state.copyValue

/**
 * What the pool said about a deposit. A refusal kept nothing. Otherwise the note is already on this
 * phone and two transactions remain, the approval then the deposit, each field copied on its own
 * for an EVM wallet.
 */
@Composable
internal fun DepositTicketView(
    ticket: DepositTicket,
    coin: Coin,
    model: WalletViewModel,
    onDone: () -> Unit,
) {
    val refusal = ticket.refusal
    if (refusal != null) {
        Lead(refusal)
    } else {
        val unit = amountUnit(coin)
        Facts {
            Fact("you send", "${ticket.amount} $unit")
            Fact("pool fee", "${ticket.fee} $unit")
            Fact("note holds", "${ticket.note} $unit")
        }
        if (ticket.approveTo.isEmpty()) {
            Step("Deposit", ticket.depositTo, ticket.depositData, model)
            Lead(
                "Send it from that address with a value of ${ticket.amount} ETH. Then read " +
                    "the pool; the note appears once it is in a block.",
            )
        } else {
            Step("1  Approve", ticket.approveTo, ticket.approveData, model)
            Step("2  Deposit", ticket.depositTo, ticket.depositData, model)
            Lead(
                "Send both from that address, value zero. Then read the pool and the note " +
                    "appears once the deposit is in a block.",
            )
        }
    }
    Secondary("Done") { onDone() }
}

@Composable
private fun Step(name: String, to: String, data: String, model: WalletViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        Label(name)
        Value(to)
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Secondary("Copy to", modifier = Modifier.weight(1f)) { model.copyValue(to) }
            Secondary("Copy data", modifier = Modifier.weight(1f)) { model.copyValue(data) }
        }
    }
}
