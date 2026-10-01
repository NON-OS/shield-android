package systems.nonos.shield.ui.state

import systems.nonos.shield.core.Recording

/** Whether the words or a key are on screen, which no recording or screenshot may hold. */
internal val UiState.secretShown: Boolean
    get() = stage == Stage.ShowingPhrase || stage == Stage.Restoring || exported != null

/** Whether the window may be captured now: allowed by the owner, and no secret showing. */
internal val UiState.captureAllowed: Boolean
    get() = recordingAllowed && !secretShown

/** Allow or hide screen recording. The words and exported keys stay hidden either way. */
internal fun WalletViewModel.toggleRecording() {
    val allowed = !current().recordingAllowed
    Recording.save(context, allowed)
    set { it.copy(recordingAllowed = allowed) }
}

internal fun WalletViewModel.readRecording(): Boolean = Recording.load(context)
