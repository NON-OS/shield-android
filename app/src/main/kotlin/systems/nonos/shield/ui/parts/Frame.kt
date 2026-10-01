package systems.nonos.shield.ui.parts

/**
 * What every screen's frame needs to know: which screen it is, and whether the core refused
 * something that has not been read yet. They are one thing, the frame's own state, and the status
 * line is the same on every screen of a run.
 */
internal data class Frame(
    val heading: Heading,
    val failure: String?,
    val status: List<String>,
    /** The section's photograph, drawn above the content and never under it. */
    val backdrop: Backdrop? = null,
    val onDismissFailure: () -> Unit,
)

/** This frame with the section's photograph above its content. */
internal fun Frame.over(backdrop: Backdrop): Frame = copy(backdrop = backdrop)
