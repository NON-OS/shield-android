package systems.nonos.shield.ui.state

/** What an export put on screen: a title, the text, and whether it may be copied or drawn as QR. */
data class Exported(val title: String, val text: String, val words: Boolean)
