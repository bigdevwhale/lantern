package app.lantern.domain

import app.lantern.content.Deck
import app.lantern.data.Tale
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.random.Random

/** Everyone the family has met in its tales, and everywhere it has been. */
object World {
    /** Someone met before: as they were last seen, in which tales, how many times. */
    data class Friend(val slide: Slide, val tales: List<Long>, val first: Long, val last: Long) {
        val count: Int get() = tales.size
    }

    fun friends(tales: List<Tale>): List<Friend> {
        val seen = LinkedHashMap<String, MutableList<Pair<Slide, Tale>>>()
        tales.sortedBy { it.endedAt }.forEach { t ->
            t.glass.filter { it.kind == Kind.HERO || it.kind == Kind.HELPER }.forEach { s -> seen.getOrPut(s.who) { mutableListOf() } += s to t }
        }
        return seen.values.map { l ->
            val (s, t) = l.last()
            // A helper who comes back is drawn as the hero they might be, without a skill.
            Friend(if (s.kind == Kind.HELPER) s.copy(kind = Kind.HERO, b = "") else s, l.map { it.second.id }.distinct(), l.first().second.endedAt, t.endedAt)
        }.sortedWith(compareByDescending<Friend> { it.count }.thenByDescending { it.last })
    }

    /** Glass that may come back into the deck: the most recent friends, in their last form. */
    fun returning(tales: List<Tale>, limit: Int = 24): List<Slide> =
        friends(tales).sortedByDescending { it.last }.take(limit).flatMap { f -> listOf(f.slide, f.slide.copy(kind = Kind.HELPER, b = Deck.skills[(f.last % Deck.skills.size).toInt()].key)) }

    /** Places in the order they were first visited, with how often. */
    fun visited(tales: List<Tale>): List<Pair<String, Int>> {
        val counts = LinkedHashMap<String, Int>()
        tales.sortedBy { it.endedAt }.forEach { t -> t.glass.filter { it.kind == Kind.PLACE && it.own == 0L }.forEach { counts[it.a] = (counts[it.a] ?: 0) + 1 } }
        return counts.toList()
    }

    /** Has this glass been seen in a tale before tonight? */
    fun isFriend(s: Slide, tales: List<Tale>): Boolean = (s.kind == Kind.HERO || s.kind == Kind.HELPER) && tales.any { t -> t.glass.any { it.who == s.who && (it.kind == Kind.HERO || it.kind == Kind.HELPER) } }
}

/**
 * The box of rare glass. Every evening with a tale earns a parcel; each parcel holds one rare
 * slide nobody has seen yet, until there are none left.
 */
object Parcels {
    fun earned(tales: List<Tale>, zone: ZoneId = ZoneId.systemDefault()): Int =
        tales.map { Instant.ofEpochMilli(it.endedAt).atZone(zone).toLocalDate() }.distinct().size

    fun locked(opened: Set<String>): List<String> = Deck.rares.filter { it !in opened }

    fun waiting(tales: List<Tale>, opened: Set<String>, zone: ZoneId = ZoneId.systemDefault()): Int = minOf((earned(tales, zone) - opened.size).coerceAtLeast(0), locked(opened).size)

    fun open(opened: Set<String>, random: Random): String? = locked(opened).randomOrNull(random)
}

/** The morning after a tale: the child tells it back. */
object Morning {
    /** Last night's tale, until one in the afternoon, if it hasn't been told back yet. */
    fun tale(tales: List<Tale>, now: Long, zone: ZoneId = ZoneId.systemDefault()): Tale? {
        val t = tales.maxByOrNull { it.endedAt } ?: return null
        if (t.retold) return null
        val at = Instant.ofEpochMilli(now).atZone(zone)
        val told = Instant.ofEpochMilli(t.endedAt).atZone(zone)
        val yesterdayEvening = told.toLocalDate() == at.toLocalDate().minusDays(1) && told.hour >= 16
        val thisMorning = told.toLocalDate() == at.toLocalDate() && told.hour < 5
        return t.takeIf { (yesterdayEvening || thisMorning) && at.hour < 13 }
    }

    /** Three questions for the tale, the same ones all morning. */
    fun questions(t: Tale): List<Int> {
        val r = Random(t.id * 13 + t.endedAt / 86_400_000L)
        return Deck.morning.indices.shuffled(r).take(3)
    }
}

/** Days in a row with a tale, today or yesterday counting. */
fun streak(tales: List<Tale>, today: LocalDate, zone: ZoneId = ZoneId.systemDefault()): Int {
    val days = tales.map { Instant.ofEpochMilli(it.endedAt).atZone(zone).toLocalDate().let { d -> if (Instant.ofEpochMilli(it.endedAt).atZone(zone).hour < 5) d.minusDays(1) else d } }.toSet()
    var d = if (today in days) today else today.minusDays(1)
    var n = 0
    while (d in days) { n++; d = d.minusDays(1) }
    return n
}
