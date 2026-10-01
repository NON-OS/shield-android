package systems.nonos.shield.ui.parts

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * The corner, and it is nearly square, so surfaces that carry values read as panels. Two points
 * keeps a hairline border from aliasing.
 */
internal val EDGE = RoundedCornerShape(2.dp)

/** A border that is one device pixel wherever it is drawn. */
internal val HAIR = 1.dp
