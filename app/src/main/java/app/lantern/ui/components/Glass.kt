package app.lantern.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import app.lantern.ui.art.Figure
import app.lantern.ui.art.figure
import app.lantern.ui.theme.Night
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * A round slide of painted glass in a brass ring, with its cut-out held against the light.
 * Plain drawing, so the widget and the book can use it too. [light] dims it (0..1); [halo] is
 * the glow the lamp throws around it.
 */
fun DrawScope.glass(
    fig: Figure?,
    color: Color,
    center: Offset,
    radius: Float,
    light: Float = 1f,
    halo: Float = 0f,
    mirror: Boolean = false,
    seed: Int = 0,
    cut: Color = Night.cut,
    rim: Boolean = true,
) {
    if (halo > 0f) drawCircle(
        Brush.radialGradient(listOf(color.copy(alpha = 0.32f * halo), color.copy(alpha = 0.10f * halo), Color.Transparent), center, radius * 1.55f),
        radius * 1.55f, center,
    )
    val lit = lerp(Color.Black, color, light.coerceIn(0f, 1f))
    val hot = lerp(lit, Color(0xFFFFF4D6), 0.38f * light)
    val deep = lerp(lit, Color.Black, 0.62f)
    drawCircle(
        Brush.radialGradient(listOf(hot, lit, deep), center + Offset(-radius * 0.18f, -radius * 0.22f), radius * 1.25f),
        radius, center,
    )
    val circle = Path().apply { addOval(androidx.compose.ui.geometry.Rect(center, radius)) }
    clipPath(circle) {
        // The painter's brush left the colour a little uneven.
        val rnd = Random(seed * 31 + color.hashCode())
        repeat(26) {
            val a = rnd.nextDouble() * Math.PI * 2
            val d = rnd.nextDouble() * radius * 0.9
            val p = center + Offset((cos(a) * d).toFloat(), (sin(a) * d).toFloat())
            drawCircle((if (rnd.nextBoolean()) Color.White else Color.Black).copy(alpha = 0.035f * light), radius * (0.08f + rnd.nextFloat() * 0.18f), p)
        }
        if (fig != null) {
            val size = radius * 2f * 0.9f
            figure(fig, cut.copy(alpha = 0.94f), center - Offset(size / 2, size / 2), size, mirror)
        }
        drawCircle(Brush.radialGradient(listOf(Color.Transparent, Color.Transparent, Color.Black.copy(alpha = 0.5f)), center, radius), radius, center)
    }
    if (!rim) return
    val w = radius * 0.075f
    drawCircle(Brush.sweepGradient(listOf(Night.brass, Night.brassDark, Night.brass, Color(0xFFE6C887), Night.brass), center), radius + w / 2, center, style = Stroke(w))
    drawCircle(Color.Black.copy(alpha = 0.55f), radius, center, style = Stroke(radius * 0.012f))
    drawArc(
        Color.White.copy(alpha = 0.16f * light), 200f, 60f, false,
        center - Offset(radius * 0.8f, radius * 0.8f), Size(radius * 1.6f, radius * 1.6f), style = Stroke(radius * 0.035f),
    )
}

/** A slide on screen, its lamp flickering a little as a flame would. */
@Composable
fun Glass(fig: Figure?, color: Color, size: Dp, modifier: Modifier = Modifier, light: Float = 1f, halo: Boolean = true, mirror: Boolean = false, flicker: Boolean = true, seed: Int = 0) {
    val f = if (flicker && !app.lantern.BuildConfig.SHOTS) {
        val t = rememberInfiniteTransition(label = "flame")
        val v by t.animateFloat(0.93f, 1f, infiniteRepeatable(tween(1700 + seed % 5 * 230, easing = LinearEasing), RepeatMode.Reverse), label = "flicker")
        v
    } else 1f
    Canvas(modifier.size(size)) {
        val r = this.size.minDimension / 2 / (if (halo) 1.3f else 1.06f)
        glass(fig, color, center, r, light * f, if (halo) light * f else 0f, mirror, seed)
    }
}
