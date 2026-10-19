package app.lantern.ui.art

import app.lantern.content.Deck
import app.lantern.domain.Kind
import app.lantern.domain.Slide

/** Which cut-out a slide shows. Built once per key and kept: paths are cheap to draw, not to make. */
object Art {
    private val cache = HashMap<String, Figure>()

    private fun cached(key: String, make: () -> Figure?): Figure? = synchronized(cache) { cache[key] } ?: make()?.also { synchronized(cache) { cache[key] = it } }

    /** "glyph:storm", "beast:dragon", "item:key", "place:volcano", "hat:crown", "thing:letter". */
    fun ref(r: String): Figure? {
        val (k, v) = r.split(':', limit = 2).let { it[0] to it.getOrElse(1) { "" } }
        return when (k) {
            "glyph" -> Glyphs.all[v]
            "beast" -> Beasts.all[v]?.art
            "item" -> Treasures.all[v]
            "place" -> Places.all[v]
            "hat" -> cached("bighat:$v") { Wardrobe.hats[v]?.let { h -> Figure { put(h, 50f, 70f, 1.6f, px = 50f, py = 66f) } } }
            "thing" -> cached("bigthing:$v") { Wardrobe.things[v]?.let { t -> Figure { put(t, 50f, 52f, 1.9f, px = 52f, py = 52f) } } }
            else -> null
        }
    }

    fun hero(creature: String, role: String): Figure? = cached("hero:$creature:$role") {
        val b = Beasts.all[creature] ?: return@cached null
        val r = Deck.role(role)
        if (r == null) b.art else Wardrobe.dressed(b, r.hat, r.thing)
    }

    /** The cut-out for a slide; a painted one comes from [own]. */
    fun of(s: Slide, own: (Long) -> Figure? = { null }): Figure? {
        if (s.own > 0) return own(s.own)
        return when (s.kind) {
            Kind.HERO -> hero(s.a, s.b)
            Kind.HELPER -> Beasts.all[s.a]?.art
            Kind.PLACE -> Places.all[s.a]
            Kind.ITEM -> Treasures.all[s.a]
            Kind.TROUBLE -> Deck.trouble(s.a)?.art?.let(::ref)
            Kind.QUIRK -> Deck.quirk(s.a)?.art?.let(::ref)
            Kind.TWIST -> Deck.twist(s.a)?.art?.let(::ref)
            Kind.ENDING -> Deck.ending(s.a)?.art?.let(::ref)
        }
    }

    /** A painted slide: the child's strokes, cut as one piece of card. See [Strokes]. */
    fun painted(id: Long, strokes: String): Figure = cached("own:$id:${strokes.hashCode()}") { Strokes.figure(strokes) }!!
}

/**
 * A drawing kept as text: strokes separated by ";", each "width:x,y,x,y…" on the 100-unit grid.
 * Short enough for the database and a backup file.
 */
object Strokes {
    data class Stroke(val width: Float, val points: List<Pair<Float, Float>>)

    fun parse(s: String): List<Stroke> = s.split(';').mapNotNull { part ->
        val w = part.substringBefore(':').toFloatOrNull() ?: return@mapNotNull null
        val nums = part.substringAfter(':').split(',').mapNotNull { it.toFloatOrNull() }
        if (nums.size < 2) null else Stroke(w, nums.chunked(2).filter { it.size == 2 }.map { it[0] to it[1] })
    }

    fun write(l: List<Stroke>): String = l.joinToString(";") { st ->
        "%.1f".format(java.util.Locale.ROOT, st.width) + ":" + st.points.joinToString(",") { (x, y) -> "%.1f,%.1f".format(java.util.Locale.ROOT, x, y) }
    }

    fun figure(s: String): Figure = Figure {
        parse(s).forEach { st ->
            if (st.width < 0) {
                // An eraser stroke cuts the card.
                hole { draw(st.copy(width = -st.width)) }
            } else draw(st)
        }
    }

    private fun Ink.draw(st: Stroke) {
        if (st.points.size == 1) circle(st.points[0].first, st.points[0].second, st.width / 2)
        else line(st.width, *st.points.flatMap { listOf(it.first, it.second) }.toFloatArray())
    }
}
