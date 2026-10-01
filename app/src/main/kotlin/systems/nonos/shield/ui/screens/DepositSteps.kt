package systems.nonos.shield.ui.screens

import androidx.compose.runtime.MutableState
import systems.nonos.shield.core.Coin

/** What a deposit is being written up as, kept by the deposit screen across its steps. */
internal class DepositDraft(val coin: MutableState<Coin>, val amount: MutableState<String>)

/** What follows a mined approval, and what ends the deposit. */
internal class ShieldSteps(val next: () -> Unit, val done: () -> Unit)
