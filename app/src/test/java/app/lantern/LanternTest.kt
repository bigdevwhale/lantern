package app.lantern

import app.lantern.content.Deck
import app.lantern.data.Backup
import app.lantern.data.Drawing
import app.lantern.data.Everything
import app.lantern.data.Tale
import app.lantern.domain.Age
import app.lantern.domain.Box
import app.lantern.domain.Hand
import app.lantern.domain.Kind
import app.lantern.domain.Lantern
import app.lantern.domain.Morning
import app.lantern.domain.Names
import app.lantern.domain.Parcels
import app.lantern.domain.Slide
import app.lantern.domain.World
import app.lantern.ui.art.Art
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.random.Random

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LanternTest {
    private val october = LocalDate.of(2026, 10, 19)

    @Test
    fun everyGlassHasArtAndWords() {
        Deck.creatures.forEach { assertNotNull(it.key, Art.hero(it.key, "")) }
        Deck.roles.forEach { assertNotNull(it.key, Art.hero("fox", it.key)) }
        Deck.places.forEach { assertNotNull(it.key, Art.of(Slide(Kind.PLACE, it.key))) }
        Deck.things.forEach { assertNotNull(it.key, Art.of(Slide(Kind.ITEM, it.key))) }
        (Deck.troubles + Deck.quirks + Deck.twists + Deck.endings).forEach { assertNotNull(it.key, Art.ref(it.art)) }
    }

    @Test
    fun aBigHandDealsEveryKindOnce() {
        val hand = Hand(age = Age.BIG, quirk = true)
        val slides = Lantern.deal(hand, Box(emptySet(), october), Random(1))
        assertEquals(listOf(Kind.HERO, Kind.PLACE, Kind.ITEM, Kind.TROUBLE, Kind.HELPER, Kind.QUIRK), slides.map { it.kind })
        assertTrue(slides.first().b.isNotEmpty())
        // The hero and the helper aren't the same creature.
        assertTrue(slides[0].a != slides[4].a)
    }

    @Test
    fun littleOnesGetABareHeroAndNoRareOrWinterGlass() {
        repeat(200) { i ->
            val s = Lantern.deal(Hand(age = Age.LITTLE), Box(emptySet(), october), Random(i))
            assertEquals(3, s.size)
            assertEquals("", s[0].b)
            assertTrue(Deck.creature(s[0].a)!!.let { !it.rare && it.season == null })
            assertTrue(!Deck.place(s[1].a)!!.rare)
        }
    }

    @Test
    fun fearKeepsTheTroublesGentle() {
        repeat(200) { i ->
            val t = Lantern.draw(Kind.TROUBLE, Hand(fear = 1), Box(emptySet(), october), Random(i))
            assertEquals(1, Deck.trouble(t.a)!!.fear)
        }
    }

    @Test
    fun twoPhonesWithOneCodeDealTheSameGlass() {
        val code = Lantern.Code.make(Hand(quirk = true), Random(5))
        assertEquals('3', code[0])
        val hand = Lantern.Code.hand(code, Hand())!!
        val a = Lantern.deal(hand, Box.common(october), Random(Lantern.Code.seed(code)))
        val b = Lantern.deal(Lantern.Code.hand(code, Hand(age = Age.LITTLE))!!, Box.common(october), Random(Lantern.Code.seed(code)))
        assertEquals(a, b)
        assertNull(Lantern.Code.hand("9123", Hand()))
    }

    @Test
    fun namesAgreeInGender() {
        assertEquals("Лиса-королева", Names.hero("fox", "king", true))
        assertEquals("Медведь-король", Names.hero("bear", "king", true))
        assertEquals("Queen Fox", Names.hero("fox", "king", false))
        assertEquals("Сова — хранительница цветов", Names.hero("owl", "flowerKeeper", true))
        val tale = listOf(Slide(Kind.HERO, "fox", "captain"), Slide(Kind.PLACE, "icePalace"))
        assertEquals("Лиса-капитан и ледяной дворец", Names.tale(tale, true))
        assertEquals("Captain Fox and the Ice Palace", Names.tale(tale, false))
    }

    private fun tale(id: Long, day: LocalDate, hour: Int, vararg s: Slide) = Tale(
        id = id, title = "t$id", slides = Slide.join(s.toList()),
        startedAt = day.atTime(hour, 0).toInstant(ZoneOffset.UTC).toEpochMilli() - 600_000,
        endedAt = day.atTime(hour, 0).toInstant(ZoneOffset.UTC).toEpochMilli(),
    )

    @Test
    fun friendsAreCountedAcrossTales() {
        val tales = listOf(
            tale(1, october.minusDays(2), 20, Slide(Kind.HERO, "hedgehog", "postie"), Slide(Kind.HELPER, "owl", "roads")),
            tale(2, october.minusDays(1), 20, Slide(Kind.HERO, "hedgehog", "explorer"), Slide(Kind.PLACE, "moon")),
        )
        val f = World.friends(tales)
        assertEquals("hedgehog", f.first().slide.a)
        assertEquals(2, f.first().count)
        assertEquals("explorer", f.first().slide.b)
        assertTrue(World.isFriend(Slide(Kind.HELPER, "owl", "jokes"), tales))
        assertEquals(listOf("moon" to 1), World.visited(tales))
    }

    @Test
    fun aParcelForEveryEveningUntilTheBoxIsFull() {
        val tales = (1..3).map { tale(it.toLong(), october.minusDays(it.toLong()), 20) } + tale(9, october.minusDays(1), 21)
        assertEquals(3, Parcels.waiting(tales, emptySet(), ZoneOffset.UTC))
        assertEquals(1, Parcels.waiting(tales, setOf("creature:dragon", "creature:whale"), ZoneOffset.UTC))
        assertEquals(0, Parcels.waiting(tales, Deck.rares.toSet()))
    }

    @Test
    fun theMorningAfterUntilOne() {
        val t = tale(1, october.minusDays(1), 20)
        val zone = ZoneOffset.UTC
        val morning = october.atTime(8, 0).toInstant(zone).toEpochMilli()
        val afternoon = october.atTime(14, 0).toInstant(zone).toEpochMilli()
        assertEquals(t, Morning.tale(listOf(t), morning, zone))
        assertNull(Morning.tale(listOf(t), afternoon, zone))
        assertNull(Morning.tale(listOf(t.copy(retold = true)), morning, zone))
    }

    @Test
    fun backupRoundTrip() {
        val all = Everything(
            listOf(tale(3, october, 20, Slide(Kind.HERO, "fox", "captain"), Slide(Kind.ITEM, "", "", 7)).copy(note = "«Ура»", favourite = true)),
            listOf(Drawing(id = 7, name = "Ракета", kind = Kind.ITEM.name, strokes = "8.0:50.0,20.0,50.0,70.0")),
        )
        val (back, opened) = Backup.decode(Backup.encode(all, setOf("creature:dragon")))
        assertEquals(all.tales, back.tales)
        assertEquals(all.drawings.map { it.copy(createdAt = 0) }, back.drawings.map { it.copy(createdAt = 0) })
        assertEquals(setOf("creature:dragon"), opened)
    }
}
