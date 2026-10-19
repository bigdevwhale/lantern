package app.lantern.ui.art

import androidx.compose.ui.geometry.Offset

/**
 * Who can be the hero: animals and folk, cut in profile facing right. [hatX], [hatY] and [hatW]
 * say where a hat sits (the middle of its brim) and how wide; [hand] is where a held thing goes.
 */
class Beast(val art: Figure, val hatX: Float, val hatY: Float, val hatW: Float, val hatTilt: Float = 0f, val hand: Offset = Offset(76f, 64f))

object Beasts {
    private fun eye(ink: Ink, x: Float, y: Float, r: Float = 1.7f) = ink.hole { circle(x, y, r) }

    val fox = Beast(Figure {
        blob(42f, 91f, 26f, 92f, 12f, 86f, 6f, 74f, 10f, 64f, 16f, 70f, 22f, 80f, 40f, 82f)
        blob(36f, 90f, 35f, 74f, 41f, 58f, 51f, 49f, 61f, 53f, 65f, 68f, 63f, 84f, 58f, 91f)
        blob(51f, 42f, 55f, 32f, 65f, 29f, 74f, 34f, 88f, 41f, 85f, 45f, 71f, 49f, 58f, 50f)
        poly(54f, 34f, 55f, 17f, 64f, 30f)
        poly(61f, 31f, 67f, 16f, 71f, 33f)
        tube(4.5f, 59f, 66f, 61f, 80f, 61f, 90f)
        tube(4.5f, 54f, 70f, 55f, 82f, 55f, 90f)
        oval(63f, 91f, 4f, 1.6f)
        oval(57f, 91f, 4f, 1.6f)
        hole { tube(1.4f, 6f, 72f, 12f, 76f, 16f, 73f) }
        eye(this, 70f, 37f)
    }, hatX = 63f, hatY = 29f, hatW = 18f, hatTilt = -6f, hand = Offset(76f, 66f))

    val bear = Beast(Figure {
        blob(28f, 90f, 25f, 64f, 32f, 44f, 50f, 38f, 64f, 46f, 69f, 66f, 66f, 90f)
        circle(56f, 30f, 13f)
        blob(62f, 26f, 74f, 29f, 79f, 34f, 75f, 39f, 64f, 40f)
        circle(48f, 19f, 5.5f)
        circle(62f, 18f, 5f)
        rect(32f, 80f, 45f, 92f, 4f)
        rect(51f, 80f, 64f, 92f, 4f)
        tube(9f, 60f, 50f, 70f, 58f, 73f, 66f)
        circle(78f, 33f, 2.2f)
        eye(this, 63f, 27f)
        hole { circle(48f, 19f, 2f) }
    }, hatX = 55f, hatY = 18f, hatW = 22f, hand = Offset(78f, 66f))

    val hare = Beast(Figure {
        circle(30f, 82f, 6f)
        blob(34f, 90f, 32f, 74f, 40f, 60f, 52f, 56f, 62f, 64f, 64f, 80f, 60f, 91f)
        blob(52f, 52f, 56f, 42f, 66f, 40f, 75f, 46f, 78f, 52f, 70f, 56f, 58f, 58f)
        taper(6.5f, 3.5f, 58f, 43f, 54f, 30f, 52f, 14f, 55f, 8f)
        taper(6.5f, 3.5f, 63f, 42f, 64f, 28f, 69f, 13f, 73f, 9f)
        tube(4f, 60f, 72f, 63f, 84f, 63f, 91f)
        blob(44f, 92f, 46f, 84f, 56f, 86f, 60f, 92f)
        hole { taper(2.5f, 1f, 63.5f, 38f, 65f, 27f, 69f, 16f) }
        eye(this, 68f, 47f)
    }, hatX = 61f, hatY = 40f, hatW = 15f, hand = Offset(78f, 70f))

    val hedgehog = Beast(Figure {
        // Spines all along the back, then the soft face poking out at the front.
        val back = (0..16).flatMap { i ->
            val a = Math.PI * (0.98 - i * 0.05)
            val r = if (i % 2 == 0) 38f else 24f
            listOf(48f + (r * kotlin.math.cos(a)).toFloat(), 86f - (r * 0.95f * kotlin.math.sin(a)).toFloat())
        }
        poly(*floatArrayOf(18f, 88f) + back.toFloatArray() + floatArrayOf(76f, 86f))
        blob(30f, 88f, 30f, 72f, 46f, 60f, 64f, 64f, 74f, 74f, 72f, 88f)
        blob(64f, 70f, 74f, 68f, 88f, 78f, 90f, 81f, 82f, 85f, 70f, 86f)
        circle(90f, 80f, 2.2f)
        tube(4f, 40f, 84f, 40f, 91f)
        tube(4f, 64f, 84f, 66f, 91f)
        eye(this, 76f, 74f, 1.5f)
    }, hatX = 54f, hatY = 54f, hatW = 18f, hatTilt = 12f, hand = Offset(84f, 64f))

    val owl = Beast(Figure {
        blob(32f, 88f, 28f, 60f, 32f, 38f, 50f, 28f, 68f, 38f, 72f, 60f, 68f, 88f)
        poly(32f, 40f, 30f, 24f, 42f, 32f)
        poly(68f, 40f, 70f, 24f, 58f, 32f)
        taper(8f, 2f, 34f, 52f, 26f, 66f, 26f, 80f)
        taper(8f, 2f, 66f, 52f, 74f, 66f, 74f, 80f)
        tube(3f, 42f, 88f, 42f, 92f)
        tube(3f, 58f, 88f, 58f, 92f)
        rect(30f, 91f, 72f, 93f, 1f)
        hole {
            circle(42f, 44f, 6.5f)
            circle(58f, 44f, 6.5f)
            poly(48f, 52f, 52f, 52f, 50f, 58f)
            (0..2).forEach { r -> (0..2).forEach { c -> poly(41f + c * 7f + r % 2 * 3.5f, 64f + r * 7f, 45f + c * 7f + r % 2 * 3.5f, 64f + r * 7f, 43f + c * 7f + r % 2 * 3.5f, 67.5f + r * 7f) } }
        }
        circle(42f, 44f, 2.6f)
        circle(58f, 44f, 2.6f)
    }, hatX = 50f, hatY = 30f, hatW = 24f, hand = Offset(80f, 66f))

    val mouse = Beast(Figure {
        tube(2f, 34f, 86f, 22f, 90f, 12f, 84f, 8f, 74f, 14f, 68f)
        blob(34f, 90f, 34f, 76f, 42f, 66f, 54f, 64f, 62f, 72f, 62f, 86f, 58f, 91f)
        blob(52f, 64f, 56f, 56f, 64f, 54f, 76f, 62f, 82f, 66f, 74f, 70f, 60f, 70f)
        circle(57f, 52f, 7.5f)
        circle(82f, 66f, 1.8f)
        tube(3f, 58f, 80f, 60f, 91f)
        oval(62f, 91f, 3.5f, 1.4f)
        hole { circle(57f, 52f, 4f) }
        eye(this, 70f, 61f, 1.4f)
    }, hatX = 64f, hatY = 56f, hatW = 13f, hatTilt = 8f, hand = Offset(76f, 74f))

    val cat = Beast(Figure {
        tube(5f, 38f, 88f, 26f, 88f, 18f, 80f, 18f, 66f, 24f, 58f)
        blob(36f, 90f, 34f, 74f, 40f, 58f, 50f, 50f, 60f, 54f, 64f, 70f, 62f, 86f, 58f, 91f)
        blob(48f, 44f, 50f, 34f, 60f, 29f, 70f, 32f, 74f, 40f, 70f, 48f, 58f, 50f)
        poly(50f, 36f, 50f, 22f, 58f, 30f)
        poly(62f, 30f, 69f, 21f, 69f, 34f)
        tube(4.5f, 58f, 66f, 60f, 80f, 60f, 90f)
        tube(4.5f, 53f, 70f, 54f, 82f, 54f, 90f)
        oval(62f, 91f, 3.6f, 1.6f)
        oval(56f, 91f, 3.6f, 1.6f)
        line(0.6f, 72f, 43f, 84f, 41f)
        line(0.6f, 72f, 44f, 83f, 46f)
        hole { oval(66f, 38f, 2.4f, 1.5f, -10f) }
    }, hatX = 59f, hatY = 29f, hatW = 18f, hand = Offset(76f, 66f))

    val wolf = Beast(Figure {
        blob(24f, 52f, 14f, 58f, 6f, 70f, 8f, 76f, 14f, 72f, 20f, 64f, 28f, 58f)
        blob(22f, 54f, 30f, 45f, 56f, 43f, 68f, 46f, 71f, 56f, 62f, 64f, 32f, 64f, 22f, 60f)
        blob(60f, 50f, 64f, 38f, 72f, 31f, 80f, 32f, 94f, 39f, 92f, 43f, 82f, 45f, 74f, 50f, 70f, 56f)
        poly(70f, 34f, 71f, 20f, 78f, 32f)
        poly(60f, 44f, 58f, 52f, 63f, 50f, 61f, 58f, 66f, 54f)
        listOf(28f, 35f, 58f, 64f).forEachIndexed { i, x ->
            tube(3.6f, x, 58f, x + (if (i % 2 == 0) -1.5f else 1.5f), 76f, x, 89f)
            oval(x + 1.5f, 90.5f, 3f, 1.4f)
        }
        hole { poly(77f, 36f, 82f, 35f, 79f, 38.5f) }
    }, hatX = 74f, hatY = 31f, hatW = 12f, hatTilt = 6f, hand = Offset(94f, 58f))

    val frog = Beast(Figure {
        blob(28f, 90f, 26f, 78f, 34f, 64f, 50f, 58f, 66f, 62f, 74f, 72f, 72f, 84f, 64f, 90f)
        circle(56f, 56f, 7f)
        circle(68f, 58f, 6.5f)
        blob(26f, 88f, 18f, 80f, 26f, 72f, 40f, 78f, 42f, 90f)
        tube(5f, 62f, 76f, 66f, 86f, 70f, 91f)
        poly(64f, 92f, 78f, 92f, 74f, 89f, 68f, 89f)
        poly(14f, 92f, 34f, 92f, 30f, 88f, 20f, 88f)
        hole {
            circle(56f, 56f, 3.4f)
            circle(68f, 58f, 3.2f)
            tube(1f, 62f, 72f, 70f, 74f, 74f, 72f)
        }
        circle(57f, 56f, 1.5f)
        circle(69f, 58f, 1.4f)
    }, hatX = 62f, hatY = 51f, hatW = 22f, hand = Offset(84f, 70f))

    val turtle = Beast(Figure {
        blob(18f, 82f, 24f, 64f, 40f, 54f, 58f, 54f, 72f, 64f, 78f, 82f)
        rect(16f, 81f, 80f, 85f, 2f)
        blob(76f, 78f, 80f, 70f, 88f, 64f, 96f, 66f, 96f, 72f, 88f, 76f, 82f, 82f)
        tube(6f, 28f, 84f, 26f, 91f)
        tube(6f, 66f, 84f, 68f, 91f)
        poly(14f, 82f, 6f, 86f, 16f, 86f)
        hole {
            poly(34f, 66f, 42f, 60f, 50f, 66f, 42f, 72f)
            poly(52f, 66f, 60f, 60f, 68f, 66f, 60f, 72f)
            poly(26f, 76f, 32f, 72f, 40f, 76f, 32f, 79f)
            poly(44f, 76f, 50f, 72f, 56f, 76f, 50f, 79f)
            poly(60f, 76f, 66f, 72f, 72f, 76f, 66f, 79f)
        }
        eye(this, 90f, 68f, 1.3f)
    }, hatX = 89f, hatY = 64.5f, hatW = 12f, hatTilt = 8f, hand = Offset(56f, 40f))

    val snail = Beast(Figure {
        blob(12f, 92f, 14f, 86f, 40f, 84f, 74f, 84f, 84f, 76f, 88f, 70f, 92f, 76f, 90f, 88f, 80f, 92f)
        circle(46f, 64f, 22f)
        tube(1.6f, 84f, 74f, 86f, 62f, 84f, 54f)
        tube(1.6f, 88f, 74f, 92f, 64f, 94f, 58f)
        circle(84f, 54f, 2.2f)
        circle(94f, 58f, 2.2f)
        hole {
            // The shell's spiral as a cut line.
            val turns = (0..60).flatMap { i ->
                val t = i / 60f
                val a = 4.2 * Math.PI * t
                val r = 18f * (1f - t * 0.9f)
                listOf(46f + (r * kotlin.math.cos(a)).toFloat(), 64f + (r * kotlin.math.sin(a)).toFloat())
            }
            tube(2.2f, *turns.toFloatArray())
        }
    }, hatX = 46f, hatY = 42f, hatW = 22f, hand = Offset(70f, 40f))

    val crow = Beast(Figure {
        poly(30f, 66f, 6f, 76f, 10f, 80f, 32f, 74f)
        blob(28f, 72f, 36f, 58f, 52f, 50f, 64f, 52f, 70f, 62f, 64f, 74f, 46f, 78f)
        circle(66f, 46f, 9f)
        poly(72f, 42f, 90f, 47f, 72f, 51f)
        taper(10f, 2f, 40f, 60f, 30f, 66f, 18f, 74f)
        line(2f, 52f, 76f, 52f, 91f)
        line(2f, 58f, 76f, 60f, 91f)
        line(1.6f, 48f, 91f, 56f, 91f, 62f, 91f, 66f, 92f)
        eye(this, 68f, 44f, 1.6f)
    }, hatX = 64f, hatY = 38f, hatW = 15f, hand = Offset(84f, 64f))

    val goat = Beast(Figure {
        blob(24f, 62f, 28f, 52f, 54f, 50f, 64f, 54f, 64f, 66f, 40f, 70f, 26f, 68f)
        line(3.5f, 31f, 64f, 30f, 90f)
        line(3.5f, 38f, 66f, 39f, 90f)
        line(3.5f, 55f, 64f, 55f, 90f)
        line(3.5f, 61f, 62f, 63f, 90f)
        blob(58f, 56f, 62f, 42f, 68f, 33f, 77f, 33f, 84f, 41f, 79f, 46f, 70f, 50f, 66f, 60f)
        taper(4.5f, 1.4f, 70f, 34f, 63f, 25f, 56f, 23f, 53f, 28f)
        taper(3.5f, 1f, 68f, 38f, 62f, 41f, 58f, 44f)
        poly(76f, 44f, 80f, 54f, 73f, 47f)
        poly(26f, 55f, 18f, 48f, 23f, 58f)
        eye(this, 74f, 38f, 1.4f)
    }, hatX = 69f, hatY = 33f, hatW = 12f, hand = Offset(86f, 66f))

    val pig = Beast(Figure {
        blob(18f, 70f, 24f, 54f, 44f, 48f, 64f, 52f, 72f, 62f, 70f, 76f, 46f, 80f, 26f, 78f)
        blob(62f, 54f, 72f, 46f, 82f, 50f, 87f, 58f, 80f, 66f, 66f, 66f)
        rect(83f, 52f, 91f, 62f, 2.5f)
        poly(70f, 50f, 70f, 38f, 79f, 48f)
        rect(27f, 74f, 34f, 90f, 2f)
        rect(37f, 76f, 44f, 90f, 2f)
        rect(54f, 76f, 61f, 90f, 2f)
        rect(63f, 74f, 70f, 90f, 2f)
        tube(1.6f, 20f, 62f, 13f, 61f, 12f, 56f, 17f, 55f, 17f, 60f, 10f, 64f)
        hole {
            circle(78f, 54f, 1.5f)
            oval(88.5f, 55f, 0.8f, 1.6f)
        }
    }, hatX = 74f, hatY = 47f, hatW = 13f, hatTilt = 6f, hand = Offset(88f, 74f))

    val horse = Beast(Figure {
        blob(22f, 54f, 30f, 44f, 58f, 44f, 70f, 50f, 68f, 64f, 40f, 66f, 22f, 62f)
        poly(58f, 50f, 68f, 26f, 80f, 24f, 74f, 54f)
        blob(70f, 22f, 78f, 18f, 88f, 26f, 93f, 32f, 89f, 37f, 82f, 36f, 74f, 32f)
        poly(73f, 22f, 75f, 11f, 79f, 20f)
        poly(70f, 22f, 64f, 28f, 66f, 30f, 60f, 36f, 63f, 38f, 57f, 44f, 61f, 46f, 66f, 30f)
        listOf(28f, 36f, 58f, 65f).forEachIndexed { i, x ->
            tube(4f, x, 58f, x + (if (i % 2 == 0) -1f else 1f), 74f, x, 88f)
            rect(x - 2.6f, 88f, x + 3f, 92f, 0.8f)
        }
        taper(7f, 2f, 23f, 50f, 14f, 56f, 11f, 70f, 15f, 82f)
        eye(this, 81f, 26f, 1.4f)
    }, hatX = 77f, hatY = 20f, hatW = 12f, hatTilt = 10f, hand = Offset(92f, 58f))

    val deer = Beast(Figure {
        blob(24f, 58f, 32f, 50f, 56f, 50f, 66f, 54f, 64f, 66f, 40f, 68f, 24f, 64f)
        poly(56f, 54f, 66f, 30f, 74f, 30f, 68f, 58f)
        blob(66f, 30f, 72f, 24f, 80f, 26f, 90f, 34f, 87f, 38f, 76f, 36f, 68f, 36f)
        taper(4f, 1.5f, 66f, 30f, 60f, 26f, 58f, 20f)
        listOf(30f, 36f, 58f, 63f).forEachIndexed { i, x -> line(3f, x, 62f, x + (if (i % 2 == 0) 1f else -1f), 76f, x, 91f) }
        tube(2.4f, 71f, 25f, 68f, 14f, 62f, 6f)
        tube(2.4f, 74f, 25f, 78f, 14f, 86f, 6f)
        tube(2f, 68f, 15f, 62f, 14f)
        tube(2f, 66f, 10f, 70f, 4f)
        tube(2f, 78f, 15f, 84f, 15f)
        tube(2f, 80f, 11f, 77f, 4f)
        poly(26f, 54f, 20f, 50f, 22f, 58f)
        eye(this, 78f, 30f, 1.3f)
    }, hatX = 74f, hatY = 26f, hatW = 11f, hand = Offset(90f, 58f))

    val squirrel = Beast(Figure {
        blob(38f, 90f, 22f, 86f, 12f, 68f, 14f, 44f, 26f, 30f, 42f, 30f, 46f, 40f, 34f, 46f, 28f, 58f, 30f, 74f, 42f, 82f)
        blob(40f, 90f, 38f, 74f, 44f, 60f, 54f, 56f, 62f, 62f, 64f, 80f, 60f, 91f)
        blob(52f, 58f, 54f, 46f, 62f, 42f, 72f, 47f, 76f, 53f, 66f, 58f)
        poly(56f, 47f, 56f, 34f, 59f, 38f, 62f, 45f)
        tube(4f, 58f, 66f, 66f, 66f, 68f, 62f)
        oval(69f, 62f, 3.2f, 3.6f)
        hole { tube(1.2f, 20f, 50f, 24f, 40f, 34f, 36f) }
        eye(this, 66f, 49f, 1.3f)
    }, hatX = 62f, hatY = 43f, hatW = 12f, hand = Offset(80f, 70f))

    val badger = Beast(Figure {
        blob(12f, 84f, 16f, 70f, 34f, 62f, 56f, 62f, 68f, 68f, 70f, 80f, 60f, 88f, 30f, 89f)
        blob(62f, 64f, 70f, 60f, 80f, 64f, 94f, 76f, 92f, 80f, 78f, 80f, 66f, 80f)
        circle(70f, 61f, 3.6f)
        rect(22f, 82f, 30f, 91f, 2f)
        rect(34f, 84f, 42f, 91f, 2f)
        rect(54f, 82f, 62f, 91f, 2f)
        rect(63f, 80f, 70f, 91f, 2f)
        poly(14f, 76f, 4f, 74f, 12f, 82f)
        hole {
            taper(2.6f, 1f, 70f, 66f, 80f, 70f, 92f, 77f)
            circle(76f, 70f, 0.1f)
        }
        eye(this, 77f, 72.5f, 1.2f)
    }, hatX = 73f, hatY = 62f, hatW = 13f, hatTilt = 14f, hand = Offset(94f, 64f))

    val beaver = Beast(Figure {
        oval(20f, 89f, 15f, 4.5f, -6f)
        blob(30f, 90f, 30f, 70f, 38f, 52f, 52f, 48f, 62f, 58f, 64f, 78f, 58f, 91f)
        blob(50f, 50f, 52f, 40f, 62f, 35f, 72f, 40f, 75f, 47f, 66f, 52f)
        circle(54f, 38f, 3f)
        tube(4f, 58f, 64f, 66f, 70f, 68f, 66f)
        rect(70f, 48f, 73.5f, 54f, 0.6f)
        hole {
            line(0.8f, 10f, 87f, 30f, 87f)
            line(0.8f, 12f, 91f, 30f, 90f)
            line(0.8f, 16f, 85f, 15f, 93f)
            line(0.8f, 22f, 85f, 22f, 93f)
        }
        eye(this, 64f, 41f, 1.3f)
    }, hatX = 60f, hatY = 36f, hatW = 14f, hand = Offset(78f, 70f))

    val raccoon = Beast(Figure {
        taper(12f, 6f, 38f, 86f, 26f, 88f, 14f, 82f, 8f, 70f)
        circle(8f, 69f, 3f)
        blob(36f, 90f, 34f, 74f, 40f, 58f, 50f, 50f, 60f, 54f, 64f, 70f, 62f, 86f, 58f, 91f)
        blob(48f, 46f, 50f, 36f, 60f, 32f, 70f, 36f, 82f, 44f, 78f, 47f, 66f, 50f, 56f, 51f)
        poly(51f, 38f, 51f, 26f, 59f, 34f)
        tube(4.5f, 58f, 66f, 62f, 76f, 66f, 72f)
        tube(4.5f, 54f, 70f, 55f, 82f, 55f, 90f)
        oval(57f, 91f, 4f, 1.6f)
        hole {
            listOf(30f, 22f, 15f).forEach { x -> taper(2.2f, 2.2f, x, 82f, x - 3f, 90f) }
            blob(58f, 38f, 70f, 38f, 74f, 42f, 62f, 44f)
        }
        circle(66f, 40.5f, 1.3f)
    }, hatX = 60f, hatY = 33f, hatW = 15f, hand = Offset(76f, 70f))

    val whale = Beast(Figure {
        blob(10f, 66f, 24f, 50f, 56f, 44f, 80f, 50f, 92f, 62f, 84f, 74f, 52f, 78f, 22f, 74f)
        poly(12f, 64f, 2f, 46f, 9f, 52f, 16f, 46f, 15f, 62f)
        tube(2f, 60f, 45f, 57f, 34f, 52f, 28f)
        tube(2f, 60f, 45f, 63f, 34f, 68f, 28f)
        circle(50f, 26f, 1.8f); circle(70f, 26f, 1.8f); circle(60f, 30f, 1.6f)
        blob(0f, 92f, 0f, 84f, 12f, 82f, 24f, 86f, 36f, 82f, 48f, 86f, 60f, 82f, 72f, 86f, 84f, 82f, 100f, 84f, 100f, 92f)
        taper(5f, 1.5f, 60f, 66f, 54f, 74f, 46f, 76f)
        hole {
            tube(1f, 70f, 66f, 80f, 67f, 88f, 64f)
            (0..3).forEach { i -> tube(0.9f, 30f + i * 8f, 70f, 34f + i * 8f, 74f) }
        }
        eye(this, 78f, 58f, 1.5f)
    }, hatX = 60f, hatY = 45f, hatW = 20f, hand = Offset(84f, 40f))

    val fish = Beast(Figure {
        blob(22f, 60f, 40f, 46f, 64f, 46f, 82f, 58f, 66f, 72f, 40f, 74f)
        poly(24f, 60f, 6f, 46f, 11f, 60f, 6f, 74f)
        poly(44f, 48f, 52f, 36f, 62f, 47f)
        poly(48f, 72f, 54f, 80f, 58f, 72f)
        hole {
            tube(1f, 66f, 50f, 68f, 60f, 66f, 69f)
            listOf(36f, 46f, 56f).forEach { x ->
                tube(0.9f, x, 54f, x + 4f, 57f, x, 60f)
                tube(0.9f, x + 4f, 61f, x + 8f, 64f, x + 4f, 67f)
            }
            circle(74f, 56f, 2.2f)
        }
        circle(74.6f, 56f, 1f)
    }, hatX = 70f, hatY = 49f, hatW = 13f, hatTilt = 14f, hand = Offset(88f, 70f))

    val crab = Beast(Figure {
        oval(50f, 76f, 22f, 11f)
        listOf(-1f, 1f).forEach { s ->
            for (i in 0..2) line(2.2f, 50f + s * (14f + i * 4f), 80f, 50f + s * (24f + i * 5f), 84f, 50f + s * (28f + i * 5f), 92f)
            tube(3f, 50f + s * 16f, 70f, 50f + s * 26f, 62f, 50f + s * 30f, 54f)
            blob(50f + s * 26f, 52f, 50f + s * 30f, 40f, 50f + s * 38f, 38f, 50f + s * 34f, 46f, 50f + s * 40f, 48f, 50f + s * 36f, 56f)
            line(2f, 50f + s * 6f, 68f, 50f + s * 8f, 58f)
            circle(50f + s * 8f, 57f, 3.4f)
        }
        hole {
            circle(43f, 57f, 1.4f); circle(59f, 57f, 1.4f)
            tube(1f, 44f, 80f, 50f, 83f, 56f, 80f)
        }
    }, hatX = 50f, hatY = 66f, hatW = 20f, hand = Offset(88f, 70f))

    val octopus = Beast(Figure {
        blob(34f, 62f, 32f, 42f, 40f, 28f, 50f, 24f, 60f, 28f, 68f, 42f, 66f, 62f)
        val arms = listOf(
            floatArrayOf(36f, 60f, 26f, 70f, 16f, 72f, 10f, 66f),
            floatArrayOf(40f, 62f, 34f, 76f, 26f, 86f, 18f, 88f),
            floatArrayOf(46f, 62f, 44f, 78f, 40f, 90f, 34f, 92f),
            floatArrayOf(54f, 62f, 56f, 78f, 60f, 90f, 66f, 92f),
            floatArrayOf(60f, 62f, 66f, 76f, 74f, 86f, 82f, 88f),
            floatArrayOf(64f, 60f, 74f, 70f, 84f, 72f, 90f, 66f),
        )
        arms.forEach { taper(7f, 1.6f, *it) }
        hole {
            circle(44f, 46f, 3.6f); circle(56f, 46f, 3.6f)
            tube(1f, 46f, 55f, 50f, 57f, 54f, 55f)
        }
        circle(44.5f, 46.5f, 1.6f); circle(56.5f, 46.5f, 1.6f)
    }, hatX = 50f, hatY = 25f, hatW = 22f, hand = Offset(88f, 56f))

    val penguin = Beast(Figure {
        blob(38f, 90f, 34f, 70f, 36f, 48f, 44f, 32f, 56f, 30f, 62f, 40f, 64f, 70f, 60f, 90f)
        poly(60f, 36f, 72f, 40f, 60f, 43f)
        taper(7f, 2f, 42f, 50f, 36f, 64f, 32f, 74f)
        blob(44f, 92f, 46f, 88f, 54f, 88f, 58f, 92f)
        blob(56f, 92f, 58f, 88f, 66f, 88f, 70f, 92f)
        hole { blob(56f, 46f, 62f, 58f, 61f, 76f, 54f, 86f, 48f, 76f, 50f, 56f) }
        eye(this, 55f, 37f, 1.5f)
    }, hatX = 51f, hatY = 31f, hatW = 15f, hand = Offset(76f, 66f))

    val elephant = Beast(Figure {
        blob(14f, 62f, 20f, 40f, 46f, 32f, 66f, 38f, 72f, 56f, 66f, 70f, 22f, 72f)
        circle(72f, 44f, 14f)
        taper(10f, 4f, 82f, 50f, 88f, 62f, 87f, 76f, 92f, 84f)
        rect(20f, 64f, 31f, 91f, 3f)
        rect(34f, 66f, 44f, 91f, 3f)
        rect(52f, 66f, 62f, 91f, 3f)
        rect(64f, 62f, 74f, 91f, 3f)
        tube(1.4f, 15f, 50f, 9f, 60f, 10f, 70f)
        hole {
            tube(1.2f, 66f, 34f, 60f, 40f, 60f, 54f, 66f, 60f, 72f, 56f)
            taper(2.4f, 0.6f, 80f, 56f, 86f, 60f, 92f, 58f)
        }
        eye(this, 78f, 40f, 1.5f)
    }, hatX = 70f, hatY = 31f, hatW = 18f, hand = Offset(96f, 60f))

    val giraffe = Beast(Figure {
        blob(22f, 58f, 28f, 48f, 54f, 46f, 64f, 52f, 60f, 62f, 30f, 64f)
        listOf(28f, 34f, 54f, 60f).forEach { x -> line(3f, x, 58f, x, 90f); rect(x - 1.8f, 89f, x + 2.4f, 92f, 0.6f) }
        taper(10f, 7f, 56f, 52f, 64f, 32f, 70f, 16f)
        blob(66f, 16f, 72f, 9f, 82f, 11f, 92f, 18f, 88f, 22f, 74f, 22f)
        tube(1.6f, 72f, 11f, 71f, 4f)
        tube(1.6f, 76f, 11f, 77f, 4f)
        circle(71f, 3.6f, 1.6f); circle(77f, 3.6f, 1.6f)
        poly(70f, 12f, 63f, 14f, 69f, 16f)
        tube(1.2f, 23f, 54f, 18f, 66f)
        hole {
            listOf(32f to 52f, 42f to 55f, 52f to 52f, 37f to 60f, 48f to 60f, 62f to 30f, 66f to 22f, 59f to 40f).forEach { (x, y) ->
                poly(x - 3f, y, x, y - 2.6f, x + 3f, y - 0.6f, x + 2f, y + 2.6f, x - 2f, y + 2.4f)
            }
        }
        eye(this, 80f, 15f, 1.3f)
    }, hatX = 77f, hatY = 10f, hatW = 11f, hand = Offset(84f, 50f))

    val lion = Beast(Figure {
        taper(3f, 2f, 36f, 86f, 24f, 88f, 16f, 80f, 14f, 70f)
        oval(14f, 68f, 3.4f, 4f)
        blob(34f, 90f, 32f, 72f, 40f, 56f, 52f, 50f, 62f, 56f, 66f, 72f, 64f, 86f, 60f, 91f)
        star(58f, 38f, 20f, 14, 0.78f)
        blob(56f, 32f, 64f, 26f, 74f, 30f, 82f, 38f, 80f, 44f, 70f, 48f, 60f, 46f)
        tube(5f, 60f, 66f, 62f, 80f, 62f, 90f)
        tube(5f, 54f, 70f, 55f, 82f, 55f, 90f)
        oval(64f, 91f, 4f, 1.6f)
        oval(57f, 91f, 4f, 1.6f)
        hole { tube(1.2f, 46f, 26f, 42f, 38f, 46f, 50f) }
        eye(this, 72f, 34f, 1.5f)
    }, hatX = 60f, hatY = 22f, hatW = 20f, hand = Offset(78f, 66f))

    val crocodile = Beast(Figure {
        blob(4f, 82f, 16f, 76f, 44f, 72f, 70f, 72f, 84f, 74f, 98f, 78f, 98f, 82f, 84f, 84f, 50f, 86f, 20f, 85f)
        (0..8).forEach { i -> val x = 18f + i * 6f; poly(x, 74.5f, x + 3f, 69f, x + 6f, 74f) }
        circle(76f, 70f, 3.6f)
        rect(30f, 82f, 36f, 91f, 2f)
        rect(56f, 82f, 62f, 91f, 2f)
        hole {
            line(1f, 80f, 79f, 97f, 80f)
            (0..4).forEach { i -> poly(82f + i * 3f, 79f, 83.5f + i * 3f, 81.6f, 85f + i * 3f, 79f) }
        }
        eye(this, 76.5f, 69.5f, 1.2f)
    }, hatX = 76f, hatY = 67f, hatW = 10f, hand = Offset(88f, 62f))

    val camel = Beast(Figure {
        blob(18f, 58f, 22f, 50f, 28f, 36f, 36f, 42f, 42f, 32f, 50f, 40f, 58f, 46f, 66f, 54f, 60f, 64f, 26f, 64f)
        taper(7f, 6f, 62f, 54f, 72f, 50f, 76f, 36f, 78f, 26f)
        blob(74f, 24f, 82f, 20f, 92f, 26f, 90f, 32f, 82f, 30f, 76f, 30f)
        poly(77f, 22f, 78f, 16f, 80f, 21f)
        listOf(26f, 32f, 54f, 60f).forEachIndexed { i, x -> tube(3f, x, 60f, x + (if (i % 2 == 0) 1f else -1f), 76f, x, 90f); oval(x + 1f, 91f, 3f, 1.3f) }
        tube(1.4f, 19f, 54f, 14f, 66f)
        eye(this, 84f, 24f, 1.2f)
    }, hatX = 82f, hatY = 21f, hatW = 11f, hand = Offset(92f, 50f))

    val dragon = Beast(Figure {
        poly(42f, 58f, 34f, 20f, 26f, 30f, 18f, 18f, 14f, 32f, 6f, 30f, 10f, 46f, 2f, 48f, 14f, 60f, 34f, 66f)
        blob(28f, 90f, 28f, 70f, 38f, 54f, 52f, 48f, 62f, 56f, 66f, 76f, 60f, 91f)
        blob(52f, 54f, 56f, 40f, 64f, 30f, 76f, 28f, 90f, 32f, 94f, 38f, 82f, 40f, 72f, 44f, 66f, 56f)
        taper(3.4f, 0.8f, 66f, 30f, 60f, 20f, 54f, 18f)
        taper(3f, 0.8f, 70f, 29f, 68f, 20f, 64f, 15f)
        taper(11f, 2f, 32f, 86f, 18f, 90f, 8f, 84f, 8f, 74f, 14f, 70f)
        poly(14f, 70f, 12f, 62f, 20f, 66f, 18f, 72f)
        (0..4).forEach { i -> val t = i / 4f; val x = 54f - t * 18f; val y = 46f + t * 18f; poly(x, y, x - 6f, y - 3f, x - 2f, y + 4f) }
        tube(5f, 60f, 64f, 68f, 70f, 72f, 66f)
        poly(70f, 64f, 76f, 62f, 74f, 68f)
        oval(60f, 91f, 6f, 2f)
        hole {
            tube(1.2f, 34f, 22f, 24f, 50f)
            tube(1.2f, 18f, 20f, 18f, 54f)
            tube(1.2f, 8f, 32f, 14f, 56f)
            poly(72f, 33f, 78f, 31f, 76f, 35f)
            tube(1f, 84f, 38f, 92f, 37f)
        }
        circle(96f, 35f, 1.2f); circle(99f, 32f, 1f)
    }, hatX = 72f, hatY = 29f, hatW = 15f, hatTilt = -4f, hand = Offset(80f, 70f))

    val unicorn = Beast(Figure {
        put(horse.art)
        taper(3f, 0.6f, 82f, 22f, 88f, 12f, 92f, 4f)
        hole { tube(0.6f, 84f, 19f, 87f, 18f); tube(0.6f, 86f, 14f, 89f, 13f) }
        tube(2.6f, 64f, 28f, 58f, 30f, 56f, 26f, 60f, 24f)
        tube(2.6f, 14f, 80f, 20f, 86f, 26f, 82f, 22f, 78f)
    }, hatX = 77f, hatY = 20f, hatW = 12f, hatTilt = 10f, hand = Offset(92f, 58f))

    val snowman = Beast(Figure {
        circle(50f, 76f, 16f)
        circle(50f, 50f, 12f)
        circle(50f, 30f, 9f)
        poly(57f, 29f, 72f, 32f, 57f, 33f)
        line(1.8f, 39f, 50f, 26f, 40f, 22f, 42f)
        line(1.8f, 26f, 40f, 24f, 34f)
        line(1.8f, 61f, 50f, 74f, 42f, 80f, 42f)
        line(1.8f, 74f, 42f, 76f, 36f)
        rect(26f, 89f, 74f, 93f, 2f)
        hole {
            circle(46f, 27f, 1.4f); circle(53f, 27f, 1.4f)
            circle(50f, 46f, 1.5f); circle(50f, 54f, 1.5f); circle(50f, 70f, 1.6f); circle(50f, 80f, 1.6f)
        }
    }, hatX = 50f, hatY = 22f, hatW = 17f, hand = Offset(84f, 52f))

    /** Folk: in profile too, with a bit of lace cut out where the clothes allow. */
    val girl = Beast(Figure {
        circle(52f, 26f, 9f)
        poly(58f, 25f, 62f, 28f, 58f, 30f)
        blob(44f, 22f, 40f, 30f, 36f, 40f, 38f, 46f, 42f, 36f, 46f, 28f)
        poly(44f, 36f, 58f, 36f, 70f, 76f, 34f, 76f)
        tube(3f, 56f, 40f, 64f, 52f, 70f, 56f)
        line(3.2f, 46f, 76f, 46f, 90f)
        line(3.2f, 56f, 76f, 58f, 90f)
        oval(48f, 91f, 4f, 1.6f); oval(60f, 91f, 4f, 1.6f)
        hole {
            (0..5).forEach { i -> circle(38f + i * 6f, 70f, 1.6f) }
            poly(48f, 46f, 54f, 46f, 51f, 52f)
        }
        eye(this, 56f, 24f, 1.1f)
    }, hatX = 51f, hatY = 18f, hatW = 18f, hand = Offset(74f, 60f))

    val boy = Beast(Figure {
        circle(52f, 28f, 9f)
        poly(58f, 27f, 62f, 30f, 58f, 32f)
        poly(43f, 24f, 46f, 17f, 49f, 21f, 53f, 16f, 55f, 21f, 60f, 19f, 60f, 24f)
        blob(44f, 38f, 52f, 36f, 60f, 40f, 60f, 62f, 44f, 62f)
        rect(43f, 60f, 61f, 70f, 2f)
        tube(3f, 58f, 42f, 66f, 52f, 72f, 54f)
        tube(3f, 46f, 42f, 40f, 54f, 40f, 60f)
        line(3.6f, 47f, 70f, 46f, 90f)
        line(3.6f, 57f, 70f, 59f, 90f)
        oval(49f, 91f, 4f, 1.6f); oval(62f, 91f, 4f, 1.6f)
        hole { line(1.2f, 46f, 44f, 58f, 44f); line(1.2f, 46f, 50f, 58f, 50f) }
        eye(this, 56f, 26f, 1.1f)
    }, hatX = 52f, hatY = 19f, hatW = 18f, hand = Offset(76f, 58f))

    val grandpa = Beast(Figure {
        circle(54f, 26f, 8.5f)
        poly(60f, 24f, 66f, 29f, 60f, 30f)
        blob(52f, 30f, 62f, 30f, 66f, 36f, 64f, 46f, 58f, 52f, 52f, 40f)
        blob(38f, 44f, 46f, 36f, 56f, 38f, 60f, 48f, 62f, 80f, 36f, 80f, 34f, 60f)
        tube(3f, 56f, 44f, 64f, 56f, 70f, 58f)
        line(3.4f, 43f, 78f, 42f, 90f)
        line(3.4f, 54f, 78f, 56f, 90f)
        oval(45f, 91f, 4.4f, 1.6f); oval(59f, 91f, 4.4f, 1.6f)
        hole {
            tube(0.8f, 57f, 36f, 59f, 44f)
            tube(0.8f, 61f, 36f, 62f, 42f)
            listOf(52f, 60f, 68f).forEach { y -> circle(52f, y, 1.2f) }
            line(1f, 38f, 74f, 61f, 74f)
        }
        eye(this, 58f, 24f, 1.1f)
    }, hatX = 53f, hatY = 19f, hatW = 17f, hand = Offset(74f, 62f))

    val grandma = Beast(Figure {
        circle(52f, 28f, 8.5f)
        circle(43f, 22f, 5f)
        poly(58f, 27f, 62f, 30f, 58f, 32f)
        blob(42f, 38f, 52f, 36f, 60f, 40f, 66f, 66f, 70f, 90f, 34f, 90f, 38f, 64f)
        poly(40f, 40f, 62f, 40f, 66f, 56f, 36f, 56f)
        tube(3f, 58f, 44f, 64f, 54f, 70f, 56f)
        hole {
            (0..6).forEach { i -> poly(37f + i * 4.2f, 56f, 39.1f + i * 4.2f, 59.5f, 41.2f + i * 4.2f, 56f) }
            (0..4).forEach { i -> circle(40f + i * 6f, 84f, 1.4f) }
        }
        eye(this, 56f, 26f, 1.1f)
    }, hatX = 51f, hatY = 20f, hatW = 17f, hand = Offset(74f, 60f))

    val robot = Beast(Figure {
        rect(40f, 18f, 62f, 36f, 3f)
        line(1.6f, 51f, 18f, 51f, 10f)
        circle(51f, 9f, 2.6f)
        rect(46f, 36f, 56f, 40f)
        rect(36f, 40f, 66f, 70f, 3f)
        line(3.2f, 36f, 46f, 28f, 58f, 30f, 66f)
        line(3.2f, 66f, 46f, 72f, 56f, 76f, 58f)
        rect(40f, 70f, 47f, 88f, 1.5f)
        rect(55f, 70f, 62f, 88f, 1.5f)
        rect(36f, 88f, 50f, 92f, 1.5f)
        rect(52f, 88f, 66f, 92f, 1.5f)
        hole {
            circle(46f, 26f, 2.6f); circle(56f, 26f, 2.6f)
            rect(45f, 31f, 57f, 33f)
            circle(51f, 50f, 5f)
            rect(42f, 60f, 60f, 62f); rect(42f, 64f, 60f, 66f)
        }
        circle(51f, 50f, 2.4f)
    }, hatX = 51f, hatY = 18f, hatW = 22f, hand = Offset(80f, 60f))

    val rooster = Beast(Figure {
        blob(28f, 66f, 34f, 52f, 50f, 50f, 62f, 54f, 66f, 64f, 56f, 74f, 38f, 74f)
        taper(6f, 2f, 34f, 58f, 24f, 40f, 16f, 34f)
        taper(6f, 2f, 32f, 62f, 18f, 50f, 10f, 48f)
        taper(6f, 2f, 32f, 66f, 18f, 62f, 10f, 64f)
        taper(7f, 5f, 60f, 56f, 64f, 44f, 66f, 32f)
        circle(68f, 30f, 6f)
        poly(73f, 28f, 80f, 31f, 73f, 33f)
        poly(62f, 26f, 63f, 20f, 66f, 23f, 68f, 18f, 70f, 23f, 73f, 20f, 73f, 27f)
        oval(72f, 37f, 1.8f, 3f)
        line(2f, 46f, 72f, 44f, 82f, 46f, 91f)
        line(2f, 54f, 72f, 56f, 82f, 56f, 91f)
        line(1.4f, 40f, 91f, 48f, 91f); line(1.4f, 50f, 91f, 60f, 91f)
        hole { tube(1f, 40f, 60f, 48f, 64f, 56f, 60f) }
        eye(this, 69f, 29f, 1.2f)
    }, hatX = 68f, hatY = 24f, hatW = 13f, hand = Offset(84f, 62f))

    val duck = Beast(Figure {
        blob(20f, 70f, 26f, 60f, 44f, 58f, 64f, 62f, 70f, 72f, 60f, 82f, 34f, 82f)
        poly(22f, 66f, 12f, 58f, 18f, 70f)
        taper(7f, 6f, 62f, 64f, 64f, 54f, 62f, 46f)
        circle(64f, 42f, 8f)
        blob(70f, 42f, 82f, 43f, 84f, 46f, 72f, 47f)
        line(2f, 44f, 82f, 44f, 91f)
        poly(42f, 91f, 50f, 91f, 46f, 89f)
        hole { tube(1.1f, 36f, 68f, 46f, 72f, 56f, 68f) }
        eye(this, 66f, 40f, 1.3f)
    }, hatX = 63f, hatY = 35f, hatW = 13f, hand = Offset(84f, 64f))

    val bee = Beast(Figure {
        oval(50f, 62f, 18f, 13f)
        circle(70f, 56f, 8f)
        poly(31f, 62f, 24f, 64f, 32f, 66f)
        tube(1.2f, 72f, 49f, 74f, 40f, 78f, 36f)
        tube(1.2f, 68f, 49f, 66f, 40f, 62f, 36f)
        circle(78f, 36f, 1.8f); circle(62f, 36f, 1.8f)
        oval(42f, 42f, 8f, 13f, -20f)
        oval(54f, 40f, 7f, 12f, 14f)
        line(1.4f, 48f, 74f, 46f, 84f); line(1.4f, 54f, 74f, 56f, 84f)
        hole {
            rect(40f, 50f, 44f, 74f, 1f); rect(50f, 49f, 54f, 75f, 1f)
            oval(42f, 42f, 5.5f, 10f, -20f); oval(54f, 40f, 4.6f, 9f, 14f)
        }
        eye(this, 73f, 55f, 1.4f)
    }, hatX = 70f, hatY = 49f, hatW = 12f, hand = Offset(84f, 70f))

    val bat = Beast(Figure {
        oval(50f, 56f, 8f, 12f)
        circle(50f, 42f, 7f)
        poly(45f, 38f, 44f, 28f, 49f, 36f)
        poly(55f, 38f, 56f, 28f, 51f, 36f)
        listOf(-1f, 1f).forEach { s ->
            poly(50f + s * 6f, 50f, 50f + s * 22f, 40f, 50f + s * 40f, 46f, 50f + s * 34f, 54f, 50f + s * 30f, 52f, 50f + s * 26f, 60f, 50f + s * 22f, 58f, 50f + s * 16f, 66f, 50f + s * 6f, 62f)
        }
        hole { circle(47.5f, 42f, 1.4f); circle(52.5f, 42f, 1.4f); poly(48f, 46f, 52f, 46f, 50f, 48f) }
    }, hatX = 50f, hatY = 36f, hatW = 13f, hand = Offset(84f, 70f))

    val all: Map<String, Beast> by lazy {
        linkedMapOf(
            "fox" to fox, "bear" to bear, "hare" to hare, "hedgehog" to hedgehog, "owl" to owl, "mouse" to mouse, "cat" to cat, "wolf" to wolf,
            "frog" to frog, "turtle" to turtle, "snail" to snail, "crow" to crow, "goat" to goat, "pig" to pig, "horse" to horse, "deer" to deer,
            "squirrel" to squirrel, "badger" to badger, "beaver" to beaver, "raccoon" to raccoon, "whale" to whale, "fish" to fish, "crab" to crab,
            "octopus" to octopus, "penguin" to penguin, "elephant" to elephant, "giraffe" to giraffe, "lion" to lion, "crocodile" to crocodile,
            "camel" to camel, "dragon" to dragon, "unicorn" to unicorn, "snowman" to snowman, "girl" to girl, "boy" to boy, "grandpa" to grandpa,
            "grandma" to grandma, "robot" to robot, "rooster" to rooster, "duck" to duck, "bee" to bee, "bat" to bat,
        )
    }
}
