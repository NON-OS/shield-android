package systems.nonos.shield.ui.state

import systems.nonos.shield.core.AccountSummary
import systems.nonos.shield.core.Balance
import systems.nonos.shield.core.BenchReport
import systems.nonos.shield.core.ChainSummary
import systems.nonos.shield.core.DepositTicket
import systems.nonos.shield.core.Idle
import systems.nonos.shield.core.Network
import systems.nonos.shield.core.PublicBalances
import systems.nonos.shield.core.PublicReview
import systems.nonos.shield.core.PublicSent
import systems.nonos.shield.core.PublicShield
import systems.nonos.shield.core.PublicSwap
import systems.nonos.shield.core.RelayHandoff
import systems.nonos.shield.core.RelayState
import systems.nonos.shield.core.SendStatus
import systems.nonos.shield.core.SpendTicket
import systems.nonos.shield.core.WatchedSummary

/** Everything a screen renders, as one immutable value. The shell derives nothing else. */
data class UiState(
    val stage: Stage = Stage.Opening,
    /** The shielded balance, one per coin, from this device's own notes. */
    val balances: List<Balance> = emptyList(),
    val network: Network = Network.SEPOLIA,
    /** The public `0x` account, and what it holds on each network read so far. */
    val account: String = "",
    val accounts: Map<Network, PublicBalances> = emptyMap(),
    /** A public send worked out and waiting for a yes, then what was sent. */
    val review: PublicReview? = null,
    val sent: PublicSent? = null,
    val sentStatus: SendStatus? = null,
    /** A swap, or the approval it needs first, waiting for a yes. */
    val swap: PublicSwap? = null,
    val relay: RelayHandoff? = null,
    val relayState: RelayState? = null,
    val selfSettle: PublicShield? = null,
    val notLanded: Boolean = false,
    val phrase: List<String> = emptyList(),
    /** The pool address other wallets pay to, with the spend and post-quantum keys. */
    val receiving: String = "",
    val chain: ChainSummary? = null,
    val deposit: DepositTicket? = null,
    val spend: SpendTicket? = null,
    val bench: BenchReport? = null,
    val busy: Busy = Busy.Idle,
    val failure: String? = null,
    val strongBox: Boolean = false,
    val userAuth: Boolean = false,
    val deviceSecure: Boolean = true,
    val cardPath: String? = null,
    val copied: Boolean = false,
    val idle: Idle = Idle.FiveMinutes,
    val accountList: List<AccountSummary> = emptyList(),
    val watched: List<WatchedSummary> = emptyList(),
    /** A key or the words on screen after the owner was confirmed, cleared on leaving. */
    val exported: Exported? = null,
    val ownTarget: UInt? = null,
    val arrivedRecently: Boolean = false,
    val searched: UInt = 0u,
    val recordingAllowed: Boolean = false,
    val shielding: PublicShield? = null,
    val fromKey: Boolean = false,
    /** A spend refused because its notes are too new, waiting for the owner to go early or not. */
    val tooSoon: SpendOrder? = null,
    /** An unused address for the next withdrawal, and whether a typed one was used. */
    val freshAddress: String? = null,
    val usedAddress: Boolean = false,
)
