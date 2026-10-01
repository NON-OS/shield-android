package systems.nonos.shield.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * The palette, from the brand book at nonos.software/brand. Technical Blue is left out, since it
 * marks engineering material and never the product. Surfaces, text, lines and error come from
 * design-tokens.json, blended flat on ink, so iOS, Android and the site match.
 */
internal val Cyan = Color(0xFF66FFFF)
internal val Ink = Color(0xFF0A0B0D)
internal val DeepTeal = Color(0xFF2E5C5C)
internal val Paper = Color(0xFFF2F3F7)

/** A card or a field, which is ink with a little light on it. */
internal val Surface = Color(0xFF101215)

/** The same, one step further up, for a field inside a card. */
internal val SurfaceHigh = Color(0x14FFFFFF)

/** A rule that divides without shouting. */
internal val Line = Color(0xFF1F2022)

/** Control outlines, the tokens' line-2. */
internal val Line2 = Color(0xFF2F3032)

/** Secondary text, the tokens' text-2. */
internal val OnInkSecondary = Color(0xFFA3A4A7)

/** A failed check and a read error, the tokens' bad, used for nothing else. */
internal val Bad = Color(0xFFFF8A8A)

/** Text on ink, and the same at the weight a caption wants. */
internal val OnInk = Color(0xFFF2F3F7)
internal val OnInkMuted = Color(0xFF87888B)
