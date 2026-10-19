package app.lantern.ui.art

import kotlin.math.cos
import kotlin.math.sin

/** The magic things, each alone in the middle of the glass. */
object Treasures {
    /** One of the held things blown up to fill the slide, its middle at ([cx], [cy]) on its own grid. */
    private fun big(f: Figure, scale: Float, cx: Float, cy: Float) = Figure { put(f, 50f, 52f, scale, px = cx, py = cy) }

    val teapot = Figure {
        blob(28f, 56f, 36f, 44f, 64f, 44f, 72f, 56f, 70f, 72f, 30f, 72f)
        oval(50f, 42f, 14f, 3f)
        circle(50f, 37f, 3f)
        taper(6f, 3f, 70f, 62f, 80f, 54f, 86f, 42f)
        tube(3f, 30f, 50f, 20f, 52f, 18f, 62f, 28f, 66f)
        rect(32f, 72f, 68f, 76f, 1.5f)
        hole { tube(1.2f, 34f, 58f, 42f, 62f, 50f, 58f, 58f, 62f, 66f, 58f); circle(50f, 66f, 1.6f) }
    }

    val spoon = Figure {
        oval(64f, 32f, 9f, 13f, 35f)
        taper(4f, 2.6f, 58f, 42f, 46f, 60f, 32f, 80f)
        hole { oval(64f, 32f, 5.6f, 9f, 35f) }
        oval(63f, 33f, 3f, 5.6f, 35f)
    }

    val boot = Figure {
        poly(36f, 22f, 58f, 22f, 58f, 58f, 78f, 66f, 82f, 76f, 30f, 76f, 34f, 56f)
        rect(30f, 76f, 82f, 80f, 1.4f)
        rect(32f, 18f, 62f, 24f, 2f)
        hole { (0..3).forEach { i -> circle(52f, 32f + i * 7f, 1.4f) }; tube(1f, 40f, 66f, 60f, 64f, 74f, 70f) }
        taper(5f, 1f, 34f, 22f, 26f, 14f, 22f, 4f)
    }

    val mirror = Figure {
        oval(50f, 36f, 18f, 22f)
        taper(7f, 5f, 50f, 58f, 50f, 84f)
        circle(50f, 86f, 3.4f)
        hole { oval(50f, 36f, 13.6f, 17.6f) }
        tube(1.2f, 42f, 28f, 46f, 22f)
        tube(1.2f, 41f, 34f, 42f, 31f)
        (0..9).forEach { i -> val a = Math.PI * 2 * i / 10; circle(50f + 18f * cos(a).toFloat(), 36f + 22f * sin(a).toFloat(), 2.2f) }
    }

    val compass = Figure {
        circle(50f, 52f, 26f)
        circle(50f, 23f, 4f)
        hole {
            circle(50f, 52f, 21f)
            circle(50f, 23f, 1.8f)
        }
        poly(50f, 32f, 55f, 52f, 45f, 52f)
        poly(50f, 72f, 55f, 52f, 45f, 52f)
        hole { poly(50f, 70f, 53f, 54f, 47f, 54f) }
        circle(50f, 52f, 2.4f)
        listOf(0, 90, 180, 270).forEach { d -> val a = Math.toRadians(d.toDouble()); circle(50f + 17f * cos(a).toFloat(), 52f + 17f * sin(a).toFloat(), 1.4f) }
    }

    val hourglass = Figure {
        rect(28f, 14f, 72f, 20f, 2f)
        rect(28f, 82f, 72f, 88f, 2f)
        line(3f, 32f, 20f, 32f, 82f); line(3f, 68f, 20f, 68f, 82f)
        poly(36f, 20f, 64f, 20f, 52f, 50f, 64f, 82f, 36f, 82f, 48f, 50f)
        hole { poly(39f, 23f, 61f, 23f, 51f, 47f, 49f, 47f); poly(49f, 54f, 51f, 54f, 58f, 72f, 42f, 72f) }
        poly(42f, 34f, 58f, 34f, 50f, 46f)
        line(1f, 50f, 50f, 50f, 72f)
    }

    val egg = Figure {
        blob(50f, 16f, 66f, 30f, 72f, 56f, 64f, 78f, 50f, 84f, 36f, 78f, 28f, 56f, 34f, 30f)
        hole {
            poly(32f, 52f, 40f, 46f, 48f, 52f, 56f, 46f, 64f, 52f, 70f, 47f, 70f, 51f, 64f, 56f, 56f, 50f, 48f, 56f, 40f, 50f, 32f, 56f)
            star(50f, 68f, 4f); star(40f, 34f, 2.4f); star(60f, 32f, 2.4f)
        }
    }

    val apple = Figure {
        blob(50f, 30f, 62f, 24f, 76f, 34f, 76f, 56f, 64f, 78f, 50f, 74f, 36f, 78f, 24f, 56f, 24f, 34f, 38f, 24f)
        taper(3f, 2f, 50f, 30f, 52f, 20f, 56f, 14f)
        taper(1f, 9f, 54f, 22f, 62f, 16f, 70f, 18f)
        hole { tube(1.4f, 34f, 38f, 32f, 46f) }
    }

    val acorn = Figure {
        blob(34f, 46f, 66f, 46f, 66f, 64f, 58f, 80f, 50f, 86f, 42f, 80f, 34f, 64f)
        blob(28f, 46f, 32f, 32f, 50f, 26f, 68f, 32f, 72f, 46f)
        tube(3f, 50f, 27f, 52f, 18f, 58f, 14f)
        hole { (0..3).forEach { r -> (0..4).forEach { c -> circle(34f + c * 8f + (r % 2) * 4f, 34f + r * 3.6f, 1.2f) } }; tube(1.2f, 40f, 56f, 40f, 68f) }
    }

    val shell = Figure {
        poly(50f, 80f, 22f, 44f, 30f, 28f, 50f, 20f, 70f, 28f, 78f, 44f)
        poly(42f, 80f, 58f, 80f, 54f, 86f, 46f, 86f)
        blob(22f, 44f, 26f, 30f, 36f, 24f, 46f, 22f, 54f, 22f, 64f, 24f, 74f, 30f, 78f, 44f)
        hole { listOf(30f, 40f, 50f, 60f, 70f).forEach { x -> line(1.4f, 50f, 76f, x, 30f) } }
    }

    val button = Figure {
        circle(50f, 50f, 28f)
        hole {
            tube(1.4f, *(0..24).flatMap { i -> val a = Math.PI * 2 * i / 24; listOf(50f + 22f * cos(a).toFloat(), 50f + 22f * sin(a).toFloat()) }.toFloatArray())
            circle(43f, 43f, 3.4f); circle(57f, 43f, 3.4f); circle(43f, 57f, 3.4f); circle(57f, 57f, 3.4f)
        }
    }

    val spool = Figure {
        rect(32f, 22f, 68f, 28f, 2f)
        rect(32f, 72f, 68f, 78f, 2f)
        rect(36f, 28f, 64f, 72f, 1f)
        hole { (0..8).forEach { i -> line(0.9f, 37f, 32f + i * 4.6f, 63f, 30f + i * 4.6f) } }
        line(1.2f, 64f, 56f, 74f, 60f, 80f, 74f, 78f, 86f)
        line(1.6f, 78f, 86f, 86f, 70f)
        hole { circle(85f, 72f, 0.8f) }
    }

    val musicBox = Figure {
        rect(24f, 50f, 76f, 80f, 2f)
        poly(24f, 50f, 30f, 38f, 70f, 38f, 76f, 50f)
        line(3f, 76f, 64f, 86f, 64f)
        line(3f, 86f, 58f, 86f, 70f)
        hole { rect(28f, 56f, 72f, 74f, 1f) }
        star(50f, 65f, 5f)
        tube(1.6f, 40f, 30f, 40f, 18f, 48f, 16f)
        circle(38f, 30f, 3f)
        tube(1.6f, 60f, 26f, 60f, 12f)
        circle(58f, 26f, 3f)
    }

    val balloon = Figure {
        blob(50f, 10f, 66f, 18f, 70f, 36f, 60f, 54f, 50f, 60f, 40f, 54f, 30f, 36f, 34f, 18f)
        poly(47f, 60f, 53f, 60f, 50f, 56f)
        tube(1f, 50f, 60f, 46f, 70f, 54f, 80f, 48f, 92f)
        hole { tube(2f, 38f, 24f, 42f, 18f) }
    }

    val paperBoat = Figure {
        poly(16f, 60f, 84f, 60f, 72f, 76f, 28f, 76f)
        poly(30f, 60f, 50f, 22f, 70f, 60f)
        hole { line(1.2f, 50f, 26f, 50f, 58f); line(1f, 18f, 63f, 82f, 63f) }
        blob(4f, 82f, 20f, 78f, 36f, 82f, 52f, 78f, 68f, 82f, 84f, 78f, 96f, 82f, 96f, 88f, 4f, 88f)
    }

    val kite = Figure {
        poly(50f, 12f, 72f, 36f, 50f, 64f, 28f, 36f)
        hole { line(1.2f, 50f, 15f, 50f, 61f); line(1.2f, 31f, 36f, 69f, 36f) }
        tube(1f, 50f, 64f, 44f, 72f, 54f, 80f, 44f, 90f)
        listOf(46f to 70f, 52f to 78f, 46f to 86f).forEach { (x, y) -> poly(x - 4f, y, x, y - 3f, x + 4f, y, x, y + 3f) }
    }

    val ring = Figure {
        oval(50f, 58f, 22f, 20f)
        hole { oval(50f, 60f, 16f, 14f) }
        poly(40f, 38f, 50f, 24f, 60f, 38f, 50f, 44f)
        hole { line(0.8f, 44f, 36f, 56f, 36f); line(0.8f, 50f, 28f, 50f, 42f) }
        star(70f, 26f, 3f, 4); star(30f, 30f, 2.2f, 4)
    }

    val snowGlobe = Figure {
        circle(50f, 44f, 28f)
        poly(26f, 66f, 74f, 66f, 80f, 86f, 20f, 86f)
        hole {
            circle(50f, 44f, 24.5f)
            rect(30f, 72f, 70f, 76f, 1f)
        }
        poly(50f, 26f, 60f, 44f, 40f, 44f)
        poly(50f, 34f, 64f, 56f, 36f, 56f)
        rect(48f, 56f, 52f, 62f)
        oval(50f, 64f, 20f, 3.4f)
        listOf(34f to 30f, 66f to 34f, 30f to 48f, 70f to 50f, 58f to 22f, 42f to 22f).forEach { (x, y) -> circle(x, y, 1.4f) }
    }

    val ladder = Figure {
        line(3f, 34f, 92f, 40f, 8f)
        line(3f, 66f, 92f, 60f, 8f)
        (0..7).forEach { i -> val y = 16f + i * 10.4f; line(2.4f, 39.4f + (y - 8f) / 84f * -5.4f + 0.6f, y, 60.6f + (y - 8f) / 84f * 5.4f - 0.6f, y) }
        star(40f, 6f, 3f); star(62f, 4f, 2f)
    }

    val flute = Figure {
        line(5f, 18f, 76f, 82f, 24f)
        hole { listOf(0.4f, 0.5f, 0.6f, 0.7f, 0.82f).forEach { t -> circle(18f + 64f * t, 76f - 52f * t, 1.3f) } }
        tube(1.2f, 72f, 18f, 78f, 10f, 74f, 6f)
        tube(1.2f, 66f, 16f, 66f, 8f)
        circle(74f, 6f, 1.8f); circle(65f, 7f, 1.6f)
    }

    val drum = Figure {
        rect(26f, 40f, 74f, 72f, 1f)
        oval(50f, 40f, 24f, 7f)
        oval(50f, 72f, 24f, 7f)
        hole { oval(50f, 40f, 21f, 5f); (0..4).forEach { i -> line(1f, 28f + i * 11f, 46f, 34f + i * 11f, 70f); line(1f, 34f + i * 11f, 46f, 28f + i * 11f, 70f) } }
        line(2.4f, 30f, 14f, 46f, 34f); circle(29f, 13f, 3f)
        line(2.4f, 72f, 12f, 58f, 34f); circle(73f, 11f, 3f)
    }

    val pebble = Figure {
        blob(22f, 52f, 30f, 30f, 52f, 24f, 74f, 30f, 80f, 52f, 72f, 72f, 50f, 78f, 28f, 72f)
        hole { circle(52f, 50f, 9f); tube(1f, 32f, 62f, 38f, 68f) }
        tube(0.8f, 52f, 41f, 54f, 30f, 60f, 18f)
    }

    val starJar = Figure {
        rect(32f, 14f, 68f, 22f, 2f)
        blob(30f, 26f, 70f, 26f, 74f, 40f, 74f, 80f, 68f, 86f, 32f, 86f, 26f, 80f, 26f, 40f)
        hole { blob(33f, 30f, 67f, 30f, 70f, 42f, 70f, 78f, 66f, 82f, 34f, 82f, 30f, 78f, 30f, 42f) }
        star(50f, 56f, 13f)
        hole { star(50f, 56f, 6f) }
        star(36f, 40f, 2f); star(64f, 72f, 2.4f); star(62f, 38f, 1.6f)
    }

    val comb = Figure {
        rect(20f, 36f, 80f, 48f, 4f)
        (0..14).forEach { i -> rect(22f + i * 4f, 46f, 24.2f + i * 4f, 66f - (if (i % 3 == 0) 0f else 2f), 1f) }
        hole { star(50f, 42f, 3f); circle(36f, 42f, 1.4f); circle(64f, 42f, 1.4f) }
    }

    val pillow = Figure {
        blob(18f, 34f, 50f, 38f, 82f, 34f, 78f, 52f, 82f, 70f, 50f, 66f, 18f, 70f, 22f, 52f)
        poly(14f, 30f, 22f, 34f, 18f, 38f); poly(86f, 30f, 78f, 34f, 82f, 38f)
        poly(14f, 74f, 22f, 70f, 18f, 66f); poly(86f, 74f, 78f, 70f, 82f, 66f)
        hole { tube(1.4f, 42f, 50f, 46f, 54f, 50f, 50f); tube(1.4f, 52f, 50f, 56f, 54f, 60f, 50f); star(66f, 44f, 2.4f); star(34f, 58f, 2f) }
    }

    val pencil = Figure {
        poly(20f, 80f, 26f, 66f, 70f, 22f, 78f, 30f, 34f, 74f)
        poly(70f, 22f, 76f, 16f, 84f, 24f, 78f, 30f)
        hole { line(1f, 26f, 66f, 34f, 74f); line(0.8f, 30f, 68f, 72f, 26f) }
        tube(1.2f, 20f, 80f, 14f, 86f, 22f, 90f, 30f, 86f, 40f, 90f)
    }

    val chest = Figure {
        rect(20f, 48f, 80f, 82f, 2f)
        blob(20f, 48f, 22f, 34f, 50f, 28f, 78f, 34f, 80f, 48f)
        hole {
            rect(22f, 50f, 78f, 52f)
            rect(46f, 50f, 54f, 62f, 1f)
            line(1.4f, 30f, 32f, 30f, 80f); line(1.4f, 70f, 32f, 70f, 80f)
        }
        circle(50f, 56f, 1.6f)
        star(30f, 20f, 3f); star(72f, 18f, 2.4f); star(52f, 14f, 2f)
    }

    val yarn = Figure {
        circle(46f, 54f, 24f)
        hole {
            tube(1.4f, 26f, 48f, 40f, 34f, 58f, 32f)
            tube(1.4f, 24f, 58f, 44f, 38f, 66f, 40f)
            tube(1.4f, 30f, 70f, 50f, 46f, 70f, 52f)
            tube(1.4f, 40f, 76f, 58f, 58f, 68f, 64f)
        }
        tube(1.6f, 66f, 68f, 76f, 76f, 86f, 74f, 90f, 82f)
    }

    val carpet = Figure {
        poly(10f, 56f, 40f, 44f, 92f, 50f, 64f, 66f)
        (0..6).forEach { i -> val t = i / 6f; line(1f, 10f + t * 30f, 56f - t * 12f, 8f + t * 30f, 60f - t * 12f) }
        (0..6).forEach { i -> val t = i / 6f; line(1f, 64f + t * 28f, 66f - t * 16f, 64f + t * 28f, 70f - t * 16f) }
        hole { poly(30f, 54f, 44f, 49f, 70f, 52f, 58f, 58f); star(50f, 53f, 2.4f) }
        tube(1f, 18f, 72f, 26f, 76f, 34f, 72f)
        tube(1f, 50f, 78f, 58f, 82f, 66f, 78f)
    }

    val glasses = Figure {
        circle(32f, 52f, 13f); circle(68f, 52f, 13f)
        hole { circle(32f, 52f, 9.6f); circle(68f, 52f, 9.6f) }
        tube(2.4f, 44f, 50f, 50f, 46f, 56f, 50f)
        tube(2f, 20f, 48f, 12f, 44f, 8f, 50f)
        tube(2f, 80f, 48f, 88f, 44f, 92f, 50f)
        star(30f, 50f, 2.4f, 4); star(66f, 50f, 2.4f, 4)
    }

    val bottle = Figure {
        blob(30f, 50f, 40f, 40f, 60f, 40f, 70f, 50f, 70f, 80f, 30f, 80f)
        rect(44f, 26f, 56f, 42f, 1f)
        rect(42f, 20f, 58f, 28f, 1.4f)
        hole { blob(34f, 52f, 42f, 44f, 58f, 44f, 66f, 52f, 66f, 76f, 34f, 76f) }
        rect(40f, 54f, 60f, 68f, 1f)
        hole { line(0.8f, 43f, 58f, 57f, 58f); line(0.8f, 43f, 62f, 54f, 62f) }
        blob(4f, 88f, 20f, 84f, 36f, 88f, 52f, 84f, 68f, 88f, 84f, 84f, 96f, 88f, 96f, 92f, 4f, 92f)
    }

    val seed = Figure {
        oval(50f, 72f, 10f, 14f)
        hole { tube(1f, 50f, 62f, 50f, 82f) }
        taper(3f, 2f, 50f, 60f, 52f, 44f, 50f, 30f)
        taper(1f, 12f, 51f, 40f, 62f, 30f, 72f, 32f)
        taper(1f, 10f, 50f, 36f, 40f, 26f, 30f, 30f)
        star(64f, 18f, 2.4f); star(36f, 16f, 2f)
    }

    val all: Map<String, Figure> by lazy {
        linkedMapOf(
            "key" to big(Wardrobe.key, 1.9f, 54f, 52f), "lantern" to big(Wardrobe.lantern, 2.4f, 50f, 64f), "umbrella" to big(Wardrobe.umbrella, 1.4f, 50f, 36f),
            "book" to big(Wardrobe.book, 1.9f, 50f, 60f), "bell" to big(Wardrobe.bell, 2.2f, 50f, 62f), "candle" to big(Wardrobe.candle, 2f, 50f, 58f),
            "clock" to big(Wardrobe.pocketWatch, 2.4f, 51f, 56f), "feather" to big(Wardrobe.quill, 1.25f, 60f, 42f), "telescope" to big(Wardrobe.telescope, 1.8f, 52f, 52f),
            "teapot" to teapot, "spoon" to spoon, "boot" to boot, "hat" to big(Wardrobe.topHat, 1.6f, 50f, 62f), "mirror" to mirror, "compass" to compass,
            "hourglass" to hourglass, "map" to big(Wardrobe.scroll, 2f, 51f, 55f), "egg" to egg, "apple" to apple, "acorn" to acorn, "shell" to shell,
            "button" to button, "spool" to spool, "musicBox" to musicBox, "balloon" to balloon, "paperBoat" to paperBoat, "kite" to kite, "ring" to ring,
            "snowGlobe" to snowGlobe, "ladder" to ladder, "bucket" to big(Wardrobe.bucket, 1.9f, 51f, 64f), "flute" to flute, "drum" to drum, "pebble" to pebble,
            "starJar" to starJar, "comb" to comb, "pillow" to pillow, "pencil" to pencil, "chest" to chest, "yarn" to yarn, "broom" to big(Wardrobe.broom, 1.4f, 55f, 58f),
            "carpet" to carpet, "glasses" to glasses, "bottle" to bottle, "seed" to seed, "wateringCan" to big(Wardrobe.wateringCan, 1.5f, 56f, 58f),
            "net" to big(Wardrobe.net, 1.5f, 56f, 40f), "trumpet" to big(Wardrobe.trumpet, 1.8f, 57f, 47f),
        )
    }
}
