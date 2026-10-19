package app.lantern.ui.art

import kotlin.math.cos
import kotlin.math.sin

/** Signs for what happens: a storm, a lost moon, a door that wasn't there. Drawn big in the middle. */
object Glyphs {
    private fun Ink.cloud(cx: Float, cy: Float, s: Float) {
        circle(cx - 12f * s, cy + 2f * s, 9f * s)
        circle(cx, cy - 4f * s, 13f * s)
        circle(cx + 13f * s, cy + 2f * s, 9f * s)
        rect(cx - 20f * s, cy + 2f * s, cx + 21f * s, cy + 11f * s, 5f * s)
    }

    private fun Ink.drop(x: Float, y: Float, s: Float) = blob(x, y - 5f * s, x + 3f * s, y + 1f * s, x, y + 4f * s, x - 3f * s, y + 1f * s)

    private fun Ink.arc(cx: Float, cy: Float, r: Float, from: Double, to: Double, w: Float) =
        tube(w, *(0..16).flatMap { j -> val a = Math.toRadians(from + (to - from) * j / 16); listOf(cx + r * cos(a).toFloat(), cy + r * sin(a).toFloat()) }.toFloatArray())

    val crescent = Figure {
        circle(50f, 50f, 28f)
        hole { circle(62f, 42f, 24f) }
        hole { circle(36f, 60f, 2f) }
        listOf(78f to 22f, 84f to 66f, 70f to 84f).forEach { (x, y) -> star(x, y, 3f, 4, 0.4f) }
    }

    val noMoon = Figure {
        tube(1.8f, *(0..24).flatMap { j -> if (j % 2 == 0) emptyList() else { val a = Math.PI * 2 * j / 24; listOf(50f + 24f * cos(a).toFloat(), 46f + 24f * sin(a).toFloat()) } }.toFloatArray())
        (0..11).forEach { j -> val a = Math.PI * 2 * j / 12; circle(50f + 24f * cos(a).toFloat(), 46f + 24f * sin(a).toFloat(), 1.8f) }
        blob(44f, 46f, 46f, 36f, 54f, 34f, 58f, 40f, 54f, 46f, 52f, 50f, 50f, 54f)
        circle(50f, 60f, 2.4f)
        listOf(20f to 20f, 80f to 24f, 22f to 80f, 82f to 80f).forEach { (x, y) -> star(x, y, 2.4f, 4, 0.4f) }
    }

    val sun = Figure {
        circle(50f, 50f, 16f)
        (0..11).forEach { i -> val a = Math.PI / 6 * i; taper(4f, 0.8f, 50f + 21f * cos(a).toFloat(), 50f + 21f * sin(a).toFloat(), 50f + 32f * cos(a).toFloat(), 50f + 32f * sin(a).toFloat()) }
        hole { circle(44f, 46f, 1.8f); circle(56f, 46f, 1.8f); tube(1.4f, 43f, 55f, 50f, 59f, 57f, 55f) }
    }

    val rain = Figure {
        cloud(50f, 38f, 1.3f)
        listOf(34f to 70f, 46f to 78f, 58f to 70f, 70f to 80f, 40f to 88f, 64f to 92f).forEach { (x, y) -> drop(x, y, 1.3f) }
    }

    val storm = Figure {
        cloud(50f, 34f, 1.35f)
        poly(52f, 50f, 40f, 70f, 50f, 70f, 44f, 92f, 64f, 64f, 54f, 64f, 60f, 50f)
    }

    val snowflake = Figure {
        (0..5).forEach { i ->
            val a = Math.PI / 3 * i
            val ex = 50f + 32f * cos(a).toFloat(); val ey = 50f + 32f * sin(a).toFloat()
            line(3f, 50f, 50f, ex, ey)
            listOf(0.55f, 0.8f).forEach { t ->
                val mx = 50f + 32f * t * cos(a).toFloat(); val my = 50f + 32f * t * sin(a).toFloat()
                listOf(-0.6, 0.6).forEach { d -> line(2.4f, mx, my, mx + 8f * cos(a + d).toFloat(), my + 8f * sin(a + d).toFloat()) }
            }
        }
        circle(50f, 50f, 5f)
        hole { circle(50f, 50f, 2f) }
    }

    val shootingStar = Figure {
        star(68f, 34f, 13f)
        taper(1f, 9f, 14f, 84f, 30f, 66f, 48f, 50f, 60f, 40f)
        hole { taper(0.5f, 3f, 24f, 76f, 38f, 62f, 52f, 48f) }
        star(24f, 30f, 2.4f, 4); star(80f, 70f, 2f, 4)
    }

    val wind = Figure {
        tube(3f, 10f, 40f, 40f, 40f, 60f, 38f, 68f, 28f, 62f, 20f, 54f, 24f, 56f, 32f)
        tube(3f, 16f, 54f, 60f, 54f, 78f, 52f, 86f, 42f, 80f, 34f, 72f, 38f)
        tube(3f, 10f, 68f, 46f, 68f, 62f, 72f, 66f, 82f, 58f, 88f, 52f, 82f)
        put(Beasts.crow.art, 40f, 24f, 0.2f, rot = -20f, px = 50f, py = 60f)
    }

    val door = Figure {
        blob(28f, 88f, 28f, 36f, 34f, 20f, 50f, 12f, 66f, 20f, 72f, 36f, 72f, 88f)
        hole { blob(33f, 88f, 33f, 38f, 38f, 25f, 50f, 18f, 62f, 25f, 67f, 38f, 67f, 88f) }
        poly(33f, 88f, 33f, 38f, 46f, 30f, 46f, 94f)
        hole { circle(42f, 60f, 1.8f); line(0.9f, 36f, 50f, 43f, 46f); line(0.9f, 36f, 74f, 43f, 74f) }
        listOf(56f to 40f, 60f to 60f, 52f to 74f).forEach { (x, y) -> star(x, y, 2.4f, 4, 0.4f) }
        rect(18f, 88f, 82f, 92f, 1f)
        poly(46f, 92f, 67f, 88f, 92f, 100f, 40f, 100f)
    }

    val footprints = Figure {
        listOf(Triple(30f, 84f, -20f), Triple(46f, 70f, 10f), Triple(42f, 50f, -16f), Triple(60f, 36f, 14f), Triple(56f, 16f, -12f)).forEachIndexed { i, (x, y, r) ->
            val s = if (i % 2 == 0) 1f else 0.95f
            oval(x, y, 4.6f * s, 6.6f * s, r)
            listOf(-4.4f, -1.4f, 1.6f, 4.4f).forEachIndexed { j, dx -> circle(x + dx * s + r * 0.04f, y - 9f * s + (if (j == 0 || j == 3) 1.6f else 0f), 1.6f * s) }
        }
    }

    val heart = Figure {
        blob(50f, 82f, 24f, 56f, 20f, 38f, 28f, 26f, 40f, 24f, 50f, 34f, 60f, 24f, 72f, 26f, 80f, 38f, 76f, 56f)
        hole { tube(1.6f, 32f, 38f, 34f, 32f, 40f, 30f) }
        star(84f, 20f, 3f, 4); star(18f, 70f, 2.4f, 4)
    }

    val brokenHeart = Figure {
        blob(50f, 82f, 24f, 56f, 20f, 38f, 28f, 26f, 40f, 24f, 50f, 34f, 60f, 24f, 72f, 26f, 80f, 38f, 76f, 56f)
        hole { line(3f, 50f, 34f, 44f, 48f, 54f, 56f, 46f, 68f, 50f, 82f) }
    }

    val notes = Figure {
        line(2.6f, 36f, 70f, 36f, 26f)
        line(2.6f, 66f, 62f, 66f, 18f)
        poly(36f, 26f, 66f, 18f, 66f, 28f, 36f, 36f)
        oval(31f, 72f, 7f, 5f, -20f)
        oval(61f, 64f, 7f, 5f, -20f)
        tube(2.4f, 78f, 86f, 78f, 50f, 86f, 56f)
        oval(74f, 86f, 5f, 3.6f, -20f)
    }

    val zzz = Figure {
        listOf(Triple(24f, 70f, 1f), Triple(48f, 46f, 1.3f), Triple(70f, 22f, 0.9f)).forEach { (x, y, s) ->
            line(3.4f * s, x, y, x + 14f * s, y, x, y + 14f * s, x + 14f * s, y + 14f * s)
        }
        circle(80f, 70f, 6f)
        hole { circle(83f, 68f, 5f) }
    }

    val tornado = Figure {
        (0..6).forEach { i ->
            val y = 18f + i * 10f
            val w = 34f - i * 4.4f
            val x = 50f + sin(i * 1.1).toFloat() * 4f
            tube(4.4f, x - w, y, x - w * 0.3f, y + 2f, x + w * 0.4f, y + 1f, x + w, y - 1f)
        }
        put(Beasts.mouse.art, 74f, 30f, 0.18f, rot = 40f, px = 50f, py = 70f)
        put(Wardrobe.umbrella, 22f, 34f, 0.3f, rot = -30f, px = 50f, py = 36f)
    }

    val ghost = Figure {
        blob(30f, 84f, 30f, 44f, 36f, 26f, 50f, 18f, 64f, 26f, 70f, 44f, 70f, 84f, 64f, 78f, 58f, 86f, 50f, 78f, 42f, 86f, 36f, 78f)
        taper(5f, 2f, 32f, 50f, 22f, 56f, 18f, 50f)
        taper(5f, 2f, 68f, 50f, 78f, 46f, 82f, 40f)
        hole { oval(43f, 42f, 3f, 4f); oval(57f, 42f, 3f, 4f); oval(50f, 56f, 4f, 3f) }
    }

    val web = Figure {
        (0..7).forEach { i -> val a = Math.PI / 4 * i; line(1.2f, 50f, 50f, 50f + 40f * cos(a).toFloat(), 50f + 40f * sin(a).toFloat()) }
        listOf(10f, 20f, 30f, 38f).forEach { r ->
            tube(1.1f, *(0..8).flatMap { j -> val a = Math.PI / 4 * j; listOf(50f + r * cos(a).toFloat(), 50f + r * sin(a).toFloat()) }.toFloatArray())
        }
        line(0.8f, 66f, 50f, 66f, 66f)
        oval(66f, 70f, 4f, 5f); circle(66f, 64f, 2.6f)
        listOf(-1f, 1f).forEach { s -> (0..2).forEach { k -> line(0.8f, 66f, 68f + k * 2f, 66f + s * 7f, 64f + k * 4f) } }
    }

    val fog = Figure {
        listOf(28f, 42f, 56f, 70f).forEachIndexed { i, y ->
            tube(5f, 10f + (i % 2) * 8f, y, 30f, y - 3f, 50f, y + 1f, 70f, y - 3f, 90f - (i % 2) * 6f, y)
        }
        hole { listOf(28f, 56f).forEach { y -> line(1f, 30f, y - 2.6f, 60f, y - 1f) } }
        put(Places.forest, 50f, 92f, 0.4f, px = 50f, py = 92f)
    }

    val bubbles = Figure {
        listOf(Triple(40f, 60f, 16f), Triple(66f, 36f, 11f), Triple(30f, 26f, 7f), Triple(72f, 72f, 7f), Triple(56f, 14f, 4f)).forEach { (x, y, r) ->
            circle(x, y, r)
            hole { circle(x, y, r * 0.84f) }
            tube(r * 0.14f, x - r * 0.5f, y - r * 0.2f, x - r * 0.3f, y - r * 0.5f)
        }
    }

    val fire = Figure {
        blob(50f, 10f, 58f, 30f, 70f, 40f, 72f, 62f, 62f, 80f, 50f, 84f, 36f, 80f, 28f, 62f, 32f, 44f, 40f, 50f, 42f, 32f)
        hole { blob(50f, 46f, 56f, 58f, 58f, 70f, 50f, 78f, 42f, 70f, 44f, 58f) }
        line(4f, 30f, 86f, 70f, 92f); line(4f, 30f, 92f, 70f, 86f)
    }

    val mushroom = Figure {
        blob(18f, 50f, 22f, 32f, 38f, 20f, 62f, 20f, 78f, 32f, 82f, 50f, 50f, 54f)
        blob(42f, 50f, 58f, 50f, 60f, 84f, 40f, 84f)
        hole { circle(36f, 34f, 4f); circle(56f, 28f, 3f); circle(68f, 40f, 3.4f); circle(48f, 42f, 2.4f) }
        rect(20f, 84f, 80f, 88f, 2f)
    }

    val leaf = Figure {
        blob(20f, 80f, 24f, 50f, 40f, 28f, 64f, 18f, 84f, 16f, 80f, 40f, 66f, 62f, 44f, 76f)
        hole { tube(1.6f, 24f, 78f, 44f, 56f, 62f, 36f, 80f, 20f); listOf(0.3f, 0.5f, 0.7f).forEach { t -> val x = 24f + 56f * t; val y = 78f - 58f * t; line(1f, x, y, x + 10f, y + 6f); line(1f, x, y, x - 6f, y - 10f) } }
        line(2f, 20f, 80f, 12f, 88f)
    }

    val eye = Figure {
        blob(10f, 50f, 30f, 32f, 50f, 26f, 70f, 32f, 90f, 50f, 70f, 68f, 50f, 74f, 30f, 68f)
        hole { blob(16f, 50f, 32f, 36f, 50f, 31f, 68f, 36f, 84f, 50f, 68f, 64f, 50f, 69f, 32f, 64f) }
        circle(50f, 50f, 13f)
        hole { circle(46f, 46f, 3.4f) }
        (0..6).forEach { i -> val a = Math.PI * (1.1 + i * 0.13); line(1.6f, 50f + 26f * cos(a).toFloat(), 52f + 26f * sin(a).toFloat(), 50f + 33f * cos(a).toFloat(), 52f + 33f * sin(a).toFloat()) }
    }

    val sparkles = Figure {
        star(50f, 50f, 20f, 4, 0.28f)
        star(24f, 28f, 9f, 4, 0.3f)
        star(76f, 30f, 7f, 4, 0.3f)
        star(28f, 76f, 7f, 4, 0.3f)
        star(76f, 74f, 10f, 4, 0.3f)
        listOf(40f to 18f, 86f to 52f, 14f to 52f, 56f to 86f).forEach { (x, y) -> circle(x, y, 1.6f) }
    }

    val question = Figure {
        tube(7f, 34f, 34f, 38f, 22f, 50f, 16f, 62f, 20f, 66f, 32f, 60f, 42f, 50f, 50f, 50f, 62f)
        circle(50f, 78f, 5f)
    }

    val upsideDown = Figure {
        arc(50f, 50f, 28f, -160.0, 10.0, 5f)
        arc(50f, 50f, 28f, 20.0, 190.0, 5f)
        poly(78f, 56f, 84f, 42f, 70f, 46f)
        poly(22f, 44f, 16f, 58f, 30f, 54f)
        put(Beasts.cat.art, 50f, 50f, 0.38f, rot = 180f, px = 50f, py = 60f)
    }

    val bigShadow = Figure {
        blob(10f, 100f, 14f, 60f, 24f, 34f, 40f, 20f, 60f, 18f, 76f, 30f, 86f, 60f, 92f, 100f)
        hole { oval(40f, 44f, 5f, 3f, -10f); oval(62f, 44f, 5f, 3f, 10f) }
        poly(24f, 34f, 18f, 10f, 34f, 24f)
        poly(76f, 30f, 84f, 8f, 68f, 22f)
        put(Beasts.mouse.art, 50f, 92f, 0.2f, px = 50f, py = 92f)
        hole { put(Beasts.mouse.art, 50f, 92f, 0.2f, px = 50f, py = 92f) }
    }

    val tears = Figure {
        circle(50f, 46f, 26f)
        hole {
            tube(2f, 36f, 40f, 40f, 38f, 44f, 40f)
            tube(2f, 56f, 40f, 60f, 38f, 64f, 40f)
            tube(2f, 40f, 62f, 50f, 56f, 60f, 62f)
        }
        drop(38f, 80f, 1.6f); drop(64f, 86f, 1.4f); drop(30f, 92f, 1f)
    }

    val sneeze = Figure {
        circle(36f, 50f, 20f)
        hole { tube(2f, 26f, 44f, 32f, 46f); tube(2f, 40f, 44f, 46f, 46f); oval(36f, 58f, 4f, 3f) }
        listOf(Triple(64f, 42f, 3f), Triple(74f, 34f, 2.4f), Triple(70f, 54f, 2.6f), Triple(84f, 46f, 2f), Triple(82f, 62f, 1.6f), Triple(90f, 30f, 1.4f)).forEach { (x, y, r) -> circle(x, y, r) }
        line(2f, 60f, 50f, 70f, 50f); line(2f, 60f, 44f, 68f, 40f); line(2f, 60f, 56f, 68f, 60f)
    }

    val gift = Figure {
        rect(24f, 44f, 76f, 84f, 1.6f)
        rect(20f, 36f, 80f, 46f, 1.6f)
        hole { rect(46f, 36f, 54f, 84f); line(1f, 24f, 50f, 76f, 50f) }
        rect(47.6f, 36f, 52.4f, 84f)
        blob(50f, 36f, 38f, 22f, 30f, 28f, 36f, 36f)
        blob(50f, 36f, 62f, 22f, 70f, 28f, 64f, 36f)
        star(84f, 22f, 3f, 4); star(16f, 30f, 2.4f, 4)
    }

    val cake = Figure {
        rect(24f, 56f, 76f, 82f, 2f)
        rect(30f, 38f, 70f, 56f, 2f)
        oval(50f, 84f, 32f, 4f)
        hole { tube(1.6f, 24f, 64f, 32f, 68f, 40f, 64f, 50f, 68f, 60f, 64f, 68f, 68f, 76f, 64f); tube(1.6f, 30f, 44f, 38f, 48f, 46f, 44f, 54f, 48f, 62f, 44f, 70f, 48f) }
        listOf(40f, 50f, 60f).forEach { x -> rect(x - 1.6f, 26f, x + 1.6f, 38f); taper(3f, 0.4f, x, 24f, x, 16f) }
    }

    val shrink = Figure {
        put(Beasts.hare.art, 50f, 60f, 0.26f, px = 50f, py = 60f)
        listOf(45.0, 135.0, 225.0, 315.0).forEach { d ->
            val a = Math.toRadians(d)
            val ox = 50f + 40f * cos(a).toFloat(); val oy = 50f + 40f * sin(a).toFloat()
            val ix = 50f + 22f * cos(a).toFloat(); val iy = 50f + 22f * sin(a).toFloat()
            line(3f, ox, oy, ix, iy)
            poly(ix, iy, ix + 8f * cos(a + 0.5).toFloat(), iy + 8f * sin(a + 0.5).toFloat(), ix + 8f * cos(a - 0.5).toFloat(), iy + 8f * sin(a - 0.5).toFloat())
        }
    }

    val grow = Figure {
        put(Beasts.mouse.art, 50f, 58f, 0.62f, px = 50f, py = 70f)
        listOf(45.0, 135.0, 225.0, 315.0).forEach { d ->
            val a = Math.toRadians(d)
            val ox = 50f + 44f * cos(a).toFloat(); val oy = 50f + 44f * sin(a).toFloat()
            val ix = 50f + 30f * cos(a).toFloat(); val iy = 50f + 30f * sin(a).toFloat()
            line(3f, ix, iy, ox, oy)
            poly(ox, oy, ox - 8f * cos(a + 0.5).toFloat(), oy - 8f * sin(a + 0.5).toFloat(), ox - 8f * cos(a - 0.5).toFloat(), oy - 8f * sin(a - 0.5).toFloat())
        }
    }

    val invisible = Figure {
        put(Beasts.fox.art, 50f, 50f, 1f, px = 50f, py = 50f)
        hole { (0..24).forEach { i -> rect(0f, 4f * i + 1.6f, 100f, 4f * i + 4f) } }
        listOf(20f to 20f, 84f to 24f, 86f to 70f).forEach { (x, y) -> star(x, y, 2.4f, 4, 0.4f) }
    }

    val house = Figure {
        rect(28f, 50f, 72f, 84f)
        poly(22f, 52f, 50f, 24f, 78f, 52f)
        rect(60f, 28f, 66f, 42f)
        hole { blob(44f, 84f, 44f, 68f, 50f, 64f, 56f, 68f, 56f, 84f); rect(32f, 58f, 40f, 66f, 1f); rect(60f, 58f, 68f, 66f, 1f) }
        tube(1.6f, 63f, 24f, 60f, 18f, 64f, 12f, 61f, 6f)
        rect(14f, 84f, 86f, 88f, 1f)
        moon(82f, 22f)
    }

    private fun Ink.moon(x: Float, y: Float) {
        circle(x, y, 6f)
        hole { circle(x + 3f, y - 1.4f, 5f) }
    }

    val bed = Figure {
        rect(14f, 56f, 86f, 70f, 2f)
        rect(12f, 40f, 18f, 82f, 2f)
        rect(82f, 50f, 88f, 82f, 2f)
        blob(20f, 56f, 22f, 48f, 34f, 46f, 36f, 56f)
        blob(36f, 58f, 40f, 48f, 60f, 46f, 80f, 50f, 82f, 58f)
        hole { tube(1.2f, 40f, 62f, 60f, 64f, 80f, 62f) }
        listOf(Triple(50f, 30f, 1f), Triple(62f, 22f, 0.8f), Triple(72f, 14f, 0.6f)).forEach { (x, y, s) -> line(2.4f * s, x, y, x + 8f * s, y, x, y + 8f * s, x + 8f * s, y + 8f * s) }
    }

    val echo = Figure {
        put(Places.mountain, 50f, 50f, 1f, px = 50f, py = 50f)
        hole { listOf(10f, 16f, 22f).forEach { r -> tube(1.4f, *(0..8).flatMap { j -> val a = Math.toRadians(-50.0 + 100.0 * j / 8); listOf(30f + r * cos(a).toFloat(), 74f + r * sin(a).toFloat()) }.toFloatArray()) } }
    }

    val all: Map<String, Figure> by lazy {
        linkedMapOf(
            "crescent" to crescent, "noMoon" to noMoon, "sun" to sun, "rain" to rain, "storm" to storm, "snowflake" to snowflake,
            "shootingStar" to shootingStar, "wind" to wind, "door" to door, "footprints" to footprints, "heart" to heart, "brokenHeart" to brokenHeart,
            "notes" to notes, "zzz" to zzz, "tornado" to tornado, "ghost" to ghost, "web" to web, "fog" to fog, "bubbles" to bubbles, "fire" to fire,
            "mushroom" to mushroom, "leaf" to leaf, "eye" to eye, "sparkles" to sparkles, "question" to question, "upsideDown" to upsideDown,
            "bigShadow" to bigShadow, "tears" to tears, "sneeze" to sneeze, "gift" to gift, "cake" to cake, "shrink" to shrink, "grow" to grow,
            "invisible" to invisible, "house" to house, "bed" to bed, "echo" to echo,
        )
    }
}
