package app.lantern.ui.art

import kotlin.math.cos
import kotlin.math.sin

/** Where it happens: whole scenes, cut to fill the round glass (keep the middle; the corners are lost). */
object Places {
    private fun Ink.ground(y: Float, bumps: Int = 5, amp: Float = 3f, seed: Int = 0) {
        val top = (0..bumps).flatMap { i ->
            val x = -4f + i * 108f / bumps
            listOf(x, y - amp * (if ((i + seed) % 2 == 0) 1f else -0.4f))
        }
        blob(*(floatArrayOf(-4f, 104f) + top.toFloatArray() + floatArrayOf(104f, 104f)))
    }

    private fun Ink.fir(x: Float, base: Float, h: Float) {
        val w = h * 0.42f
        poly(x, base - h, x + w * 0.45f, base - h * 0.6f, x + w * 0.25f, base - h * 0.6f, x + w * 0.7f, base - h * 0.28f, x + w * 0.4f, base - h * 0.28f, x + w, base,
            x - w, base, x - w * 0.4f, base - h * 0.28f, x - w * 0.7f, base - h * 0.28f, x - w * 0.25f, base - h * 0.6f, x - w * 0.45f, base - h * 0.6f)
        rect(x - h * 0.04f, base - 1f, x + h * 0.04f, base + h * 0.08f)
    }

    private fun Ink.roundTree(x: Float, base: Float, h: Float) {
        tube(h * 0.08f, x, base, x, base - h * 0.5f)
        blob(x - h * 0.3f, base - h * 0.5f, x - h * 0.34f, base - h * 0.78f, x, base - h, x + h * 0.34f, base - h * 0.78f, x + h * 0.3f, base - h * 0.5f, x, base - h * 0.42f)
    }

    private fun Ink.house(x: Float, base: Float, w: Float, h: Float, lit: Boolean = true) {
        rect(x - w / 2, base - h, x + w / 2, base)
        poly(x - w / 2 - 2f, base - h, x, base - h - w * 0.6f, x + w / 2 + 2f, base - h)
        rect(x + w * 0.18f, base - h - w * 0.5f, x + w * 0.34f, base - h - w * 0.1f)
        if (lit) hole { rect(x - w * 0.3f, base - h * 0.7f, x - w * 0.05f, base - h * 0.4f); rect(x + w * 0.08f, base - h * 0.7f, x + w * 0.32f, base - h * 0.4f) }
    }

    private fun Ink.stars(vararg xy: Float) = xy.toList().chunked(2).forEachIndexed { i, (x, y) -> star(x, y, if (i % 3 == 0) 2.2f else 1.4f, 4, 0.4f) }

    private fun Ink.moon(x: Float, y: Float, r: Float) {
        circle(x, y, r)
        hole { circle(x + r * 0.45f, y - r * 0.2f, r * 0.85f) }
    }

    private fun Ink.waves(y: Float, amp: Float = 2.4f) {
        val top = (0..12).flatMap { i -> listOf(-4f + i * 9f, y + (if (i % 2 == 0) -amp else amp)) }
        blob(*(floatArrayOf(-4f, 104f) + top.toFloatArray() + floatArrayOf(104f, 104f)))
    }

    val forest = Figure {
        ground(84f, 6, 2f)
        listOf(14f to 40f, 28f to 52f, 44f to 44f, 58f to 58f, 74f to 46f, 88f to 54f).forEach { (x, h) -> fir(x, 86f, h) }
        listOf(6f to 30f, 36f to 28f, 66f to 30f, 96f to 34f).forEach { (x, h) -> fir(x, 88f, h) }
        moon(70f, 18f, 6f)
        stars(30f, 14f, 46f, 10f, 84f, 30f, 20f, 26f)
        hole { circle(52f, 80f, 1f); circle(55f, 80f, 1f) }
    }

    val castle = Figure {
        blob(-4f, 104f, -4f, 80f, 20f, 74f, 50f, 70f, 80f, 74f, 104f, 80f, 104f, 104f)
        rect(30f, 44f, 70f, 74f)
        listOf(26f, 74f).forEach { x ->
            rect(x - 7f, 30f, x + 7f, 74f)
            poly(x - 9f, 30f, x, 12f, x + 9f, 30f)
            tube(0.8f, x, 12f, x, 6f)
            poly(x, 6f, x + 6f, 8f, x, 10f)
        }
        rect(44f, 30f, 56f, 46f)
        poly(42f, 30f, 50f, 18f, 58f, 30f)
        (0..4).forEach { i -> rect(30f + i * 8.6f, 40f, 34f + i * 8.6f, 45f) }
        hole {
            blob(45f, 74f, 45f, 62f, 50f, 57f, 55f, 62f, 55f, 74f)
            listOf(26f, 74f).forEach { x -> rect(x - 1.6f, 38f, x + 1.6f, 44f, 1.6f) }
            rect(48.4f, 34f, 51.6f, 40f, 1.6f)
            rect(34f, 52f, 37f, 57f, 1.4f); rect(63f, 52f, 66f, 57f, 1.4f)
        }
        stars(14f, 14f, 88f, 18f, 60f, 10f)
        tube(1.6f, 50f, 74f, 46f, 84f, 52f, 94f)
    }

    val lighthouse = Figure {
        waves(80f)
        blob(36f, 84f, 40f, 72f, 58f, 70f, 70f, 76f, 74f, 84f)
        poly(46f, 72f, 49f, 30f, 59f, 30f, 62f, 72f)
        rect(46f, 24f, 62f, 30f)
        poly(45f, 24f, 54f, 14f, 63f, 24f)
        circle(54f, 12f, 1.4f)
        hole {
            rect(48f, 25f, 60f, 29f)
            listOf(40f, 52f, 62f).forEach { y -> rect(51.6f, y, 56.4f, y + 4f, 2.4f) }
        }
        poly(60f, 26f, 100f, 14f, 100f, 34f)
        hole { poly(62f, 26f, 100f, 16f, 100f, 32f) }
        poly(64f, 26f, 100f, 20f, 100f, 28f)
        tube(1f, 16f, 48f, 20f, 46f, 24f, 48f)
        tube(1f, 24f, 40f, 27f, 38f, 30f, 40f)
        stars(20f, 20f, 34f, 12f, 80f, 48f)
    }

    val island = Figure {
        waves(82f, 1.8f)
        blob(24f, 84f, 32f, 74f, 50f, 70f, 70f, 74f, 78f, 84f)
        taper(4f, 2.4f, 52f, 74f, 50f, 56f, 54f, 36f, 60f, 26f)
        taper(1f, 7f, 60f, 26f, 70f, 22f, 82f, 28f)
        taper(1f, 7f, 60f, 26f, 72f, 30f, 78f, 40f)
        taper(1f, 7f, 60f, 26f, 50f, 20f, 38f, 24f)
        taper(1f, 7f, 60f, 26f, 48f, 30f, 42f, 40f)
        taper(1f, 6f, 60f, 26f, 62f, 14f, 70f, 10f)
        circle(57f, 30f, 2.4f); circle(61f, 31f, 2.4f)
        moon(22f, 20f, 7f)
        stars(80f, 50f, 34f, 46f, 88f, 14f)
        tube(1.4f, 70f, 54f, 74f, 50f, 78f, 54f)
        tube(1.4f, 76f, 46f, 80f, 42f, 84f, 46f)
    }

    val underwater = Figure {
        blob(-4f, 104f, -4f, 86f, 20f, 82f, 40f, 86f, 60f, 82f, 80f, 86f, 104f, 82f, 104f, 104f)
        listOf(14f to 50f, 24f to 36f, 78f to 46f, 88f to 32f).forEach { (x, h) ->
            taper(4f, 1f, x, 86f, x - 4f, 86f - h * 0.3f, x + 3f, 86f - h * 0.6f, x - 2f, 86f - h)
        }
        put(Beasts.fish.art, 50f, 50f, 0.42f, px = 50f, py = 60f)
        put(Beasts.fish.art, 66f, 30f, 0.26f, mirror = true, px = 50f, py = 60f)
        put(Treasures.shell, 46f, 82f, 0.18f, px = 50f, py = 52f)
        blob(56f, 86f, 60f, 76f, 70f, 74f, 74f, 86f)
        listOf(30f to 24f, 34f to 16f, 28f to 10f, 62f to 66f, 60f to 60f).forEachIndexed { i, (x, y) -> circle(x, y, 1.4f + i % 3); hole { circle(x - 0.3f, y - 0.3f, (1.4f + i % 3) * 0.6f) } }
        tube(0.8f, 0f, 6f, 20f, 4f, 40f, 6f, 60f, 4f, 80f, 6f, 100f, 4f)
    }

    val mountain = Figure {
        poly(-4f, 104f, -4f, 70f, 14f, 50f, 22f, 58f, 44f, 22f, 52f, 30f, 60f, 18f, 84f, 56f, 92f, 50f, 104f, 66f, 104f, 104f)
        hole {
            poly(44f, 26f, 48f, 32f, 44f, 36f, 40f, 32f)
            poly(60f, 22f, 64f, 30f, 60f, 34f, 56f, 29f)
            tube(1.2f, 34f, 70f, 40f, 66f, 46f, 72f, 52f, 68f, 58f, 74f)
        }
        line(1f, 60f, 18f, 60f, 6f)
        poly(60f, 6f, 68f, 8f, 60f, 11f)
        stars(20f, 20f, 84f, 20f, 30f, 10f)
        moon(82f, 34f, 4f)
    }

    val moonland = Figure {
        blob(-4f, 104f, -4f, 74f, 20f, 70f, 44f, 72f, 70f, 68f, 104f, 72f, 104f, 104f)
        hole { oval(24f, 80f, 8f, 2.4f); oval(66f, 84f, 10f, 2.6f); oval(46f, 92f, 6f, 1.8f) }
        circle(70f, 28f, 12f)
        hole { circle(70f, 28f, 10f) }
        blob(62f, 22f, 70f, 18f, 76f, 24f, 72f, 30f, 66f, 34f, 60f, 30f)
        blob(70f, 34f, 76f, 32f, 78f, 36f, 74f, 38f)
        rect(30f, 56f, 34f, 70f)
        poly(30f, 56f, 44f, 58f, 30f, 62f)
        stars(14f, 18f, 30f, 30f, 46f, 12f, 90f, 54f, 16f, 46f)
    }

    val cloudCity = Figure {
        blob(10f, 76f, 16f, 66f, 28f, 66f, 34f, 58f, 48f, 60f, 56f, 54f, 70f, 58f, 78f, 64f, 90f, 66f, 94f, 76f, 80f, 84f, 20f, 84f)
        rect(30f, 44f, 40f, 66f); poly(28f, 44f, 35f, 34f, 42f, 44f)
        rect(46f, 30f, 56f, 62f); poly(44f, 30f, 51f, 16f, 58f, 30f)
        rect(62f, 46f, 72f, 64f); blob(62f, 46f, 67f, 38f, 72f, 46f)
        hole {
            rect(33f, 50f, 37f, 56f, 2f); rect(49f, 36f, 53f, 42f, 2f); rect(49f, 48f, 53f, 54f, 2f); rect(65f, 52f, 69f, 58f, 2f)
            tube(1.2f, 20f, 76f, 40f, 78f, 60f, 76f, 80f, 78f)
        }
        tube(0.8f, 51f, 16f, 51f, 10f); poly(51f, 10f, 57f, 12f, 51f, 14f)
        blob(70f, 22f, 76f, 18f, 84f, 20f, 88f, 26f, 72f, 28f)
        blob(12f, 34f, 18f, 30f, 26f, 32f, 28f, 38f, 12f, 38f)
        stars(20f, 16f, 86f, 40f)
    }

    val mill = Figure {
        ground(80f, 5, 3f, 1)
        poly(44f, 80f, 48f, 44f, 60f, 44f, 64f, 80f)
        poly(46f, 44f, 54f, 34f, 62f, 44f)
        hole { blob(51f, 80f, 51f, 70f, 54f, 66f, 57f, 70f, 57f, 80f); rect(52f, 52f, 56f, 57f, 1f) }
        listOf(30f, 120f, 210f, 300f).forEach { d ->
            val a = Math.toRadians(d.toDouble())
            val ex = 54f + 30f * cos(a).toFloat(); val ey = 42f + 30f * sin(a).toFloat()
            line(1.6f, 54f, 42f, ex, ey)
            val nx = -sin(a).toFloat() * 6f; val ny = cos(a).toFloat() * 6f
            val mx = 54f + 12f * cos(a).toFloat(); val my = 42f + 12f * sin(a).toFloat()
            poly(mx, my, ex, ey, ex + nx, ey + ny, mx + nx, my + ny)
        }
        circle(54f, 42f, 2.6f)
        roundTree(20f, 82f, 24f)
        roundTree(84f, 82f, 18f)
        moon(18f, 20f, 5f)
    }

    val village = Figure {
        ground(82f, 6, 2f, 1)
        house(20f, 82f, 18f, 16f)
        house(46f, 82f, 22f, 22f)
        house(76f, 82f, 18f, 14f)
        fir(62f, 84f, 26f)
        roundTree(6f, 84f, 20f)
        roundTree(94f, 84f, 22f)
        moon(30f, 22f, 6f)
        stars(60f, 14f, 80f, 26f, 12f, 30f)
        tube(1f, 56f, 32f, 54f, 26f, 58f, 20f, 56f, 14f)
    }

    val library = Figure {
        rect(-4f, 86f, 104f, 104f)
        listOf(8f, 72f).forEach { x ->
            rect(x, 14f, x + 22f, 86f)
            hole {
                (0..4).forEach { r ->
                    val y = 18f + r * 13.4f
                    var bx = x + 2f
                    var i = 0
                    while (bx < x + 19f) {
                        val w = 2f + (r * 7 + i * 3) % 3
                        rect(bx, y + (i % 2) * 1.5f, bx + w - 0.6f, y + 11f)
                        bx += w; i++
                    }
                }
            }
        }
        rect(40f, 30f, 60f, 32f)
        line(1f, 50f, 32f, 50f, 50f)
        poly(42f, 50f, 50f, 46f, 58f, 50f, 50f, 56f)
        circle(50f, 56f, 1.8f)
        put(Wardrobe.book, 50f, 80f, 0.6f, px = 50f, py = 60f)
        line(1.6f, 40f, 72f, 60f, 72f)
        line(1.6f, 44f, 72f, 42f, 86f); line(1.6f, 56f, 72f, 58f, 86f)
        put(Wardrobe.candle, 58f, 70f, 0.4f, px = 50f, py = 72f)
        line(2f, 66f, 86f, 66f, 20f)
        (0..6).forEach { i -> line(1.6f, 62f, 24f + i * 9f, 70f, 24f + i * 9f) }
        line(2f, 70f, 86f, 70f, 20f)
    }

    val grannyHouse = Figure {
        rect(-4f, 84f, 104f, 104f)
        rect(30f, 30f, 70f, 56f, 1f)
        hole { rect(32f, 32f, 68f, 54f); }
        line(1.6f, 50f, 30f, 50f, 56f); line(1.6f, 30f, 43f, 70f, 43f)
        moon(60f, 38f, 4f)
        hole { }
        blob(22f, 30f, 30f, 28f, 32f, 60f, 26f, 62f)
        blob(78f, 30f, 70f, 28f, 68f, 60f, 74f, 62f)
        rect(22f, 26f, 78f, 29f, 1f)
        rect(36f, 62f, 64f, 66f, 1f)
        line(2f, 40f, 66f, 40f, 84f); line(2f, 60f, 66f, 60f, 84f)
        put(Treasures.teapot, 50f, 58f, 0.2f, px = 50f, py = 72f)
        blob(74f, 84f, 74f, 62f, 80f, 56f, 88f, 56f, 92f, 62f, 92f, 72f, 96f, 84f)
        line(1.6f, 80f, 56f, 80f, 44f, 86f, 40f)
        put(Treasures.yarn, 16f, 80f, 0.16f, px = 46f, py = 54f)
        put(Beasts.cat.art, 14f, 74f, 0.18f, px = 50f, py = 92f)
    }

    val icePalace = Figure {
        rect(-4f, 82f, 104f, 104f)
        listOf(30f to 40f, 70f to 40f, 50f to 22f, 18f to 56f, 82f to 56f).forEach { (x, top) ->
            poly(x - 6f, 82f, x - 6f, top + 10f, x, top, x + 6f, top + 10f, x + 6f, 82f)
        }
        rect(24f, 56f, 76f, 82f)
        hole {
            blob(44f, 82f, 44f, 70f, 50f, 64f, 56f, 70f, 56f, 82f)
            listOf(30f, 70f, 50f).forEach { x -> poly(x - 1.6f, 50f, x, 46f, x + 1.6f, 50f, x + 1.6f, 56f, x - 1.6f, 56f) }
            (0..5).forEach { i -> poly(26f + i * 9f, 58f, 30f + i * 9f, 58f, 28f + i * 9f, 62f) }
        }
        listOf(14f to 20f, 86f to 16f, 34f to 12f, 70f to 14f).forEach { (x, y) -> star(x, y, 3f, 6, 0.3f) }
    }

    val desert = Figure {
        blob(-4f, 104f, -4f, 82f, 20f, 78f, 40f, 82f, 60f, 78f, 80f, 82f, 104f, 78f, 104f, 104f)
        poly(14f, 80f, 34f, 46f, 54f, 80f)
        poly(44f, 80f, 60f, 54f, 76f, 80f)
        hole { line(1f, 34f, 46f, 40f, 80f); line(1f, 60f, 54f, 64f, 80f) }
        put(Beasts.camel.art, 86f, 78f, 0.22f, px = 50f, py = 92f)
        circle(78f, 24f, 9f)
        stars(20f, 20f, 40f, 12f, 52f, 26f)
        taper(2.4f, 1.6f, 6f, 82f, 6f, 66f)
        tube(1.6f, 6f, 72f, 2f, 70f, 2f, 64f)
    }

    val jungle = Figure {
        ground(86f, 5, 2f)
        listOf(-2f to 18f, 102f to 22f).forEach { (x, y) -> taper(5f, 3f, x, 104f, x + (if (x < 50f) 6f else -6f), 50f, x + (if (x < 50f) 10f else -10f), y) }
        listOf(
            floatArrayOf(4f, 20f, 20f, 12f, 36f, 18f), floatArrayOf(6f, 24f, 18f, 36f, 26f, 44f), floatArrayOf(96f, 24f, 80f, 14f, 66f, 20f),
            floatArrayOf(94f, 28f, 82f, 40f, 74f, 50f), floatArrayOf(10f, 18f, 4f, 6f, 12f, 0f), floatArrayOf(90f, 24f, 98f, 10f, 92f, 2f),
        ).forEach { taper(1f, 9f, *it) }
        tube(1f, 40f, 0f, 42f, 20f, 38f, 36f, 40f, 48f)
        tube(1f, 62f, 0f, 60f, 16f, 64f, 30f)
        put(Beasts.crocodile.art, 54f, 86f, 0.5f, px = 50f, py = 86f)
        listOf(30f to 80f, 70f to 78f, 20f to 70f).forEach { (x, y) -> taper(1f, 7f, x, y + 8f, x - 4f, y) ; taper(1f, 7f, x, y + 8f, x + 5f, y + 1f) }
        put(Beasts.bat.art, 60f, 34f, 0.18f, px = 50f, py = 50f)
    }

    val swamp = Figure {
        waves(84f, 1.2f)
        listOf(12f, 18f, 24f, 78f, 84f, 90f).forEachIndexed { i, x ->
            val top = 46f + (i % 3) * 8f
            line(1.2f, x, 86f, x + (i % 2) * 2f, top)
            oval(x + (i % 2) * 2f, top + 4f, 1.8f, 5f)
        }
        oval(50f, 82f, 14f, 3f)
        hole { poly(50f, 82f, 64f, 79f, 64f, 83f) }
        put(Beasts.frog.art, 48f, 80f, 0.3f, px = 50f, py = 90f)
        listOf(30f to 30f, 70f to 26f, 58f to 40f, 38f to 46f).forEach { (x, y) -> circle(x, y, 1.6f); hole { circle(x, y, 0.6f) } }
        tube(1f, 0f, 40f, 10f, 38f, 18f, 44f, 30f, 42f)
        moon(80f, 16f, 6f)
        put(Beasts.snail.art, 70f, 82f, 0.12f, px = 50f, py = 92f)
    }

    val cave = Figure {
        blob(-4f, -4f, 104f, -4f, 104f, 104f, -4f, 104f)
        hole {
            blob(10f, 86f, 12f, 50f, 24f, 26f, 44f, 16f, 66f, 18f, 82f, 30f, 90f, 54f, 90f, 86f)
        }
        (0..7).forEach { i -> val x = 22f + i * 8f; poly(x - 3f, 18f + (i % 3) * 3f, x + 3f, 18f + (i % 3) * 3f, x, 30f + (i * 7 % 11)) }
        listOf(28f, 52f, 70f).forEachIndexed { i, x -> poly(x - 4f, 86f, x, 70f - i * 4f, x + 4f, 86f) }
        rect(-4f, 84f, 104f, 104f)
        put(Treasures.chest, 50f, 80f, 0.3f, px = 50f, py = 82f)
        listOf(40f to 50f, 60f to 46f, 48f to 40f, 66f to 60f).forEach { (x, y) -> star(x, y, 1.4f, 4, 0.4f) }
    }

    val nightTrain = Figure {
        rect(-4f, 86f, 104f, 89f)
        (0..10).forEach { i -> rect(-2f + i * 10f, 89f, 4f + i * 10f, 92f) }
        rect(6f, 60f, 38f, 82f, 2f)
        rect(42f, 58f, 70f, 82f, 2f)
        rect(70f, 50f, 84f, 82f, 1f)
        rect(66f, 46f, 88f, 50f, 1f)
        blob(84f, 62f, 94f, 64f, 96f, 82f, 84f, 82f)
        rect(88f, 52f, 92f, 62f)
        listOf(14f, 30f, 50f, 62f, 76f, 90f).forEach { x -> circle(x, 84f, 4f) }
        line(1.4f, 38f, 76f, 42f, 76f)
        hole {
            listOf(10f, 20f, 30f).forEach { x -> rect(x - 1f, 64f, x + 5f, 70f, 1f) }
            listOf(46f, 56f).forEach { x -> rect(x, 62f, x + 8f, 70f, 1f) }
            rect(73f, 54f, 81f, 62f, 1f)
            listOf(14f, 30f, 50f, 62f, 76f, 90f).forEach { x -> circle(x, 84f, 1.4f) }
        }
        listOf(90f to 44f, 84f to 34f, 76f to 26f, 66f to 20f).forEachIndexed { i, (x, y) -> circle(x, y, 3f + i * 1.4f) }
        moon(24f, 24f, 7f)
        stars(46f, 14f, 40f, 32f, 10f, 40f, 94f, 16f)
    }

    val ship = Figure {
        waves(82f, 2f)
        poly(14f, 64f, 86f, 64f, 78f, 82f, 24f, 82f)
        poly(70f, 64f, 70f, 58f, 88f, 58f, 86f, 64f)
        line(2f, 50f, 64f, 50f, 8f)
        line(1.6f, 30f, 64f, 30f, 24f)
        poly(52f, 12f, 76f, 16f, 74f, 36f, 52f, 40f)
        poly(52f, 42f, 80f, 44f, 78f, 60f, 52f, 60f)
        poly(28f, 28f, 14f, 32f, 14f, 54f, 28f, 58f)
        poly(50f, 8f, 60f, 4f, 50f, 2f)
        hole { listOf(30f, 42f, 54f, 66f).forEach { x -> circle(x, 72f, 2f) }; poly(60f, 48f, 66f, 46f, 70f, 52f, 64f, 56f) }
        line(1f, 50f, 8f, 88f, 58f)
        line(1f, 30f, 24f, 14f, 64f)
        moon(16f, 14f, 5f)
    }

    val balloonSky = Figure {
        blob(30f, 26f, 40f, 10f, 60f, 6f, 76f, 14f, 78f, 32f, 66f, 50f, 54f, 52f, 42f, 46f)
        hole { listOf(42f, 54f, 66f).forEach { x -> tube(1.2f, x, 8f, x - 2f, 28f, x + 1f, 48f) } }
        line(1f, 46f, 50f, 50f, 64f); line(1f, 62f, 50f, 60f, 64f)
        rect(48f, 64f, 62f, 74f, 1f)
        hole { line(0.8f, 49f, 68f, 61f, 68f) }
        blob(-4f, 104f, -4f, 92f, 10f, 86f, 22f, 90f, 30f, 84f, 44f, 88f, 56f, 84f, 70f, 90f, 82f, 84f, 96f, 88f, 104f, 86f, 104f, 104f)
        blob(8f, 60f, 12f, 54f, 20f, 54f, 24f, 60f)
        blob(80f, 60f, 86f, 52f, 96f, 56f, 98f, 62f)
        put(Beasts.crow.art, 22f, 36f, 0.16f, px = 50f, py = 60f)
        stars(84f, 26f, 14f, 20f)
    }

    val giantGarden = Figure {
        ground(88f, 5, 2f)
        listOf(Triple(24f, 30f, 10f), Triple(54f, 18f, 13f), Triple(82f, 36f, 9f)).forEach { (x, y, r) ->
            taper(3f, 2f, x, 90f, x - 2f, (90f + y) / 2f, x, y)
            (0..7).forEach { i -> val a = Math.PI / 4 * i; oval(x + r * cos(a).toFloat(), y + r * sin(a).toFloat(), r * 0.55f, r * 0.55f) }
            hole { circle(x, y, r * 0.5f) }
            circle(x, y, r * 0.3f)
            taper(1f, 8f, x - 1f, 74f, x - 10f, 68f, x - 16f, 70f)
        }
        put(Beasts.bee.art, 38f, 46f, 0.16f, px = 50f, py = 60f)
        put(Beasts.snail.art, 40f, 88f, 0.12f, px = 50f, py = 92f)
        put(Beasts.mouse.art, 68f, 88f, 0.14f, px = 50f, py = 92f)
    }

    val circus = Figure {
        rect(-4f, 86f, 104f, 104f)
        poly(14f, 86f, 20f, 50f, 50f, 22f, 80f, 50f, 86f, 86f)
        line(1.4f, 50f, 22f, 50f, 8f)
        poly(50f, 8f, 60f, 11f, 50f, 14f)
        hole {
            (0..4).forEach { i -> val x = 26f + i * 12f; poly(x - 2.4f, 52f + kotlin.math.abs(2 - i) * 4f, x + 2.4f, 52f + kotlin.math.abs(2 - i) * 4f, 50f + (x - 50f) * 0.1f, 26f) }
            poly(42f, 86f, 50f, 64f, 58f, 86f)
        }
        listOf(20f, 30f, 40f, 50f, 60f, 70f, 80f).forEach { x -> circle(x, 50f + kotlin.math.abs(x - 50f) * 0.05f, 4f) }
        put(Treasures.balloon, 92f, 50f, 0.3f, px = 50f, py = 50f)
        put(Treasures.balloon, 8f, 46f, 0.26f, px = 50f, py = 50f)
        stars(30f, 16f, 74f, 20f)
    }

    val clockTower = Figure {
        ground(86f, 5, 1.6f)
        rect(38f, 30f, 62f, 86f)
        poly(36f, 30f, 50f, 8f, 64f, 30f)
        circle(50f, 42f, 9f)
        hole { circle(50f, 42f, 7f); blob(44f, 86f, 44f, 74f, 50f, 68f, 56f, 74f, 56f, 86f); rect(46f, 56f, 54f, 62f, 3f) }
        line(1.2f, 50f, 42f, 50f, 37f); line(1.2f, 50f, 42f, 54f, 44f)
        (0..11).forEach { i -> val a = Math.PI / 6 * i; circle(50f + 5.6f * cos(a).toFloat(), 42f + 5.6f * sin(a).toFloat(), 0.5f) }
        house(18f, 86f, 16f, 14f)
        house(82f, 86f, 16f, 18f)
        moon(80f, 18f, 5f)
        stars(20f, 22f, 30f, 10f)
    }

    val bridge = Figure {
        waves(86f, 1.4f)
        ground(70f, 2, 1f)
        hole { rect(-4f, 72f, 104f, 84f) }
        blob(-4f, 104f, -4f, 66f, 10f, 64f, 12f, 104f)
        blob(104f, 104f, 104f, 66f, 90f, 64f, 88f, 104f)
        rect(6f, 56f, 94f, 60f)
        hole { listOf(28f, 50f, 72f).forEach { x -> oval(x, 70f, 9f, 9f) } }
        rect(6f, 60f, 94f, 66f)
        listOf(16f, 39f, 61f, 84f).forEach { x -> rect(x - 2f, 60f, x + 2f, 84f) }
        (0..8).forEach { i -> line(1f, 10f + i * 10f, 56f, 10f + i * 10f, 48f) }
        line(1.4f, 8f, 48f, 92f, 48f)
        put(Beasts.fish.art, 40f, 90f, 0.14f, px = 50f, py = 60f)
        moon(50f, 20f, 7f)
        stars(20f, 24f, 80f, 14f, 70f, 34f)
    }

    val treehouse = Figure {
        ground(88f, 4, 1.6f)
        taper(12f, 7f, 50f, 90f, 48f, 60f, 52f, 40f)
        taper(4f, 1f, 50f, 58f, 34f, 48f, 24f, 50f)
        taper(4f, 1f, 52f, 46f, 70f, 38f, 80f, 40f)
        blob(16f, 40f, 20f, 18f, 40f, 6f, 64f, 8f, 84f, 20f, 88f, 40f, 72f, 46f, 50f, 40f, 30f, 46f)
        rect(36f, 44f, 64f, 62f)
        poly(32f, 44f, 50f, 32f, 68f, 44f)
        hole { rect(40f, 48f, 46f, 54f, 1f); rect(52f, 48f, 60f, 62f, 1f) }
        line(1f, 66f, 62f, 70f, 88f); line(1f, 72f, 62f, 76f, 88f)
        (0..4).forEach { i -> line(1f, 66.6f + i * 0.8f, 66f + i * 5f, 72.6f + i * 0.8f, 66f + i * 5f) }
        rect(30f, 62f, 70f, 64f)
    }

    val northPole = Figure {
        rect(-4f, 80f, 104f, 104f)
        blob(28f, 80f, 30f, 66f, 44f, 56f, 58f, 58f, 70f, 66f, 72f, 80f)
        hole {
            blob(44f, 80f, 44f, 72f, 50f, 68f, 56f, 72f, 56f, 80f)
            tube(0.9f, 32f, 70f, 50f, 64f, 68f, 70f)
            tube(0.9f, 30f, 76f, 70f, 76f)
            line(0.9f, 40f, 60f, 40f, 70f); line(0.9f, 58f, 61f, 58f, 70f)
        }
        put(Beasts.penguin.art, 84f, 80f, 0.22f, px = 50f, py = 92f)
        listOf(
            floatArrayOf(0f, 30f, 20f, 20f, 40f, 30f, 60f, 18f, 80f, 26f, 100f, 16f),
            floatArrayOf(0f, 40f, 24f, 32f, 50f, 40f, 76f, 30f, 100f, 36f),
        ).forEach { tube(1.6f, *it) }
        stars(14f, 12f, 50f, 8f, 88f, 46f, 30f, 50f)
    }

    val volcano = Figure {
        poly(-4f, 104f, -4f, 88f, 24f, 82f, 40f, 40f, 60f, 40f, 76f, 82f, 104f, 88f, 104f, 104f)
        hole { tube(1.6f, 46f, 42f, 44f, 54f, 48f, 64f, 44f, 78f) }
        listOf(36f to 24f, 50f to 14f, 62f to 26f).forEach { (x, y) -> blob(x - 6f, y + 6f, x - 4f, y, x, y - 4f, x + 6f, y, x + 6f, y + 6f) }
        listOf(30f to 34f, 70f to 32f, 50f to 30f).forEach { (x, y) -> taper(3f, 0.6f, 50f, 40f, x, y) }
        listOf(20f to 14f, 80f to 16f).forEach { (x, y) -> circle(x, y, 1.6f) }
        put(Beasts.dragon.art, 82f, 86f, 0.24f, mirror = true, px = 50f, py = 92f)
    }

    val rooftops = Figure {
        rect(-4f, 76f, 104f, 104f)
        poly(-4f, 76f, -4f, 54f, 10f, 44f, 24f, 54f, 24f, 76f)
        poly(22f, 76f, 22f, 60f, 40f, 48f, 58f, 60f, 58f, 76f)
        poly(56f, 76f, 56f, 50f, 70f, 40f, 84f, 50f, 84f, 76f)
        poly(82f, 76f, 82f, 58f, 94f, 52f, 104f, 58f, 104f, 76f)
        rect(30f, 42f, 36f, 56f); rect(66f, 34f, 72f, 46f)
        hole { listOf(6f to 62f, 14f to 62f, 34f to 64f, 44f to 64f, 64f to 58f, 74f to 58f, 90f to 64f).forEach { (x, y) -> rect(x, y, x + 5f, y + 6f, 0.6f) } }
        put(Beasts.cat.art, 72f, 40f, 0.18f, px = 50f, py = 92f)
        moon(40f, 20f, 9f)
        stars(14f, 18f, 66f, 14f, 88f, 26f, 24f, 34f)
        tube(1f, 33f, 40f, 30f, 34f, 34f, 28f)
    }

    val underBed = Figure {
        rect(-4f, 86f, 104f, 104f)
        rect(-4f, 10f, 104f, 26f)
        line(4f, 6f, 26f, 6f, 86f); line(4f, 94f, 26f, 94f, 86f)
        hole { (0..8).forEach { i -> line(1f, 12f + i * 9f, 22f, 16f + i * 9f, 14f) } }
        put(Treasures.button, 30f, 82f, 0.12f, px = 50f, py = 50f)
        put(Treasures.yarn, 72f, 80f, 0.16f, px = 46f, py = 54f)
        blob(40f, 86f, 42f, 74f, 50f, 70f, 56f, 76f, 54f, 86f)
        hole { circle(46f, 77f, 1.4f); circle(51f, 77f, 1.4f) }
        put(Treasures.boot, 20f, 86f, 0.2f, px = 50f, py = 80f)
        listOf(24f to 50f, 70f to 44f, 54f to 56f).forEach { (x, y) -> circle(x, y, 1f) }
        put(Beasts.mouse.art, 86f, 86f, 0.16f, mirror = true, px = 50f, py = 92f)
    }

    val teapotHouse = Figure {
        ground(86f, 5, 1.4f)
        put(Treasures.teapot, 50f, 58f, 1.1f, px = 50f, py = 58f)
        hole { blob(44f, 79f, 44f, 70f, 50f, 66f, 56f, 70f, 56f, 79f); circle(38f, 60f, 3f); circle(62f, 60f, 3f) }
        tube(1f, 90f, 16f, 86f, 10f, 90f, 4f)
        stars(14f, 18f, 30f, 10f, 74f, 12f)
        roundTree(12f, 88f, 22f)
    }

    val rainbow = Figure {
        ground(84f, 6, 2f)
        (0..3).forEach { i ->
            val r = 40f - i * 5f
            tube(3f, *(0..20).flatMap { j -> val a = Math.PI + Math.PI * j / 20; listOf(50f + r * cos(a).toFloat(), 82f + r * 0.95f * sin(a).toFloat()) }.toFloatArray())
        }
        blob(2f, 84f, 4f, 74f, 14f, 72f, 20f, 80f)
        blob(80f, 82f, 84f, 74f, 94f, 72f, 98f, 82f)
        put(Beasts.hare.art, 52f, 84f, 0.22f, px = 50f, py = 92f)
        listOf(20f, 30f, 70f, 80f).forEach { x -> put(Wardrobe.flower, x, 86f, 0.2f, px = 50f, py = 70f) }
    }

    val well = Figure {
        blob(-4f, -4f, 104f, -4f, 104f, 104f, -4f, 104f)
        hole { oval(50f, 50f, 34f, 40f) }
        listOf(20f, 34f, 48f, 62f, 76f).forEachIndexed { i, y -> line(1f, 16f, y, 84f, y + (i % 2) * 2f) }
        hole { circle(50f, 22f, 10f) }
        stars(46f, 18f, 54f, 24f)
        rect(-4f, 82f, 104f, 104f)
        put(Beasts.frog.art, 50f, 82f, 0.26f, px = 50f, py = 90f)
        put(Wardrobe.bucket, 70f, 50f, 0.3f, px = 51f, py = 56f)
        line(1f, 70f, 0f, 70f, 42f)
    }

    val space = Figure {
        circle(40f, 56f, 20f)
        hole { tube(1.2f, 22f, 50f, 34f, 46f, 46f, 52f, 58f, 48f); tube(1.2f, 24f, 64f, 40f, 60f, 56f, 66f) }
        tube(3f, *(0..32).flatMap { j -> val a = Math.PI * 2 * j / 32; listOf(40f + 32f * cos(a).toFloat(), 56f + 8f * sin(a).toFloat()) }.toFloatArray())
        hole { tube(1f, *(0..16).flatMap { j -> val a = Math.PI + Math.PI * j / 16; listOf(40f + 32f * cos(a).toFloat(), 56f + 8f * sin(a).toFloat()) }.toFloatArray()) }
        circle(40f, 56f, 0.1f)
        poly(70f, 30f, 80f, 10f, 90f, 30f, 88f, 40f, 72f, 40f)
        hole { circle(80f, 24f, 3f) }
        poly(72f, 36f, 66f, 44f, 72f, 42f); poly(88f, 36f, 94f, 44f, 88f, 42f)
        taper(5f, 0.6f, 80f, 42f, 80f, 52f)
        stars(14f, 18f, 30f, 28f, 60f, 16f, 18f, 86f, 74f, 80f, 90f, 60f, 56f, 88f)
    }

    val burrow = Figure {
        blob(-4f, -4f, 104f, -4f, 104f, 30f, 80f, 26f, 50f, 30f, 20f, 26f, -4f, 30f)
        listOf(20f, 50f, 78f).forEach { x -> tube(1.4f, x, 26f, x - 4f, 34f, x - 2f, 42f); tube(1f, x, 28f, x + 5f, 38f) }
        roundTree(70f, 26f, 26f)
        blob(-4f, 104f, -4f, 30f, 6f, 34f, 4f, 60f, 12f, 86f, 50f, 92f, 88f, 86f, 96f, 60f, 94f, 34f, 104f, 30f, 104f, 104f)
        rect(30f, 70f, 70f, 74f, 1f)
        line(2f, 34f, 74f, 34f, 88f); line(2f, 66f, 74f, 66f, 88f)
        put(Wardrobe.candle, 40f, 66f, 0.3f, px = 50f, py = 72f)
        put(Treasures.teapot, 58f, 70f, 0.16f, px = 50f, py = 74f)
        put(Beasts.hedgehog.art, 20f, 90f, 0.2f, px = 50f, py = 92f)
        put(Treasures.pillow, 80f, 84f, 0.18f, px = 50f, py = 70f)
    }

    val all: Map<String, Figure> by lazy {
        linkedMapOf(
            "forest" to forest, "castle" to castle, "lighthouse" to lighthouse, "island" to island, "underwater" to underwater, "mountain" to mountain,
            "moon" to moonland, "cloudCity" to cloudCity, "mill" to mill, "village" to village, "library" to library, "grannyHouse" to grannyHouse,
            "icePalace" to icePalace, "desert" to desert, "jungle" to jungle, "swamp" to swamp, "cave" to cave, "nightTrain" to nightTrain,
            "ship" to ship, "balloonSky" to balloonSky, "giantGarden" to giantGarden, "circus" to circus, "clockTower" to clockTower, "bridge" to bridge,
            "treehouse" to treehouse, "northPole" to northPole, "volcano" to volcano, "rooftops" to rooftops, "underBed" to underBed,
            "teapotHouse" to teapotHouse, "rainbow" to rainbow, "well" to well, "space" to space, "burrow" to burrow,
        )
    }
}
