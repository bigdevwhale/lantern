package app.lantern.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import app.lantern.R
import app.lantern.domain.Kind

/**
 * A room at bedtime with a magic lantern on the table: near-black walls, warm lamp light, brass,
 * and coloured glass. Never bright, never blue-white: it's for the last half hour of the day.
 */
@Immutable
data class Palette(
    val bg: Color,
    val surface: Color,
    val surface2: Color,
    val line: Color,
    val ink: Color,
    val muted: Color,
    val faint: Color,
    val lamp: Color,
    val lampSoft: Color,
    val brass: Color,
    val brassDark: Color,
    val cut: Color,
    val danger: Color,
)

val Night = Palette(
    bg = Color(0xFF0D0A0E),
    surface = Color(0xFF17121A),
    surface2 = Color(0xFF211A24),
    line = Color(0xFF30272F),
    ink = Color(0xFFF1E4C8),
    muted = Color(0xFFB9A88A),
    faint = Color(0xFF7E705E),
    lamp = Color(0xFFF2B04F),
    lampSoft = Color(0xFF3A2914),
    brass = Color(0xFFC9A25A),
    brassDark = Color(0xFF6E5428),
    cut = Color(0xFF130B07),
    danger = Color(0xFFD0654F),
)

/** Each kind of slide is painted on its own colour of glass. */
fun Kind.glass(): Color = when (this) {
    Kind.HERO -> Color(0xFFE9A23B)
    Kind.PLACE -> Color(0xFF4C98B0)
    Kind.ITEM -> Color(0xFFC6B44C)
    Kind.TROUBLE -> Color(0xFFC9503F)
    Kind.HELPER -> Color(0xFF9479C8)
    Kind.QUIRK -> Color(0xFFDB7C93)
    Kind.TWIST -> Color(0xFF52AE80)
    Kind.ENDING -> Color(0xFF8DA3CC)
}

val LocalPalette = staticCompositionLocalOf { Night }

@OptIn(ExperimentalTextApi::class)
private fun alegreya(w: Int, italic: Boolean = false) = Font(
    if (italic) R.font.alegreya_italic else R.font.alegreya,
    FontWeight(w),
    if (italic) FontStyle.Italic else FontStyle.Normal,
    variationSettings = FontVariation.Settings(FontVariation.weight(w)),
)

/** Alegreya: made for long literary text, a little calligraphic. The voice of the tales. */
val Serif = FontFamily(alegreya(400), alegreya(500), alegreya(700), alegreya(800), alegreya(400, true), alegreya(500, true))

/** Alegreya Sans for the small print: labels, buttons, numbers. */
val Sans = FontFamily(
    Font(R.font.alegreya_sans, FontWeight.Normal),
    Font(R.font.alegreya_sans_medium, FontWeight.Medium),
    Font(R.font.alegreya_sans_bold, FontWeight.Bold),
)

object U {
    val c: Palette @Composable get() = LocalPalette.current

    val RXl = RoundedCornerShape(26.dp)
    val RLg = RoundedCornerShape(20.dp)
    val RMd = RoundedCornerShape(16.dp)
    val RSm = RoundedCornerShape(12.dp)
}

object UType {
    val Hero: TextStyle @Composable get() = TextStyle(fontFamily = Serif, fontWeight = FontWeight.Bold, fontSize = 34.sp, lineHeight = 38.sp, color = U.c.ink)
    val Display: TextStyle @Composable get() = TextStyle(fontFamily = Serif, fontWeight = FontWeight.Bold, fontSize = 27.sp, lineHeight = 32.sp, color = U.c.ink)
    val Title: TextStyle @Composable get() = TextStyle(fontFamily = Serif, fontWeight = FontWeight.Bold, fontSize = 21.sp, lineHeight = 26.sp, color = U.c.ink)
    val Name: TextStyle @Composable get() = TextStyle(fontFamily = Serif, fontWeight = FontWeight.Medium, fontSize = 19.sp, lineHeight = 23.sp, color = U.c.ink)
    val Tale: TextStyle @Composable get() = TextStyle(fontFamily = Serif, fontSize = 19.sp, lineHeight = 27.sp, color = U.c.ink)
    val Line: TextStyle @Composable get() = TextStyle(fontFamily = Serif, fontStyle = FontStyle.Italic, fontSize = 16.sp, lineHeight = 21.sp, color = U.c.muted)
    val Sub: TextStyle @Composable get() = TextStyle(fontFamily = Sans, fontSize = 15.5.sp, lineHeight = 21.sp, color = U.c.muted)
    val Eyebrow: TextStyle @Composable get() = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Bold, fontSize = 11.5.sp, letterSpacing = 0.16.em, color = U.c.faint)
    val Strong: TextStyle @Composable get() = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Bold, fontSize = 16.5.sp, color = U.c.ink)
    val Body: TextStyle @Composable get() = TextStyle(fontFamily = Sans, fontSize = 16.sp, lineHeight = 22.sp, color = U.c.ink)
    val Muted: TextStyle @Composable get() = TextStyle(fontFamily = Sans, fontSize = 14.sp, lineHeight = 19.sp, color = U.c.muted)
    val Small: TextStyle @Composable get() = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Medium, fontSize = 12.5.sp, color = U.c.faint)
    val Number: TextStyle @Composable get() = TextStyle(fontFamily = Serif, fontWeight = FontWeight.Bold, fontSize = 26.sp, color = U.c.ink, fontFeatureSettings = "lnum")
    val Button: TextStyle @Composable get() = TextStyle(fontFamily = Sans, fontWeight = FontWeight.Bold, fontSize = 16.sp)
}

@Composable
fun LanternTheme(content: @Composable () -> Unit) {
    val p = Night
    CompositionLocalProvider(LocalPalette provides p) {
        MaterialTheme(
            colorScheme = darkColorScheme(
                primary = p.lamp,
                onPrimary = p.cut,
                primaryContainer = p.lampSoft,
                onPrimaryContainer = p.ink,
                secondary = p.brass,
                background = p.bg,
                surface = p.surface,
                surfaceContainer = p.surface,
                surfaceContainerHigh = p.surface2,
                surfaceContainerHighest = p.surface2,
                surfaceContainerLow = p.surface,
                onBackground = p.ink,
                onSurface = p.ink,
                onSurfaceVariant = p.muted,
                outline = p.line,
                outlineVariant = p.line,
                error = p.danger,
            ),
            typography = MaterialTheme.typography.copy(
                bodyLarge = UType.Body,
                bodyMedium = UType.Body.copy(fontSize = 15.sp),
                labelLarge = UType.Button,
            ),
            content = content,
        )
    }
}
