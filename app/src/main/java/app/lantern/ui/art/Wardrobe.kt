package app.lantern.ui.art

/**
 * Hats and things to hold: what turns a hedgehog into the postie. A hat is cut with the middle of
 * its brim at (50, 80) and the brim 40 across; a held thing has its grip at (50, 50).
 */
object Wardrobe {
    val crown = Figure {
        poly(31f, 80f, 28f, 56f, 39f, 67f, 44f, 50f, 50f, 64f, 56f, 50f, 61f, 67f, 72f, 56f, 69f, 80f)
        circle(28f, 55f, 2.6f); circle(44f, 49f, 2.6f); circle(56f, 49f, 2.6f); circle(72f, 55f, 2.6f)
        hole { circle(50f, 73f, 2.6f); circle(40f, 74f, 1.8f); circle(60f, 74f, 1.8f) }
    }

    val wizard = Figure {
        oval(50f, 79f, 25f, 4f)
        poly(34f, 79f, 46f, 44f, 44f, 28f, 30f, 16f, 50f, 24f, 58f, 44f, 66f, 79f)
        hole { star(50f, 62f, 5f); star(54f, 44f, 3f); circle(44f, 68f, 1.2f) }
    }

    val toque = Figure {
        rect(36f, 64f, 64f, 80f, 2f)
        circle(38f, 54f, 10f); circle(50f, 46f, 12f); circle(62f, 54f, 10f)
        hole { line(1f, 36f, 66f, 64f, 66f); line(0.8f, 44f, 70f, 44f, 78f); line(0.8f, 56f, 70f, 56f, 78f) }
    }

    val captain = Figure {
        blob(32f, 72f, 34f, 60f, 50f, 56f, 68f, 60f, 70f, 72f)
        rect(32f, 70f, 68f, 80f, 2f)
        poly(62f, 76f, 82f, 80f, 62f, 81f)
        hole {
            line(1f, 50f, 61f, 50f, 68f)
            line(1f, 47f, 63f, 53f, 63f)
            tube(1f, 46f, 66f, 50f, 69f, 54f, 66f)
        }
    }

    val sailor = Figure {
        rect(34f, 70f, 66f, 80f, 2f)
        oval(50f, 68f, 22f, 5f)
        taper(3f, 2.4f, 34f, 76f, 26f, 84f, 22f, 92f)
        taper(3f, 2.4f, 36f, 77f, 31f, 86f, 30f, 94f)
        hole { line(1f, 35f, 74f, 65f, 74f) }
    }

    val topHat = Figure {
        rect(38f, 44f, 62f, 78f, 1.5f)
        oval(50f, 79f, 24f, 3.2f)
        hole { rect(38.5f, 68f, 61.5f, 72f) }
    }

    val beret = Figure {
        oval(51f, 75f, 22f, 7f, -6f)
        tube(2f, 50f, 68f, 51f, 64f)
    }

    val tricorn = Figure {
        poly(22f, 79f, 32f, 58f, 50f, 64f, 68f, 58f, 78f, 79f, 50f, 74f)
        hole {
            circle(50f, 68f, 2.6f)
            line(0.9f, 45f, 74f, 55f, 72f); line(0.9f, 45f, 72f, 55f, 74f)
        }
    }

    val straw = Figure {
        oval(50f, 79f, 27f, 4f)
        blob(38f, 79f, 39f, 66f, 50f, 61f, 61f, 66f, 62f, 79f)
        hole { rect(38f, 72f, 62f, 75f) }
        circle(62f, 72f, 3f); circle(65f, 70f, 2.2f); circle(66f, 74f, 2.2f)
    }

    val pith = Figure {
        blob(32f, 77f, 34f, 64f, 50f, 57f, 66f, 64f, 68f, 77f)
        oval(50f, 79f, 25f, 3.6f)
        hole { tube(1f, 34f, 72f, 50f, 70f, 66f, 72f) }
        circle(50f, 57f, 2f)
    }

    val helmet = Figure {
        blob(32f, 80f, 32f, 64f, 42f, 54f, 58f, 54f, 68f, 64f, 70f, 80f)
        taper(6f, 1f, 42f, 56f, 34f, 42f, 24f, 38f, 18f, 44f)
        taper(5f, 1f, 46f, 55f, 42f, 40f, 34f, 32f)
        hole { rect(54f, 66f, 70f, 70f, 1f); line(1f, 50f, 58f, 50f, 78f) }
    }

    val nightcap = Figure {
        poly(32f, 80f, 68f, 80f, 64f, 64f, 54f, 50f, 40f, 46f, 26f, 54f, 18f, 66f, 30f, 60f, 40f, 60f, 36f, 70f)
        circle(17f, 68f, 4f)
        hole { (0..3).forEach { i -> circle(40f + i * 7f, 73f - i * 3f, 1.4f) } }
    }

    val wreath = Figure {
        tube(2f, 30f, 78f, 50f, 74f, 70f, 78f)
        listOf(32f, 41f, 50f, 59f, 68f).forEachIndexed { i, x ->
            val y = 77f - (if (i == 2) 4f else if (i % 2 == 1) 3f else 0f)
            star(x, y, 4.4f, 6, 0.5f)
        }
        hole { listOf(32f, 41f, 50f, 59f, 68f).forEachIndexed { i, x -> circle(x, 77f - (if (i == 2) 4f else if (i % 2 == 1) 3f else 0f), 1.2f) } }
    }

    val jester = Figure {
        rect(32f, 72f, 68f, 80f, 2f)
        poly(34f, 74f, 22f, 50f, 14f, 62f, 40f, 72f)
        poly(42f, 72f, 50f, 40f, 58f, 72f)
        poly(60f, 72f, 86f, 62f, 78f, 50f, 66f, 74f)
        circle(14f, 63f, 3f); circle(50f, 39f, 3f); circle(86f, 63f, 3f)
        hole { listOf(38f, 46f, 54f, 62f).forEach { x -> poly(x, 74f, x + 4f, 74f, x + 2f, 78f) } }
    }

    val cowboy = Figure {
        blob(36f, 78f, 37f, 62f, 44f, 58f, 50f, 62f, 56f, 58f, 63f, 62f, 64f, 78f)
        blob(22f, 74f, 30f, 79f, 50f, 81f, 70f, 79f, 78f, 74f, 74f, 82f, 50f, 84f, 26f, 82f)
        hole { rect(37f, 72f, 63f, 75f) }
    }

    val fireHelmet = Figure {
        blob(34f, 78f, 34f, 64f, 46f, 56f, 60f, 58f, 68f, 68f, 70f, 78f)
        poly(30f, 74f, 16f, 82f, 34f, 80f)
        poly(66f, 76f, 78f, 80f, 66f, 81f)
        hole { tube(1f, 40f, 62f, 48f, 58f, 58f, 60f) }
        rect(56f, 62f, 64f, 72f, 1.5f)
        hole { circle(60f, 67f, 1.6f) }
    }

    val deerstalker = Figure {
        blob(33f, 78f, 35f, 64f, 50f, 58f, 65f, 64f, 67f, 78f)
        poly(64f, 75f, 78f, 80f, 64f, 81f)
        poly(36f, 75f, 22f, 80f, 36f, 81f)
        circle(50f, 58f, 2f)
        hole { line(0.9f, 50f, 60f, 50f, 78f); line(0.9f, 42f, 62f, 42f, 78f); line(0.9f, 58f, 62f, 58f, 78f) }
    }

    val mortarboard = Figure {
        blob(36f, 80f, 36f, 70f, 64f, 70f, 64f, 80f)
        poly(22f, 68f, 50f, 60f, 78f, 68f, 50f, 74f)
        tube(1f, 50f, 66f, 68f, 70f, 70f, 80f)
        rect(68f, 79f, 72f, 86f, 1f)
    }

    val viking = Figure {
        blob(33f, 80f, 34f, 64f, 50f, 56f, 66f, 64f, 67f, 80f)
        taper(6f, 1f, 35f, 70f, 26f, 64f, 22f, 52f, 26f, 44f)
        taper(6f, 1f, 65f, 70f, 74f, 64f, 78f, 52f, 74f, 44f)
        hole { rect(34f, 72f, 66f, 75f); line(1f, 50f, 58f, 50f, 70f) }
    }

    val hats: Map<String, Figure> by lazy {
        linkedMapOf(
            "crown" to crown, "wizard" to wizard, "toque" to toque, "captain" to captain, "sailor" to sailor, "topHat" to topHat,
            "beret" to beret, "tricorn" to tricorn, "straw" to straw, "pith" to pith, "helmet" to helmet, "nightcap" to nightcap,
            "wreath" to wreath, "jester" to jester, "cowboy" to cowboy, "fireHelmet" to fireHelmet, "deerstalker" to deerstalker,
            "mortarboard" to mortarboard, "viking" to viking,
        )
    }

    // Things to hold.

    val lantern = Figure {
        tube(2f, 50f, 50f, 50f, 44f)
        tube(1.6f, 46f, 52f, 50f, 48f, 54f, 52f)
        poly(42f, 58f, 50f, 52f, 58f, 58f)
        rect(43f, 58f, 57f, 76f, 1.5f)
        poly(42f, 76f, 58f, 76f, 55f, 80f, 45f, 80f)
        hole { rect(45.5f, 60.5f, 49f, 66.5f); rect(51f, 60.5f, 54.5f, 66.5f); rect(45.5f, 68.5f, 49f, 74f); rect(51f, 68.5f, 54.5f, 74f) }
        oval(50f, 69f, 1.6f, 2.6f)
    }

    val bag = Figure {
        tube(1.6f, 48f, 44f, 56f, 52f, 60f, 60f)
        blob(44f, 58f, 70f, 58f, 72f, 76f, 42f, 76f)
        rect(46f, 52f, 60f, 62f, 1f)
        hole { line(0.8f, 46.5f, 52.5f, 53f, 57f, 59.5f, 52.5f); rect(44f, 64f, 70f, 66f) }
    }

    val rod = Figure {
        line(1.6f, 50f, 50f, 84f, 8f)
        tube(0.5f, 84f, 8f, 90f, 20f, 90f, 40f)
        blob(86f, 44f, 90f, 40f, 95f, 44f, 90f, 48f)
        poly(86f, 44f, 82f, 41f, 82f, 47f)
        circle(52f, 47f, 2.4f)
    }

    val umbrella = Figure {
        poly(26f, 30f, 30f, 16f, 50f, 6f, 70f, 16f, 74f, 30f)
        hole { (0..4).forEach { i -> circle(30.5f + i * 9.75f, 32.5f, 5f) } }
        line(1.6f, 50f, 8f, 50f, 64f)
        tube(1.6f, 50f, 64f, 50f, 68f, 46f, 70f, 43f, 66f)
        circle(50f, 5f, 1.4f)
    }

    val book = Figure {
        poly(30f, 48f, 50f, 52f, 70f, 48f, 70f, 68f, 50f, 72f, 30f, 68f)
        hole {
            line(0.9f, 49.6f, 52f, 49.6f, 71f)
            (0..2).forEach { i -> line(0.7f, 34f, 54f + i * 4f, 46f, 56f + i * 4f); line(0.7f, 54f, 56f + i * 4f, 66f, 54f + i * 4f) }
        }
    }

    val wand = Figure {
        line(2f, 44f, 60f, 64f, 30f)
        star(66f, 27f, 6f)
        star(76f, 20f, 2.2f); star(58f, 18f, 1.8f); star(74f, 36f, 1.6f)
    }

    val trumpet = Figure {
        line(2.4f, 36f, 50f, 64f, 44f)
        poly(62f, 44f, 76f, 36f, 78f, 56f, 62f, 47f)
        tube(1.6f, 44f, 48f, 46f, 42f, 54f, 41f, 56f, 46f)
        rect(46f, 43f, 48f, 50f); rect(50f, 42f, 52f, 49f); rect(54f, 41f, 56f, 48f)
    }

    val palette = Figure {
        blob(32f, 54f, 40f, 42f, 56f, 40f, 68f, 48f, 66f, 60f, 54f, 66f, 40f, 66f)
        line(1.6f, 50f, 70f, 72f, 30f)
        poly(70f, 30f, 76f, 24f, 74f, 32f)
        hole { circle(40f, 58f, 3f); circle(42f, 48f, 2.4f); circle(52f, 46f, 2.4f); circle(60f, 51f, 2.4f); circle(54f, 59f, 2.2f) }
    }

    val broom = Figure {
        line(1.8f, 44f, 30f, 54f, 66f)
        poly(51f, 64f, 58f, 62f, 66f, 84f, 48f, 88f)
        hole { line(0.7f, 55f, 70f, 56f, 86f); line(0.7f, 59f, 70f, 62f, 85f); line(0.7f, 52f, 70f, 51f, 86f) }
    }

    val key = Figure {
        circle(40f, 50f, 9f)
        line(3f, 48f, 50f, 72f, 50f)
        rect(64f, 50f, 67f, 58f); rect(69f, 50f, 72f, 56f)
        hole { circle(40f, 50f, 4f); star(40f, 50f, 0.1f) }
    }

    val basket = Figure {
        tube(1.8f, 38f, 60f, 40f, 44f, 50f, 38f, 60f, 44f, 62f, 60f)
        poly(32f, 58f, 68f, 58f, 64f, 78f, 36f, 78f)
        circle(44f, 56f, 5f); circle(55f, 55f, 5f)
        hole { line(0.8f, 34f, 64f, 66f, 64f); line(0.8f, 35f, 70f, 65f, 70f); (0..4).forEach { i -> line(0.8f, 40f + i * 5f, 60f, 40f + i * 5f, 76f) } }
    }

    val telescope = Figure {
        taper(5f, 8f, 34f, 62f, 56f, 48f)
        taper(8.6f, 10f, 56f, 48f, 72f, 38f)
        line(2f, 46f, 56f, 42f, 74f)
        line(2f, 46f, 56f, 52f, 74f)
        hole { line(0.8f, 55f, 45f, 58f, 52f) }
    }

    val sword = Figure {
        poly(52f, 48f, 80f, 20f, 84f, 16f, 80f, 24f, 54f, 52f)
        line(3f, 46f, 46f, 58f, 58f)
        line(3f, 52f, 52f, 44f, 60f)
        circle(42f, 62f, 2.4f)
    }

    val scroll = Figure {
        rect(36f, 46f, 66f, 64f, 1f)
        oval(36f, 55f, 3f, 10f); oval(66f, 55f, 3f, 10f)
        hole { tube(0.8f, 40f, 58f, 46f, 52f, 52f, 56f, 58f, 50f); line(0.8f, 59f, 51f, 62f, 54f); line(0.8f, 62f, 51f, 59f, 54f) }
    }

    val loaf = Figure {
        blob(32f, 56f, 40f, 46f, 60f, 44f, 72f, 50f, 70f, 60f, 50f, 64f)
        hole { listOf(42f, 50f, 58f, 66f).forEach { x -> line(1f, x - 2f, 55f, x + 2f, 48f) } }
    }

    val letter = Figure {
        rect(36f, 44f, 66f, 62f, 1f)
        hole { line(1f, 37f, 45f, 51f, 55f, 65f, 45f); circle(60f, 49f, 2f) }
    }

    val flower = Figure {
        tube(1.6f, 50f, 70f, 52f, 54f, 50f, 36f)
        taper(5f, 1f, 51f, 60f, 58f, 56f, 62f, 52f)
        (0..5).forEach { i -> val a = Math.PI / 3 * i; oval(50f + 5.4f * kotlin.math.cos(a).toFloat(), 30f + 5.4f * kotlin.math.sin(a).toFloat(), 3.6f, 3.6f) }
        hole { circle(50f, 30f, 2.4f) }
    }

    val bell = Figure {
        tube(1.6f, 50f, 50f, 50f, 46f)
        blob(40f, 70f, 42f, 56f, 50f, 50f, 58f, 56f, 60f, 70f, 64f, 74f, 36f, 74f)
        circle(50f, 77f, 2.6f)
        hole { tube(0.8f, 42f, 66f, 50f, 68f, 58f, 66f) }
    }

    val net = Figure {
        line(1.6f, 40f, 66f, 60f, 34f)
        oval(66f, 24f, 9f, 12f, 30f)
        hole { oval(66f, 24f, 7f, 10f, 30f) }
        tube(0.6f, 59f, 22f, 66f, 26f, 73f, 22f)
        tube(0.6f, 61f, 16f, 66f, 34f)
        tube(0.6f, 67f, 13f, 71f, 32f)
    }

    val wateringCan = Figure {
        blob(36f, 52f, 60f, 52f, 62f, 74f, 36f, 74f)
        taper(3.6f, 2f, 60f, 64f, 72f, 54f, 78f, 46f)
        poly(76f, 44f, 82f, 40f, 82f, 50f)
        tube(2f, 38f, 54f, 30f, 58f, 30f, 66f, 36f, 70f)
        hole { tube(0.9f, 40f, 60f, 48f, 62f, 56f, 60f) }
    }

    val magnifier = Figure {
        circle(62f, 36f, 11f)
        line(3.4f, 54f, 44f, 40f, 62f)
        hole { circle(62f, 36f, 8f) }
        tube(1f, 58f, 30f, 62f, 28f)
    }

    val bucket = Figure {
        poly(38f, 56f, 64f, 56f, 61f, 78f, 41f, 78f)
        tube(1.4f, 38f, 56f, 42f, 44f, 60f, 44f, 64f, 56f)
        hole { line(0.9f, 39f, 62f, 63f, 62f) }
        circle(46f, 52f, 2f); circle(54f, 50f, 1.6f)
    }

    val doctorBag = Figure {
        tube(2f, 44f, 54f, 46f, 46f, 56f, 46f, 58f, 54f)
        blob(34f, 56f, 66f, 56f, 68f, 76f, 32f, 76f)
        hole { rect(48f, 59f, 52f, 71f); rect(44f, 63f, 56f, 67f) }
    }

    val candle = Figure {
        oval(50f, 72f, 12f, 3f)
        rect(45f, 54f, 55f, 72f, 1f)
        tube(1.4f, 55f, 66f, 62f, 66f, 62f, 72f)
        taper(5f, 0.6f, 50f, 50f, 51f, 44f, 49f, 38f)
        hole { taper(1.6f, 0.4f, 50f, 50f, 50f, 45f) }
    }

    val bindle = Figure {
        line(1.8f, 40f, 60f, 70f, 28f)
        blob(66f, 34f, 74f, 26f, 84f, 30f, 84f, 40f, 74f, 44f)
        hole { circle(73f, 32f, 1.2f); circle(79f, 37f, 1.2f); circle(78f, 30f, 1f) }
    }

    val shield = Figure {
        blob(36f, 40f, 64f, 40f, 64f, 58f, 50f, 74f, 36f, 58f)
        hole { blob(40f, 44f, 60f, 44f, 60f, 57f, 50f, 69f, 40f, 57f) }
        star(50f, 54f, 6f)
    }

    val pocketWatch = Figure {
        circle(52f, 60f, 11f)
        rect(50f, 46f, 54f, 50f)
        circle(52f, 45f, 2f)
        tube(0.8f, 52f, 44f, 46f, 40f, 40f, 46f)
        hole {
            circle(52f, 60f, 8.4f)
        }
        line(1.4f, 52f, 60f, 52f, 54f)
        line(1.4f, 52f, 60f, 56f, 62f)
        (0..11).forEach { i -> val a = Math.PI / 6 * i; circle(52f + 7f * kotlin.math.cos(a).toFloat(), 60f + 7f * kotlin.math.sin(a).toFloat(), 0.6f) }
    }

    val quill = Figure {
        taper(6f, 0.6f, 44f, 62f, 54f, 44f, 70f, 26f, 80f, 16f)
        line(0.8f, 40f, 68f, 46f, 60f)
        hole { tube(0.6f, 48f, 54f, 60f, 38f, 74f, 22f) }
    }

    val things: Map<String, Figure> by lazy {
        linkedMapOf(
            "lantern" to lantern, "bag" to bag, "rod" to rod, "umbrella" to umbrella, "book" to book, "wand" to wand, "trumpet" to trumpet,
            "palette" to palette, "broom" to broom, "key" to key, "basket" to basket, "telescope" to telescope, "sword" to sword, "scroll" to scroll,
            "loaf" to loaf, "letter" to letter, "flower" to flower, "bell" to bell, "net" to net, "wateringCan" to wateringCan, "magnifier" to magnifier,
            "bucket" to bucket, "doctorBag" to doctorBag, "candle" to candle, "bindle" to bindle, "shield" to shield, "pocketWatch" to pocketWatch, "quill" to quill,
        )
    }

    /** A beast dressed for a role: the hat on its head, the thing in its paw. */
    fun dressed(b: Beast, hat: String?, thing: String?): Figure = Figure {
        put(b.art)
        hat?.let { hats[it] }?.let { put(it, b.hatX, b.hatY + 1f, b.hatW / 40f * 1.45f, rot = b.hatTilt, px = 50f, py = 80f) }
        thing?.let { things[it] }?.let { put(it, b.hand.x, b.hand.y, 0.72f, px = 50f, py = 50f) }
    }
}
