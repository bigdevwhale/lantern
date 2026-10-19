package app.lantern.data

import app.lantern.AppContainer
import app.lantern.domain.Box
import app.lantern.domain.Hand
import app.lantern.domain.Kind
import app.lantern.domain.Lantern
import app.lantern.domain.Own
import app.lantern.domain.Slide
import app.lantern.domain.World
import java.time.LocalDate
import kotlin.random.Random

/** Everything that happens to tonight's evening, from the first glass to the shelf. */
object Evenings {
    private suspend fun box(c: AppContainer, s: Settings): Box {
        val dao = c.db.dao()
        val own = dao.drawings().map { Own(it.id, it.kindOf) }
        val friends = if (s.friends) World.returning(dao.tales()) else emptyList()
        return Box(s.opened, LocalDate.now(), own, friends)
    }

    /**
     * Deals a new evening. With a [code] it's a shared one, drawn from the common box so the phone
     * at the other end deals the same glass. [keep] holds glass that stays, as in a sequel.
     */
    suspend fun deal(c: AppContainer, code: String = "", keep: Map<Kind, Slide> = emptyMap(), sequelOf: Long = 0, hand: Hand? = null) {
        val s = c.settings.now()
        val seed = if (code.isNotEmpty()) Lantern.Code.seed(code) else System.nanoTime()
        val h = (if (code.isNotEmpty()) Lantern.Code.hand(code, s.hand) else null) ?: hand ?: s.hand
        val box = if (code.isNotEmpty()) Box.common(LocalDate.now()) else box(c, s)
        val slides = Lantern.deal(h, box, Random(seed), keep)
        c.recorder.stop()
        c.settings.update { it.copy(evening = Evening(slides = slides, code = code, sequelOf = sequelOf, seed = seed)) }
    }

    /** Changes one glass for another of the same kind: once per glass, and a sequel keeps its hero. */
    suspend fun swap(c: AppContainer, index: Int) {
        val s = c.settings.now()
        val e = s.evening
        val old = e.slides.getOrNull(index) ?: return
        if (index in e.swapped) return
        val shared = e.code.isNotEmpty()
        val box = if (shared) Box.common(LocalDate.now()) else box(c, s)
        // A shared evening swaps the same way on both phones.
        val random = Random(if (shared) e.seed + 101 + index else System.nanoTime())
        val hand = if (shared) Lantern.Code.hand(e.code, s.hand) ?: s.hand else s.hand
        val fresh = Lantern.draw(old.kind, hand.copy(age = if (old.b.isEmpty() && old.kind == Kind.HERO) app.lantern.domain.Age.LITTLE else hand.age), box, random, e.slides.map { it.who }.toSet() + old.who)
        c.settings.evening { it.copy(slides = it.slides.toMutableList().also { l -> l[index] = fresh }, swapped = it.swapped + index) }
    }

    suspend fun start(c: AppContainer, record: Boolean) {
        val name = if (record) c.recorder.start() else null
        c.settings.evening { it.copy(startedAt = System.currentTimeMillis(), recording = name ?: "", step = 0) }
    }

    /** The next twist, kept with the evening. */
    suspend fun twist(c: AppContainer): Slide {
        val e = c.settings.now().evening
        val random = Random(if (e.code.isNotEmpty()) e.seed + 1000 + e.twists.size else System.nanoTime())
        val t = Lantern.twist(random, e.twists.map { it.a })
        c.settings.evening { it.copy(twists = it.twists + t) }
        return t
    }

    /** Puts the last twist back: it came at the wrong moment. */
    suspend fun untwist(c: AppContainer) = c.settings.evening { it.copy(twists = it.twists.dropLast(1)) }

    suspend fun step(c: AppContainer, to: Int) = c.settings.evening { it.copy(step = to) }

    /** The candle is out or the tale is told: draw the ending, stop the microphone. */
    suspend fun end(c: AppContainer): Slide {
        c.recorder.stop()
        val e = c.settings.now().evening
        e.ending?.let { return it }
        val random = Random(if (e.code.isNotEmpty()) e.seed + 5000 else System.nanoTime())
        val end = Lantern.ending(random)
        c.settings.evening { it.copy(ending = end) }
        return end
    }

    suspend fun reEnd(c: AppContainer) {
        val end = Lantern.ending(Random(System.nanoTime()))
        c.settings.evening { it.copy(ending = end) }
    }

    /** Puts the tale on the shelf and clears the evening. Returns the tale's id. */
    suspend fun save(c: AppContainer, title: String, note: String, tellers: List<String>): Long {
        c.recorder.stop()
        val e = c.settings.now().evening
        val now = System.currentTimeMillis()
        val id = c.db.dao().insert(
            Tale(
                title = title.trim(), slides = Slide.join(e.slides), twists = Slide.join(e.twists), ending = e.ending?.encode() ?: "",
                startedAt = if (e.startedAt > 0) e.startedAt else now, endedAt = now, note = note.trim(), tellers = tellers.joinToString(","),
                recording = e.recording, code = e.code,
            )
        )
        c.settings.update { it.copy(evening = Evening()) }
        c.changed()
        return id
    }

    /** Puts the glass away without a tale, deleting a recording if one was started. */
    suspend fun drop(c: AppContainer) {
        c.recorder.stop()
        val e = c.settings.now().evening
        if (e.recording.isNotEmpty()) c.recorder.file(e.recording).delete()
        c.settings.update { it.copy(evening = Evening()) }
    }
}
