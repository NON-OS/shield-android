package systems.nonos.shield.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import systems.nonos.shield.R

/** Geist for what you read, JetBrains Mono for what you check, both under the SIL OFL. */
internal val Sans = FontFamily(
    Font(R.font.geist_regular, FontWeight.Normal),
    Font(R.font.geist_medium, FontWeight.Medium),
    Font(R.font.geist_semibold, FontWeight.SemiBold),
)

internal val Mono = FontFamily(Font(R.font.jetbrains_mono_regular, FontWeight.Normal))

/* Sizes run a quarter above the first drawn scale, in sp so the reader's setting scales them. */
internal val ShieldTypography = Typography(
    /* The balance, in sans with tabular figures, since the mono dotted zero reads as a symbol. */
    displayLarge = TextStyle(
        fontFamily = Sans,
        fontWeight = FontWeight.Medium,
        fontSize = 55.sp,
        fontFeatureSettings = "tnum",
    ),
    displaySmall = TextStyle(
        fontFamily = Sans,
        fontWeight = FontWeight.Medium,
        fontSize = 42.sp,
        fontFeatureSettings = "tnum",
    ),
    /* A screen's statement, in mono, at a size where sentences do not wrap. */
    headlineSmall = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Normal, fontSize = 26.sp),
    titleLarge = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Medium, fontSize = 25.sp),
    titleMedium = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Medium, fontSize = 20.sp),
    titleSmall = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Medium, fontSize = 19.sp),
    bodyLarge = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Normal, fontSize = 20.sp),
    bodyMedium = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Normal, fontSize = 18.sp),
    bodySmall = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Normal, fontSize = 16.sp),
    labelLarge = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Medium, fontSize = 18.sp),
    labelSmall = TextStyle(fontFamily = Mono, fontWeight = FontWeight.Normal, fontSize = 15.sp),
)
