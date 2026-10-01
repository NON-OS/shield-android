package systems.nonos.shield.ui.state

import systems.nonos.shield.core.ProveException

/**
 * Why a proof stopped, in one sentence each, the same as on iOS. Nothing is sent after any of
 * them, and most clear on a second try.
 */
internal fun proving(source: ProveException): String = when (source) {
    is ProveException.SeedEntropy -> "This phone gave no randomness for the proof. Try again."
    is ProveException.SeedReused -> "A proof needs fresh randomness. Try again."
    is ProveException.WitnessUnsatisfied ->
        "The notes could not be proved against the pool. Refresh, then try again."
    is ProveException.Unbalanced -> "The amounts do not add up. Nothing was sent."
    is ProveException.SelfVerify ->
        "The proof did not check out on this phone. Nothing was sent. Try again."
    is ProveException.Cancelled -> "Proving was stopped. Nothing was sent."
    is ProveException.Encoding ->
        "The proof could not be packed for the relayer. Nothing was sent."
    is ProveException.RootNotPublished -> "The pool moved while the proof was made. Try again."
}
