package systems.nonos.shield.ui.state

import systems.nonos.shield.core.CancelToken
import systems.nonos.shield.core.Machine

/**
 * The measurement screen's two actions. They exist in the shipped app on
 * purpose: the gate the product turns on is a proof timed on a real device, so
 * the number has to be obtainable from a phone in somebody's hand.
 */
internal fun WalletViewModel.measureProof() {
    val token = CancelToken()
    cancel = token
    // The measurement screen opens at once, so the run shows its bar and its clock.
    set { it.copy(stage = Stage.Measuring, bench = null, cardPath = null) }
    launchCore(Busy.Measuring) {
        val report = wallet?.benchProof(token)
        cancel = null
        /*
         * The card goes to a file as well as to the screen. A number whose
         * only witness is a screenshot is a number somebody has to take on
         * trust, and a run that leaves an artifact can be quoted and diffed
         * against the next one.
         */
        val card = report?.let { measured ->
            runCatching { wallet?.saveMeasurement(measured, Machine.describe(context)) }
                .getOrNull()
        }
        set {
            it.copy(bench = report, cardPath = card, stage = Stage.Measuring, busy = Busy.Idle)
        }
    }
}
