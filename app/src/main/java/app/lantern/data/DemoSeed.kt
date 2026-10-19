package app.lantern.data

import android.content.Context
import app.lantern.AppContainer
import app.lantern.domain.Age
import app.lantern.domain.Hand
import app.lantern.domain.Kind
import app.lantern.domain.Slide
import app.lantern.ru
import app.lantern.ui.art.Strokes
import java.time.LocalDate
import java.time.ZoneId

/** A made-up family's three weeks of bedtime tales, for the screenshots (`-Pdemo`). */
object DemoSeed {
    private fun s(kind: Kind, a: String, b: String = "", own: Long = 0) = Slide(kind, a, b, own)

    suspend fun seedIfEmpty(context: Context, c: AppContainer) {
        val dao = c.db.dao()
        if (dao.tales().isNotEmpty() || c.settings.now().onboarded) return
        val ru = context.ru
        val zone = ZoneId.systemDefault()
        val today = LocalDate.now(zone)
        fun at(daysAgo: Int, h: Int, m: Int) = today.minusDays(daysAgo.toLong()).atTime(h, m).atZone(zone).toInstant().toEpochMilli()

        // A rocket the child drew, to show painted glass in the box and on the shelf.
        val rocket = Strokes.write(
            listOf(
                Strokes.Stroke(16f, listOf(50f to 22f, 50f to 70f)),
                Strokes.Stroke(8f, listOf(50f to 12f, 50f to 22f)),
                Strokes.Stroke(6f, listOf(42f to 62f, 32f to 78f)),
                Strokes.Stroke(6f, listOf(58f to 62f, 68f to 78f)),
                Strokes.Stroke(-6f, listOf(50f to 38f)),
                Strokes.Stroke(5f, listOf(46f to 76f, 44f to 86f)),
                Strokes.Stroke(5f, listOf(54f to 76f, 56f to 88f)),
                Strokes.Stroke(3f, listOf(22f to 24f)), Strokes.Stroke(3f, listOf(78f to 30f)), Strokes.Stroke(3f, listOf(70f to 14f)),
            )
        )
        val own = dao.insert(Drawing(name = if (ru) "Ракета Маши" else "Masha's Rocket", kind = Kind.ITEM.name, strokes = rocket, createdAt = at(9, 18, 30)))

        data class D(val ago: Int, val slides: List<Slide>, val twists: List<Slide>, val end: String, val title: String, val titleRu: String, val note: String, val noteRu: String, val min: Int, val retold: Boolean = false, val fav: Boolean = false, val tellers: String = "")
        val tales = listOf(
            D(20, listOf(s(Kind.HERO, "hedgehog", "postie"), s(Kind.PLACE, "forest"), s(Kind.ITEM, "lantern", "home"), s(Kind.TROUBLE, "lost"), s(Kind.HELPER, "owl", "roads")),
                listOf(s(Kind.TWIST, "footprints")), "blanket", "The Postie Hedgehog and the Dark Forest", "Ёжик-почтальон и тёмный лес",
                "A letter for the moon. Masha decided the owl was the moon's aunt.", "Письмо для луны. Маша решила, что сова — луне тётя.", 14, retold = true),
            D(18, listOf(s(Kind.HERO, "fox", "captain"), s(Kind.PLACE, "lighthouse"), s(Kind.ITEM, "compass", "truth"), s(Kind.TROUBLE, "storm"), s(Kind.HELPER, "whale", "swim")),
                listOf(s(Kind.TWIST, "map"), s(Kind.TWIST, "lonely")), "train", "Captain Fox and the Lighthouse", "Лиса-капитан и маяк",
                "The storm turned out to be lonely. They taught it to whistle.", "Буря оказалась одинокой. Её научили свистеть.", 18, fav = true),
            D(16, listOf(s(Kind.HERO, "mouse", "lamplighter"), s(Kind.PLACE, "rooftops"), s(Kind.ITEM, "starJar", "stars")),
                emptyList(), "moon", "The Lamplighter Mouse", "Мышка-фонарщица", "", "", 9),
            D(15, listOf(s(Kind.HERO, "bear", "baker"), s(Kind.PLACE, "grannyHouse"), s(Kind.ITEM, "spoon", "sweet"), s(Kind.TROUBLE, "birthday"), s(Kind.HELPER, "bee", "pies")),
                listOf(s(Kind.TWIST, "chocolate")), "milk", "Baker Bear and Granny's House", "Медведь-пекарь и бабушкин дом",
                "Pies with everything. Even with buttons.", "Пироги со всем на свете. Даже с пуговицами.", 12, retold = true, tellers = "Mum,Dad,Masha"),
            D(13, listOf(s(Kind.HERO, "hedgehog", "explorer"), s(Kind.PLACE, "moon"), s(Kind.ITEM, "", "", own), s(Kind.TROUBLE, "moon"), s(Kind.HELPER, "crow", "stars")),
                listOf(s(Kind.TWIST, "star")), "count", "Explorer Hedgehog and the Moon", "Ёжик-путешественник и луна",
                "The hedgehog again! Flew on Masha's rocket to find the moon.", "Снова ёжик! Полетел на Машиной ракете искать луну.", 16, fav = true),
            D(11, listOf(s(Kind.HERO, "dragon", "sleepyhead"), s(Kind.PLACE, "volcano"), s(Kind.ITEM, "pillow", "lullaby"), s(Kind.TROUBLE, "snore"), s(Kind.HELPER, "snail", "quiet")),
                emptyList(), "still", "The Sleepyhead Dragon", "Дракон-соня", "", "", 11),
            D(9, listOf(s(Kind.HERO, "girl", "detective"), s(Kind.PLACE, "library"), s(Kind.ITEM, "glasses", "past"), s(Kind.TROUBLE, "socks"), s(Kind.HELPER, "cat", "secrets")),
                listOf(s(Kind.TWIST, "pocket"), s(Kind.TWIST, "letter")), "book", "The Detective and the Endless Library", "Девочка-сыщица и бесконечная библиотека",
                "The socks were in the giant's library, being read.", "Носки нашлись в библиотеке — их там читали.", 21, tellers = "Mum,Masha"),
            D(7, listOf(s(Kind.HERO, "frog", "king"), s(Kind.PLACE, "swamp"), s(Kind.ITEM, "bell", "danger")),
                listOf(s(Kind.TWIST, "lights")), "windows", "Queen Frog and the Misty Swamp", "Лягушка-королева и туманное болото", "", "", 8),
            D(6, listOf(s(Kind.HERO, "penguin", "wanderer"), s(Kind.PLACE, "northPole"), s(Kind.ITEM, "snowGlobe", "world"), s(Kind.TROUBLE, "winter"), s(Kind.HELPER, "bear", "snack")),
                listOf(s(Kind.TWIST, "whale")), "snow", "The Wandering Penguin", "Пингвин-странник", "Baker Bear came back with pies for the road.", "Вернулся Медведь-пекарь — с пирогами в дорогу.", 15),
            D(4, listOf(s(Kind.HERO, "robot", "gardener"), s(Kind.PLACE, "giantGarden"), s(Kind.ITEM, "seed", "grow"), s(Kind.TROUBLE, "colours"), s(Kind.HELPER, "hedgehog", "friends")),
                listOf(s(Kind.TWIST, "rainbow")), "forever", "The Gardener Robot", "Робот-садовник", "", "", 17, retold = true),
            D(3, listOf(s(Kind.HERO, "unicorn", "painter"), s(Kind.PLACE, "rainbow"), s(Kind.ITEM, "pencil", "door")),
                emptyList(), "tomorrow", "The Painter Unicorn", "Единорог-художник", "", "", 10),
            D(2, listOf(s(Kind.HERO, "owl", "stargazer"), s(Kind.PLACE, "space"), s(Kind.ITEM, "telescope", "far"), s(Kind.TROUBLE, "stars"), s(Kind.HELPER, "fox", "listen")),
                listOf(s(Kind.TWIST, "moon"), s(Kind.TWIST, "time")), "owl", "The Stargazer Owl in Outer Space", "Сова-звездочёт и космос",
                "Every star has a name. We gave three of them ours.", "У каждой звезды есть имя. Трём мы дали свои.", 19, fav = true),
            D(1, listOf(s(Kind.HERO, "hare", "knight"), s(Kind.PLACE, "castle"), s(Kind.ITEM, "yarn", "home"), s(Kind.TROUBLE, "giant"), s(Kind.HELPER, "mouse", "hide")),
                listOf(s(Kind.TWIST, "friends")), "carried", "Knight Hare and the Castle on the Hill", "Заяц-рыцарь и замок на холме",
                "The giant just wanted someone to play chess with.", "Великану просто не с кем было играть в шашки.", 16),
        )
        tales.forEach { d ->
            val end = at(d.ago, 20, 48 - (d.ago % 5) * 3)
            dao.insert(
                Tale(
                    title = if (ru) d.titleRu else d.title, slides = Slide.join(d.slides), twists = Slide.join(d.twists), ending = Slide(Kind.ENDING, d.end).encode(),
                    startedAt = end - d.min * 60_000L, endedAt = end, note = if (ru) d.noteRu else d.note,
                    tellers = if (ru) d.tellers.replace("Mum", "Мама").replace("Dad", "Папа").replace("Masha", "Маша") else d.tellers,
                    retold = d.retold, favourite = d.fav,
                )
            )
        }
        c.settings.update {
            it.copy(
                onboarded = true,
                hand = Hand(age = Age.BIG, quirk = false, fear = 2),
                tellers = if (ru) listOf("Мама", "Папа", "Маша") else listOf("Mum", "Dad", "Masha"),
                opened = setOf("creature:dragon", "creature:whale", "creature:robot", "creature:unicorn", "role:stargazer", "place:volcano", "place:space", "thing:starJar", "thing:snowGlobe", "role:keykeeper"),
            )
        }
    }
}
