package app.lantern.domain

import app.lantern.content.Deck
import app.lantern.content.G
import app.lantern.content.Season
import java.time.LocalDate
import java.time.Month
import kotlin.random.Random

/** What a glass slide shows. The order is the order they come out of the lantern. */
enum class Kind { HERO, PLACE, ITEM, TROUBLE, HELPER, QUIRK, TWIST, ENDING }

/**
 * One glass: [a] and [b] are deck keys (a creature and its role, a thing and its power), or
 * [own] is the id of a slide the child painted.
 */
data class Slide(val kind: Kind, val a: String, val b: String = "", val own: Long = 0) {
    fun encode(): String = "${kind.name}:$a:$b:$own"

    /** The same glass for the shelf and the world, whatever role it wore that night. */
    val who: String get() = if (own > 0) "own:$own" else a

    companion object {
        fun decode(s: String): Slide? {
            val p = s.split(':')
            if (p.size < 2) return null
            val kind = runCatching { Kind.valueOf(p[0]) }.getOrNull() ?: return null
            return Slide(kind, p[1], p.getOrElse(2) { "" }, p.getOrElse(3) { "0" }.toLongOrNull() ?: 0)
        }

        fun list(s: String): List<Slide> = s.split(';').filter { it.isNotBlank() }.mapNotNull { decode(it) }
        fun join(l: List<Slide>): String = l.joinToString(";") { it.encode() }
    }
}

/** How big the evening is. */
enum class Age {
    /** Three to five: a hero and a place, pictures only, and the thing if you like. */
    LITTLE,

    /** Six and up: the whole set. */
    BIG,
}

/** What the lantern deals tonight. */
data class Hand(
    val age: Age = Age.BIG,
    val item: Boolean = true,
    val trouble: Boolean = true,
    val helper: Boolean = true,
    val quirk: Boolean = false,
    /** 1 silly, 2 an adventure, 3 properly scary. */
    val fear: Int = 2,
) {
    val kinds: List<Kind>
        get() = when (age) {
            Age.LITTLE -> listOfNotNull(Kind.HERO, Kind.PLACE, Kind.ITEM.takeIf { item })
            Age.BIG -> listOfNotNull(Kind.HERO, Kind.PLACE, Kind.ITEM.takeIf { item }, Kind.TROUBLE.takeIf { trouble }, Kind.HELPER.takeIf { helper }, Kind.QUIRK.takeIf { quirk })
        }
}

/** A slide the child painted, as the deck sees it. */
data class Own(val id: Long, val kind: Kind)

/** The glass in the box: everything that isn't rare, the rare ones already opened, this season's. */
class Box(val opened: Set<String>, val today: LocalDate, val own: List<Own> = emptyList(), val friends: List<Slide> = emptyList()) {
    private val winter = today.month in setOf(Month.DECEMBER, Month.JANUARY, Month.FEBRUARY)
    private fun season(s: Season?) = s == null || (s == Season.WINTER && winter)

    val creatures = Deck.creatures.filter { (!it.rare || "creature:${it.key}" in opened) && season(it.season) }
    val roles = Deck.roles.filter { !it.rare || "role:${it.key}" in opened }
    val places = Deck.places.filter { (!it.rare || "place:${it.key}" in opened) && season(it.season) }
    val things = Deck.things.filter { !it.rare || "thing:${it.key}" in opened }

    companion object {
        /** Everyone's box: what a shared evening is drawn from, so two phones deal the same glass. */
        fun common(today: LocalDate) = Box(emptySet(), today)
    }
}

object Lantern {
    /** How often a painted slide or an old friend steps in for a drawn one. */
    const val OWN_CHANCE = 0.22
    const val FRIEND_CHANCE = 0.25

    /** Deals a fresh set of slides. [keep] holds glass that stays (the hero of a sequel). */
    fun deal(hand: Hand, box: Box, random: Random, keep: Map<Kind, Slide> = emptyMap()): List<Slide> {
        val taken = mutableSetOf<String>()
        return hand.kinds.map { k -> keep[k] ?: draw(k, hand, box, random, taken).also { taken += it.who } }
    }

    /** One slide of [kind], not one of [avoid] if there's a choice. */
    fun draw(kind: Kind, hand: Hand, box: Box, random: Random, avoid: Set<String> = emptySet()): Slide {
        val own = box.own.filter { it.kind == kind && "own:${it.id}" !in avoid }
        if (own.isNotEmpty() && random.nextDouble() < OWN_CHANCE) return Slide(kind, "", "", own.random(random).id)
        val friends = box.friends.filter { it.kind == kind && it.who !in avoid && (it.own > 0 || box.creatures.any { c -> c.key == it.a }) }
        if (friends.isNotEmpty() && random.nextDouble() < FRIEND_CHANCE) {
            val f = friends.random(random)
            // An old friend keeps who they are; on a big evening they may come in a new hat.
            return when {
                kind == Kind.HERO && hand.age == Age.BIG && f.own == 0L && random.nextBoolean() -> f.copy(b = box.roles.random(random).key)
                kind == Kind.HELPER && f.own == 0L -> f.copy(b = Deck.skills.random(random).key)
                else -> f
            }
        }
        fun <T> pick(l: List<T>, key: (T) -> String): T = l.filter { key(it) !in avoid }.ifEmpty { l }.random(random)
        return when (kind) {
            Kind.HERO -> {
                val c = pick(box.creatures) { it.key }
                Slide(kind, c.key, if (hand.age == Age.BIG) box.roles.random(random).key else "")
            }
            Kind.HELPER -> Slide(kind, pick(box.creatures) { it.key }.key, Deck.skills.random(random).key)
            Kind.PLACE -> Slide(kind, pick(box.places) { it.key }.key)
            Kind.ITEM -> Slide(kind, pick(box.things) { it.key }.key, if (hand.age == Age.BIG) Deck.powers.random(random).key else "")
            Kind.TROUBLE -> Slide(kind, pick(Deck.troubles.filter { it.fear <= hand.fear }) { it.key }.key)
            Kind.QUIRK -> Slide(kind, pick(Deck.quirks) { it.key }.key)
            Kind.TWIST -> Slide(kind, pick(Deck.twists) { it.key }.key)
            Kind.ENDING -> Slide(kind, pick(Deck.endings) { it.key }.key)
        }
    }

    /** The next twist, never one already shown tonight while there are others left. */
    fun twist(random: Random, shown: List<String>): Slide {
        val left = Deck.twists.filter { it.key !in shown }.ifEmpty { Deck.twists }
        return Slide(Kind.TWIST, left.random(random).key)
    }

    fun ending(random: Random) = Slide(Kind.ENDING, Deck.endings.random(random).key)

    /**
     * A shared evening: the code carries the size of the hand in its first digit, then four that
     * seed the deal. Both phones read it the same way and deal the same glass from the common box.
     */
    object Code {
        fun make(hand: Hand, random: Random): String = "${digit(hand)}${random.nextInt(1000, 10000)}"

        fun digit(hand: Hand) = when {
            hand.age == Age.LITTLE -> 1
            hand.quirk -> 3
            else -> 2
        }

        fun hand(code: String, base: Hand): Hand? {
            if (code.length != 5 || !code.all { it.isDigit() }) return null
            return when (code[0]) {
                '1' -> base.copy(age = Age.LITTLE, item = true)
                '2' -> base.copy(age = Age.BIG, item = true, trouble = true, helper = true, quirk = false, fear = 2)
                '3' -> base.copy(age = Age.BIG, item = true, trouble = true, helper = true, quirk = true, fear = 2)
                else -> null
            }
        }

        fun seed(code: String): Long = code.toLong() * 7919L

        fun pretty(code: String) = if (code.length == 5) "${code[0]}·${code.substring(1)}" else code
    }
}

/** Names for a slide, in one language. The UI and the widget both read slides through here. */
object Names {
    fun hero(a: String, b: String, ru: Boolean): String {
        val c = Deck.creature(a) ?: return a
        val name = c.name.of(ru)
        val role = Deck.role(b) ?: return name
        val r = role.of(ru, c.g)
        return when {
            !ru -> "$r $name"
            ' ' in r -> "$name — $r"
            else -> "$name-$r"
        }
    }

    /** The big line on the glass. Painted slides use their own name. */
    fun title(s: Slide, ru: Boolean, ownName: (Long) -> String = { "" }): String {
        if (s.own > 0) return ownName(s.own)
        return when (s.kind) {
            Kind.HERO -> hero(s.a, s.b, ru)
            Kind.HELPER -> Deck.creature(s.a)?.name?.of(ru) ?: s.a
            Kind.PLACE -> Deck.place(s.a)?.name?.of(ru) ?: s.a
            Kind.ITEM -> Deck.thing(s.a)?.name?.of(ru) ?: s.a
            Kind.TROUBLE -> Deck.trouble(s.a)?.text?.of(ru) ?: s.a
            Kind.QUIRK -> Deck.quirk(s.a)?.text?.of(ru) ?: s.a
            Kind.TWIST -> Deck.twist(s.a)?.text?.of(ru) ?: s.a
            Kind.ENDING -> Deck.ending(s.a)?.text?.of(ru) ?: s.a
        }
    }

    /** The small line under it: what the thing can do, what the helper knows. */
    fun sub(s: Slide, ru: Boolean): String = when (s.kind) {
        Kind.ITEM -> Deck.power(s.b)?.text?.of(ru).orEmpty()
        Kind.HELPER -> Deck.skill(s.b)?.text?.of(ru).orEmpty()
        else -> ""
    }

    /** "Fox the Captain and the Ice Palace": the default name of a tale. */
    fun tale(slides: List<Slide>, ru: Boolean, ownName: (Long) -> String = { "" }): String {
        val hero = slides.firstOrNull { it.kind == Kind.HERO }?.let { title(it, ru, ownName) }
        val place = slides.firstOrNull { it.kind == Kind.PLACE }?.let { title(it, ru, ownName) }
        val thing = slides.firstOrNull { it.kind == Kind.ITEM }?.let { title(it, ru, ownName) }
        val other = place ?: thing
        return when {
            hero != null && other != null -> if (ru) "$hero и ${lower(other, ru)}" else "$hero and ${theLower(other)}"
            hero != null -> hero
            else -> other.orEmpty()
        }
    }

    private fun lower(s: String, ru: Boolean) = if (ru && s.length > 1 && s[1].isLowerCase()) s.replaceFirstChar { it.lowercase() } else s

    /** "The Ice Palace" → "the Ice Palace"; "Lantern" → "the Lantern". */
    private fun theLower(s: String) = when {
        s.startsWith("The ") -> "the " + s.removePrefix("The ")
        s.startsWith("A ") -> "a " + s.removePrefix("A ")
        s == "Outer Space" || s == "The Moon" -> s
        else -> "the $s"
    }

    fun gender(s: Slide): G = Deck.creature(s.a)?.g ?: G.M
}
