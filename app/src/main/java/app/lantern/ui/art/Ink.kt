package app.lantern.ui.art

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * A shadow-puppet cut-out, as Lotte Reiniger made them: one colour of card, with holes cut through
 * it for an eye or a bit of lace. Built once on a 100 × 100 grid (the ground is at y = 92, figures
 * face right) and drawn at any size.
 */
class Figure(build: Ink.() -> Unit) {
    internal val ops: List<Op> = Ink().apply(build).ops

    internal class Op(val path: Path, val stroke: Float, val hole: Boolean)
}

/** The scissors. Every shape is in grid units. */
class Ink {
    internal val ops = mutableListOf<Figure.Op>()
    private var cutting = false
    private var dx = 0f
    private var dy = 0f
    private var k = 1f
    private var flip = false

    private fun x(v: Float) = if (flip) dx - v * k else dx + v * k
    private fun y(v: Float) = dy + v * k
    private fun p(xy: FloatArray): List<Offset> = xy.toList().chunked(2).map { Offset(x(it[0]), y(it[1])) }

    private fun add(path: Path, stroke: Float = 0f) { ops += Figure.Op(path, stroke * k, cutting) }

    /** What's drawn inside is cut out of the card instead. */
    fun hole(block: Ink.() -> Unit) {
        val was = cutting
        cutting = true
        block()
        cutting = was
    }

    /** Draws a piece moved, scaled and maybe mirrored, so a hat or a lantern can be reused. */
    fun at(x: Float, y: Float, scale: Float = 1f, mirror: Boolean = false, block: Ink.() -> Unit) {
        val s = arrayOf(dx, dy, k, flip)
        dx = x(x); dy = y(y); k *= scale; flip = flip xor mirror
        block()
        dx = s[0] as Float; dy = s[1] as Float; k = s[2] as Float; flip = s[3] as Boolean
    }

    /**
     * Another figure: its point ([px], [py]) goes to ([x], [y]) here, scaled and turned [rot]
     * degrees about it. That's how a hat lands on a head and a lantern in a paw.
     */
    fun put(f: Figure, x: Float = 0f, y: Float = 0f, scale: Float = 1f, mirror: Boolean = false, rot: Float = 0f, px: Float = 0f, py: Float = 0f) {
        val ox = x(x); val oy = y(y); val s = k * scale
        val matrix = androidx.compose.ui.graphics.Matrix().apply {
            translate(ox, oy)
            if (flip) scale(-1f, 1f)
            if (rot != 0f) rotateZ(rot)
            if (mirror) scale(-1f, 1f)
            scale(s, s)
            translate(-px, -py)
        }
        f.ops.forEach { op ->
            val path = Path().apply { addPath(op.path); transform(matrix) }
            ops += Figure.Op(path, op.stroke * s, op.hole || cutting)
        }
    }

    /** A smooth closed shape through the points: bodies, heads, clouds. */
    fun blob(vararg xy: Float) = add(Path().apply { smooth(p(xy), closed = true) })

    /** A closed shape with straight sides: roofs, ears, hats. */
    fun poly(vararg xy: Float) = add(Path().apply {
        val pts = p(xy)
        moveTo(pts[0].x, pts[0].y)
        pts.drop(1).forEach { lineTo(it.x, it.y) }
        close()
    })

    fun oval(cx: Float, cy: Float, rx: Float, ry: Float, deg: Float = 0f) {
        if (deg == 0f) {
            add(Path().apply { addOval(Rect(x(cx) - rx * k, y(cy) - ry * k, x(cx) + rx * k, y(cy) + ry * k)) })
            return
        }
        val pts = (0 until 24).flatMap { i ->
            val a = Math.PI * 2 * i / 24
            val r = Math.toRadians(deg.toDouble())
            val ex = rx * cos(a); val ey = ry * sin(a)
            listOf((cx + ex * cos(r) - ey * sin(r)).toFloat(), (cy + ex * sin(r) + ey * cos(r)).toFloat())
        }.toFloatArray()
        blob(*pts)
    }

    fun circle(cx: Float, cy: Float, r: Float) = oval(cx, cy, r, r)

    fun rect(l: Float, t: Float, r: Float, b: Float, round: Float = 0f) {
        val a = x(l); val c = x(r)
        add(Path().apply { addRoundRect(RoundRect(minOf(a, c), y(t), maxOf(a, c), y(b), CornerRadius(round * k))) })
    }

    /** A rope of even width through the points, smooth, with round ends: legs, stalks, whiskers. */
    fun tube(w: Float, vararg xy: Float) = add(Path().apply { smooth(p(xy), closed = false) }, w)

    /** Like [tube] but with straight joints. */
    fun line(w: Float, vararg xy: Float) = add(Path().apply {
        val pts = p(xy)
        moveTo(pts[0].x, pts[0].y)
        pts.drop(1).forEach { lineTo(it.x, it.y) }
    }, w)

    /** A smooth stroke that narrows from [w0] to [w1]: tails, horns, flames, leaves, feathers. */
    fun taper(w0: Float, w1: Float, vararg xy: Float) {
        val line = sample(p(xy), 12)
        if (line.size < 2) return
        val left = ArrayList<Offset>()
        val right = ArrayList<Offset>()
        line.forEachIndexed { i, o ->
            val a = line[maxOf(0, i - 1)]
            val b = line[minOf(line.lastIndex, i + 1)]
            val ang = atan2(b.y - a.y, b.x - a.x)
            val w = (w0 + (w1 - w0) * i / line.lastIndex) * k / 2
            val n = Offset(-sin(ang) * w, cos(ang) * w)
            left += o + n
            right += o - n
        }
        add(Path().apply {
            moveTo(left[0].x, left[0].y)
            left.drop(1).forEach { lineTo(it.x, it.y) }
            right.asReversed().forEach { lineTo(it.x, it.y) }
            close()
        })
        // A round start so the root of a tail doesn't show a hard edge.
        if (w0 > 0.6f) add(Path().apply { val o = line[0]; addOval(Rect(o.x - w0 * k / 2, o.y - w0 * k / 2, o.x + w0 * k / 2, o.y + w0 * k / 2)) })
    }

    /** An SVG path in grid units, for the odd shape that's easier to write that way. */
    fun svg(d: String) {
        val path = PathParser().parsePathString(d).toPath()
        path.transform(androidx.compose.ui.graphics.Matrix().apply {
            translate(dx, dy)
            scale(if (flip) -k else k, k)
        })
        add(path)
    }

    /** A five-pointed star, for the sky and the odd wand. */
    fun star(cx: Float, cy: Float, r: Float, points: Int = 5, inner: Float = 0.45f) {
        val xy = (0 until points * 2).flatMap { i ->
            val a = -Math.PI / 2 + Math.PI * i / points
            val rr = if (i % 2 == 0) r else r * inner
            listOf((cx + rr * cos(a)).toFloat(), (cy + rr * sin(a)).toFloat())
        }.toFloatArray()
        poly(*xy)
    }

    private companion object {
        fun Path.smooth(pts: List<Offset>, closed: Boolean) {
            if (pts.size < 3) {
                moveTo(pts[0].x, pts[0].y); pts.drop(1).forEach { lineTo(it.x, it.y) }
                return
            }
            val n = pts.size
            fun at(i: Int) = if (closed) pts[(i + n) % n] else pts[i.coerceIn(0, n - 1)]
            moveTo(pts[0].x, pts[0].y)
            val last = if (closed) n else n - 1
            for (i in 0 until last) {
                val p0 = at(i - 1); val p1 = at(i); val p2 = at(i + 1); val p3 = at(i + 2)
                val c1 = p1 + (p2 - p0) / 6f
                val c2 = p2 - (p3 - p1) / 6f
                cubicTo(c1.x, c1.y, c2.x, c2.y, p2.x, p2.y)
            }
            if (closed) close()
        }

        /** Points along the smooth open curve through [pts], [per] per segment. */
        fun sample(pts: List<Offset>, per: Int): List<Offset> {
            if (pts.size < 3) {
                if (pts.size < 2) return pts
                return (0..per).map { pts[0] + (pts[1] - pts[0]) * (it / per.toFloat()) }
            }
            val n = pts.size
            fun at(i: Int) = pts[i.coerceIn(0, n - 1)]
            val out = ArrayList<Offset>()
            for (i in 0 until n - 1) {
                val p0 = at(i - 1); val p1 = at(i); val p2 = at(i + 1); val p3 = at(i + 2)
                for (s in 0 until per) {
                    val t = s / per.toFloat()
                    val t2 = t * t; val t3 = t2 * t
                    out += (p1 * 2f + (p2 - p0) * t + (p0 * 2f - p1 * 5f + p2 * 4f - p3) * t2 + (p1 * 3f - p0 - p2 * 3f + p3) * t3) * 0.5f
                }
            }
            out += pts.last()
            return out.filterIndexed { i, o -> i == 0 || hypot(o.x - out[i - 1].x, o.y - out[i - 1].y) > 0.01f }
        }
    }
}

/**
 * Draws [f] in [color] with its top-left at [at], [size] pixels across the 100-unit grid.
 * The holes are cut on their own layer, so the glass behind shows through them.
 */
fun DrawScope.figure(f: Figure, color: Color, at: Offset, size: Float, mirror: Boolean = false) {
    val s = size / 100f
    val bounds = Rect(at.x - size * 0.3f, at.y - size * 0.3f, at.x + size * 1.3f, at.y + size * 1.3f)
    // The see-through goes on the whole card at once, so overlapping pieces don't show darker.
    drawContext.canvas.saveLayer(bounds, Paint().apply { alpha = color.alpha })
    val solid = color.copy(alpha = 1f)
    withTransform({
        translate(at.x, at.y)
        if (mirror) { translate(size, 0f); scale(-s, s, Offset.Zero) } else scale(s, s, Offset.Zero)
    }) {
        f.ops.forEach { op ->
            val style = if (op.stroke > 0f) Stroke(op.stroke, cap = StrokeCap.Round, join = StrokeJoin.Round) else Fill
            drawPath(op.path, if (op.hole) Color.Black else solid, style = style, blendMode = if (op.hole) BlendMode.Clear else BlendMode.SrcOver)
        }
    }
    drawContext.canvas.restore()
}

/** Same, but centred on [center]. */
fun DrawScope.figureAround(f: Figure, color: Color, center: Offset, size: Float, mirror: Boolean = false) =
    figure(f, color, center - Offset(size / 2, size / 2), size, mirror)
