package app.lantern.content

/** A word or a line in both languages. */
class W(val en: String, val ru: String) {
    fun of(ru: Boolean) = if (ru) this.ru else en
}

/** Grammatical gender, so a fox becomes «Лиса-королева» and a bear «Медведь-король». */
enum class G { M, F }

/** Only some glass is in the box all year round. */
enum class Season { WINTER }

/** Who can be the hero or the helper. [art] is a key in the beasts. */
class Creature(val key: String, val name: W, val g: G, val rare: Boolean = false, val season: Season? = null)

/** What the hero does: a hat and a thing to hold, and the word for it in both genders. */
class Role(val key: String, val hat: String?, val thing: String?, val en: String, val ru: String, val enF: String = en, val ruF: String = ru, val rare: Boolean = false) {
    fun of(ru: Boolean, g: G) = if (ru) (if (g == G.F) ruF else this.ru) else (if (g == G.F) enF else en)
}

class Place(val key: String, val name: W, val rare: Boolean = false, val season: Season? = null)

/** A magic thing; its art is the treasure of the same key. */
class Thing(val key: String, val name: W, val rare: Boolean = false)

/**
 * A card that is only words and a picture: a power, a trouble, a twist. [art] names the picture
 * ("glyph:storm", "beast:dragon", "item:key", "place:volcano", "hat:crown", "thing:letter").
 */
class Line(val key: String, val text: W, val art: String = "", val fear: Int = 1)

/** One of Propp's steps, put for a parent: what happens, and what to ask the child. */
class Step(val key: String, val title: W, val hint: W, val ask: W)

object Deck {
    val creatures = listOf(
        Creature("fox", W("Fox", "Лиса"), G.F),
        Creature("bear", W("Bear", "Медведь"), G.M),
        Creature("hare", W("Hare", "Заяц"), G.M),
        Creature("hedgehog", W("Hedgehog", "Ёжик"), G.M),
        Creature("owl", W("Owl", "Сова"), G.F),
        Creature("mouse", W("Mouse", "Мышка"), G.F),
        Creature("cat", W("Cat", "Кот"), G.M),
        Creature("wolf", W("Wolf", "Волк"), G.M),
        Creature("frog", W("Frog", "Лягушка"), G.F),
        Creature("turtle", W("Tortoise", "Черепаха"), G.F),
        Creature("snail", W("Snail", "Улитка"), G.F),
        Creature("crow", W("Crow", "Ворона"), G.F),
        Creature("goat", W("Goat", "Коза"), G.F),
        Creature("pig", W("Piglet", "Поросёнок"), G.M),
        Creature("horse", W("Pony", "Лошадка"), G.F),
        Creature("deer", W("Stag", "Олень"), G.M),
        Creature("squirrel", W("Squirrel", "Белка"), G.F),
        Creature("badger", W("Badger", "Барсук"), G.M),
        Creature("beaver", W("Beaver", "Бобр"), G.M),
        Creature("raccoon", W("Raccoon", "Енот"), G.M),
        Creature("whale", W("Whale", "Кит"), G.M, rare = true),
        Creature("fish", W("Goldfish", "Рыбка"), G.F),
        Creature("crab", W("Crab", "Краб"), G.M),
        Creature("octopus", W("Octopus", "Осьминог"), G.M, rare = true),
        Creature("penguin", W("Penguin", "Пингвин"), G.M),
        Creature("elephant", W("Elephant", "Слон"), G.M),
        Creature("giraffe", W("Giraffe", "Жираф"), G.M),
        Creature("lion", W("Lion", "Лев"), G.M),
        Creature("crocodile", W("Crocodile", "Крокодил"), G.M),
        Creature("camel", W("Camel", "Верблюд"), G.M, rare = true),
        Creature("dragon", W("Dragon", "Дракон"), G.M, rare = true),
        Creature("unicorn", W("Unicorn", "Единорог"), G.M, rare = true),
        Creature("snowman", W("Snowman", "Снеговик"), G.M, season = Season.WINTER),
        Creature("girl", W("Girl", "Девочка"), G.F),
        Creature("boy", W("Boy", "Мальчик"), G.M),
        Creature("grandpa", W("Grandpa", "Дедушка"), G.M),
        Creature("grandma", W("Granny", "Бабушка"), G.F),
        Creature("robot", W("Robot", "Робот"), G.M, rare = true),
        Creature("rooster", W("Rooster", "Петух"), G.M),
        Creature("duck", W("Duckling", "Уточка"), G.F),
        Creature("bee", W("Bee", "Пчёлка"), G.F),
        Creature("bat", W("Bat", "Летучая мышка"), G.F, rare = true),
    )

    val roles = listOf(
        Role("king", "crown", null, "King", "король", "Queen", "королева"),
        Role("wizard", "wizard", "wand", "Wizard", "волшебник", "Wizard", "волшебница"),
        Role("captain", "captain", "telescope", "Captain", "капитан"),
        Role("baker", "toque", "loaf", "Baker", "пекарь"),
        Role("postie", "captain", "bag", "Postie", "почтальон"),
        Role("explorer", "pith", "scroll", "Explorer", "путешественник", "Explorer", "путешественница"),
        Role("musician", null, "trumpet", "Trumpeter", "трубач"),
        Role("painter", "beret", "palette", "Painter", "художник", "Painter", "художница"),
        Role("detective", "deerstalker", "magnifier", "Detective", "сыщик", "Detective", "сыщица"),
        Role("firefighter", "fireHelmet", "bucket", "Firefighter", "пожарный"),
        Role("gardener", "straw", "wateringCan", "Gardener", "садовник", "Gardener", "садовница"),
        Role("fisher", "sailor", "rod", "Fisher", "рыбак", "Fisher", "рыбачка"),
        Role("pirate", "tricorn", "sword", "Pirate", "пират", "Pirate", "пиратка"),
        Role("knight", "helmet", "shield", "Knight", "рыцарь"),
        Role("lamplighter", "topHat", "lantern", "Lamplighter", "фонарщик", "Lamplighter", "фонарщица"),
        Role("doctor", null, "doctorBag", "Doctor", "доктор"),
        Role("sleepyhead", "nightcap", "candle", "Sleepyhead", "соня"),
        Role("scholar", "mortarboard", "book", "Professor", "профессор"),
        Role("clockmaker", null, "pocketWatch", "Clockmaker", "часовщик", "Clockmaker", "часовщица"),
        Role("wanderer", "cowboy", "bindle", "Wanderer", "странник", "Wanderer", "странница"),
        Role("storyteller", "beret", "quill", "Storyteller", "сказочник", "Storyteller", "сказочница"),
        Role("flowerKeeper", "wreath", "flower", "Flower-keeper", "хранитель цветов", "Flower-keeper", "хранительница цветов"),
        Role("stargazer", "wizard", "telescope", "Stargazer", "звездочёт", rare = true),
        Role("jester", "jester", "bell", "Jester", "шут", "Jester", "шутиха", rare = true),
        Role("viking", "viking", "sword", "Viking", "викинг", rare = true),
        Role("sweep", "topHat", "broom", "Chimney Sweep", "трубочист", rare = true),
        Role("dreamCatcher", "nightcap", "net", "Dream-catcher", "ловец снов", rare = true),
        Role("keykeeper", "crown", "key", "Keeper of Keys", "ключник", "Keeper of Keys", "ключница", rare = true),
    )

    val places = listOf(
        Place("forest", W("The Dark Forest", "Тёмный лес")),
        Place("castle", W("The Castle on the Hill", "Замок на холме")),
        Place("lighthouse", W("The Lighthouse at the Edge of the Sea", "Маяк на краю моря")),
        Place("island", W("A Desert Island", "Необитаемый остров")),
        Place("mountain", W("The Top of the Highest Mountain", "Вершина самой высокой горы")),
        Place("moon", W("The Moon", "Луна")),
        Place("mill", W("The Old Mill", "Старая мельница")),
        Place("village", W("The Sleepy Village", "Сонная деревня")),
        Place("library", W("The Endless Library", "Бесконечная библиотека")),
        Place("grannyHouse", W("Granny's House", "Бабушкин дом")),
        Place("desert", W("The Desert of Pyramids", "Пустыня пирамид")),
        Place("jungle", W("The Jungle", "Джунгли")),
        Place("swamp", W("The Misty Swamp", "Туманное болото")),
        Place("cave", W("The Treasure Cave", "Пещера сокровищ")),
        Place("nightTrain", W("The Night Train", "Ночной поезд")),
        Place("ship", W("The Pirate Ship", "Пиратский корабль")),
        Place("balloonSky", W("A Hot-Air Balloon", "Воздушный шар")),
        Place("giantGarden", W("The Giants' Garden", "Сад великанов")),
        Place("circus", W("The Travelling Circus", "Бродячий цирк")),
        Place("clockTower", W("The Clock Tower", "Часовая башня")),
        Place("bridge", W("The Bridge over the River", "Мост через реку")),
        Place("treehouse", W("The Treehouse", "Дом на дереве")),
        Place("northPole", W("The North Pole", "Северный полюс")),
        Place("rooftops", W("The Rooftops at Night", "Ночные крыши")),
        Place("underBed", W("Under the Bed", "Под кроватью")),
        Place("rainbow", W("The End of the Rainbow", "Конец радуги")),
        Place("burrow", W("The Burrow under the Roots", "Нора под корнями")),
        Place("underwater", W("The Underwater Kingdom", "Подводное царство"), rare = true),
        Place("cloudCity", W("The City on the Clouds", "Город на облаках"), rare = true),
        Place("volcano", W("The Sleeping Volcano", "Спящий вулкан"), rare = true),
        Place("teapotHouse", W("The House in a Teapot", "Домик в чайнике"), rare = true),
        Place("well", W("The Bottom of the Well", "Дно колодца"), rare = true),
        Place("space", W("Outer Space", "Космос"), rare = true),
        Place("icePalace", W("The Ice Palace", "Ледяной дворец"), season = Season.WINTER),
    )

    val things = listOf(
        Thing("key", W("Key", "Ключ")),
        Thing("lantern", W("Lantern", "Фонарь")),
        Thing("umbrella", W("Umbrella", "Зонтик")),
        Thing("book", W("Book", "Книга")),
        Thing("bell", W("Bell", "Колокольчик")),
        Thing("candle", W("Candle", "Свеча")),
        Thing("clock", W("Pocket watch", "Часы")),
        Thing("feather", W("Feather", "Перо")),
        Thing("telescope", W("Telescope", "Подзорная труба")),
        Thing("teapot", W("Teapot", "Чайник")),
        Thing("spoon", W("Spoon", "Ложка")),
        Thing("boot", W("Boot", "Сапог")),
        Thing("hat", W("Top hat", "Шляпа")),
        Thing("mirror", W("Mirror", "Зеркальце")),
        Thing("compass", W("Compass", "Компас")),
        Thing("hourglass", W("Hourglass", "Песочные часы")),
        Thing("map", W("Map", "Карта")),
        Thing("egg", W("Egg", "Яйцо")),
        Thing("apple", W("Apple", "Яблоко")),
        Thing("acorn", W("Acorn", "Жёлудь")),
        Thing("shell", W("Seashell", "Ракушка")),
        Thing("button", W("Button", "Пуговица")),
        Thing("spool", W("Spool of thread", "Катушка ниток")),
        Thing("balloon", W("Balloon", "Воздушный шарик")),
        Thing("paperBoat", W("Paper boat", "Бумажный кораблик")),
        Thing("kite", W("Kite", "Воздушный змей")),
        Thing("ladder", W("Ladder", "Лестница")),
        Thing("bucket", W("Bucket", "Ведёрко")),
        Thing("flute", W("Pipe", "Дудочка")),
        Thing("drum", W("Drum", "Барабан")),
        Thing("pebble", W("Pebble with a hole", "Камешек с дырочкой")),
        Thing("comb", W("Comb", "Гребешок")),
        Thing("pillow", W("Pillow", "Подушка")),
        Thing("pencil", W("Pencil", "Карандаш")),
        Thing("yarn", W("Ball of yarn", "Клубочек")),
        Thing("broom", W("Broom", "Метла")),
        Thing("glasses", W("Glasses", "Очки")),
        Thing("bottle", W("Bottle", "Бутылочка")),
        Thing("seed", W("Seed", "Семечко")),
        Thing("wateringCan", W("Watering can", "Лейка")),
        Thing("net", W("Butterfly net", "Сачок")),
        Thing("trumpet", W("Trumpet", "Труба")),
        Thing("carpet", W("Carpet", "Ковёр"), rare = true),
        Thing("starJar", W("Star in a jar", "Звезда в банке"), rare = true),
        Thing("snowGlobe", W("Snow globe", "Снежный шар"), rare = true),
        Thing("musicBox", W("Music box", "Музыкальная шкатулка"), rare = true),
        Thing("chest", W("Little chest", "Сундучок"), rare = true),
        Thing("ring", W("Ring", "Колечко"), rare = true),
    )

    /** What a magic thing can do. Present tense, so it needs no gender in Russian. */
    val powers = listOf(
        Line("wish", W("grants one wish", "исполняет одно желание")),
        Line("home", W("always shows the way home", "всегда показывает дорогу домой")),
        Line("hide", W("hides you from every eye", "прячет от чужих глаз")),
        Line("talk", W("talks, and has opinions", "разговаривает человеческим голосом")),
        Line("animals", W("lets you understand animals", "помогает понимать язык зверей")),
        Line("tiny", W("shrinks whatever it touches", "уменьшает всё, к чему прикоснётся")),
        Line("huge", W("makes whatever it touches huge", "увеличивает всё, к чему прикоснётся")),
        Line("back", W("turns time back by one minute", "отматывает время на минуту назад")),
        Line("fly", W("flies wherever you ask", "летит туда, куда попросишь")),
        Line("lost", W("finds anything that's lost", "находит всё потерянное")),
        Line("lullaby", W("sings lullabies", "поёт колыбельные")),
        Line("glow", W("glows when friends are near", "светится, когда рядом друзья")),
        Line("laugh", W("makes anyone laugh", "смешит кого угодно")),
        Line("stars", W("lights up the stars", "зажигает звёзды на небе")),
        Line("door", W("opens any door", "открывает любую дверь")),
        Line("snow", W("makes it snow", "вызывает снегопад")),
        Line("truth", W("can only tell the truth", "умеет говорить только правду")),
        Line("remember", W("remembers everything", "помнит всё на свете")),
        Line("friends", W("makes quarrellers friends again", "мирит поссорившихся")),
        Line("sweet", W("makes everything sweet", "делает всё сладким")),
        Line("brave", W("makes you a little braver", "прибавляет немножко смелости")),
        Line("wind", W("calls the wind", "зовёт ветер")),
        Line("three", W("works only three times", "срабатывает только три раза")),
        Line("children", W("obeys only children", "слушается только детей")),
        Line("danger", W("rings when trouble is near", "звенит, когда рядом беда")),
        Line("world", W("keeps a tiny world inside", "хранит внутри крошечный мир")),
        Line("mood", W("changes colour with your mood", "меняет цвет от настроения")),
        Line("repair", W("mends anything broken", "чинит всё сломанное")),
        Line("grow", W("grows a tree in a minute", "выращивает дерево за минуту")),
        Line("voices", W("swaps everybody's voices", "меняет всем голоса")),
        Line("dreams", W("brings good dreams", "приносит хорошие сны")),
        Line("water", W("lets you breathe under water", "помогает дышать под водой")),
        Line("copy", W("makes a copy of anything", "делает копию чего угодно")),
        Line("past", W("shows what happened yesterday", "показывает, что было вчера")),
        Line("future", W("shows what will happen tomorrow", "показывает, что будет завтра")),
        Line("weather", W("changes the weather", "меняет погоду")),
        Line("hungry", W("is always hungry", "всё время хочет есть")),
        Line("sleepy", W("falls asleep at the worst moment", "засыпает в самый неподходящий момент")),
        Line("dance", W("makes everyone dance", "заставляет всех танцевать")),
        Line("far", W("lets you see very far", "позволяет видеть очень далеко")),
    )

    /** What goes wrong. [Line.fear]: 1 silly, 2 an adventure, 3 properly scary. */
    val troubles = listOf(
        Line("socks", W("All the socks in the kingdom have gone missing", "В королевстве пропали все носки"), "item:boot", 1),
        Line("dragonCold", W("The dragon caught a cold and sneezes sparks", "Дракон простудился и чихает искрами"), "beast:dragon", 1),
        Line("clocks", W("Every clock has stopped", "Все часы остановились"), "item:clock", 1),
        Line("colours", W("The colours ran away from the world", "Из мира сбежали все краски"), "thing:palette", 1),
        Line("hiccups", W("The king has hiccups that won't stop", "У короля икота, и она не проходит"), "hat:crown", 1),
        Line("snore", W("Somebody snores so loudly nobody can sleep", "Кто-то храпит так громко, что никто не может уснуть"), "glyph:zzz", 1),
        Line("upside", W("Everything turned upside down", "Всё вдруг перевернулось вверх ногами"), "glyph:upsideDown", 1),
        Line("echo", W("The echo stopped answering", "Эхо перестало отвечать"), "glyph:echo", 1),
        Line("birthday", W("Somebody forgot a birthday", "Кто-то забыл про день рождения"), "glyph:cake", 1),
        Line("shoes", W("The shoes went for a walk on their own", "Башмаки ушли гулять сами по себе"), "item:boot", 1),
        Line("rain", W("It has rained for a hundred days", "Дождь идёт уже сто дней"), "glyph:rain", 1),
        Line("sour", W("All the sweets have turned sour", "Все конфеты стали кислыми"), "glyph:cake", 1),
        Line("moon", W("The moon has gone from the sky", "С неба пропала луна"), "glyph:noMoon", 2),
        Line("stars", W("The stars are falling, one by one", "Звёзды падают одна за другой"), "glyph:shootingStar", 2),
        Line("lost", W("A little one is lost and can't find home", "Малыш потерялся и не может найти дорогу домой"), "glyph:footprints", 2),
        Line("storm", W("A great storm is coming", "Надвигается большая буря"), "glyph:storm", 2),
        Line("fog", W("A fog came and hid every path", "Пришёл туман и спрятал все тропинки"), "glyph:fog", 2),
        Line("frog", W("A friend was turned into a frog", "Друга превратили в лягушку"), "beast:frog", 2),
        Line("shrunk", W("The hero shrank to the size of a thimble", "Герой уменьшился до размеров напёрстка"), "glyph:shrink", 2),
        Line("asleep", W("The whole town fell asleep and won't wake", "Весь город уснул и не может проснуться"), "glyph:bed", 2),
        Line("quarrel", W("Two best friends have quarrelled", "Поссорились два лучших друга"), "glyph:brokenHeart", 2),
        Line("winter", W("Winter won't leave", "Зима никак не уходит"), "glyph:snowflake", 2),
        Line("voice", W("Someone stole the songbird's voice", "У певчей птички украли голос"), "glyph:notes", 2),
        Line("key", W("The key to the gate is lost", "Потерялся ключ от ворот"), "item:key", 2),
        Line("web", W("A giant web is blocking the road", "Дорогу перегородила огромная паутина"), "glyph:web", 2),
        Line("sun", W("The sun forgot to rise", "Солнце забыло взойти"), "glyph:sun", 2),
        Line("whirlwind", W("A whirlwind carried the little house away", "Вихрь унёс домик"), "glyph:tornado", 2),
        Line("crown", W("Somebody took the king's crown", "Кто-то утащил корону короля"), "hat:crown", 2),
        Line("volcano", W("The volcano has started to grumble", "Вулкан начал ворчать"), "place:volcano", 2),
        Line("shadow", W("A huge shadow has moved into the woods", "В лесу поселилась огромная тень"), "glyph:bigShadow", 3),
        Line("giant", W("A grumpy giant guards the bridge", "Сердитый великан сторожит мост"), "place:bridge", 3),
        Line("witch", W("A sly witch has put everyone under a spell", "Хитрая колдунья заколдовала всех вокруг"), "hat:wizard", 3),
        Line("wolf", W("The wolf prowls around the houses", "Волк бродит возле домиков"), "beast:wolf", 3),
        Line("ghost", W("Something wails in the attic every night", "Каждую ночь на чердаке кто-то воет"), "glyph:ghost", 3),
        Line("treasure", W("A dragon carried off the treasure", "Дракон утащил сокровище"), "item:chest", 3),
        Line("dark", W("Every light in the town went out", "Во всём городе погас свет"), "item:lantern", 3),
        Line("eye", W("Someone is watching from the bushes", "Кто-то следит из кустов"), "glyph:eye", 3),
        Line("fire", W("A fire broke out in the forest", "В лесу начался пожар"), "glyph:fire", 3),
    )

    /** What the helper can do. */
    val skills = listOf(
        Line("roads", W("knows every road", "знает все дороги")),
        Line("fly", W("can fly, or says so", "умеет летать — по крайней мере, так говорит")),
        Line("languages", W("speaks every language", "говорит на всех языках")),
        Line("awake", W("never sleeps", "никогда не спит")),
        Line("dark", W("sees in the dark", "видит в темноте")),
        Line("secrets", W("keeps secrets", "умеет хранить тайны")),
        Line("jokes", W("tells the funniest jokes", "рассказывает самые смешные шутки")),
        Line("lift", W("can lift anything at all", "поднимает что угодно")),
        Line("oldTales", W("remembers the oldest tales", "помнит старинные сказки")),
        Line("hide", W("can hide anywhere", "умеет прятаться где угодно")),
        Line("pies", W("bakes the best pies", "печёт лучшие пироги")),
        Line("treasure", W("knows where treasure is buried", "знает, где зарыт клад")),
        Line("whistle", W("whistles up the wind", "свистом зовёт ветер")),
        Line("stars", W("talks to the stars", "разговаривает со звёздами")),
        Line("quiet", W("walks without a sound", "ходит бесшумно")),
        Line("fix", W("can mend anything", "чинит что угодно")),
        Line("shortcut", W("knows a short cut", "знает короткую дорогу")),
        Line("swim", W("swims faster than fish", "плавает быстрее рыб")),
        Line("count", W("can count to a million", "умеет считать до миллиона")),
        Line("sing", W("sings so that everyone falls asleep", "поёт так, что все засыпают")),
        Line("questions", W("asks clever questions", "задаёт хитрые вопросы")),
        Line("doors", W("draws doors on walls", "рисует двери на стенах")),
        Line("trees", W("understands what the trees whisper", "понимает, о чём шепчут деревья")),
        Line("snack", W("always has a snack", "всегда носит с собой что-нибудь вкусное")),
        Line("friends", W("makes friends with anyone", "дружит с кем угодно")),
        Line("lullabies", W("knows every lullaby", "знает все колыбельные")),
        Line("older", W("is older than the forest", "старше самого леса")),
        Line("neverLost", W("never gets lost", "никогда не теряется")),
        Line("fast", W("runs faster than the wind", "бегает быстрее ветра")),
        Line("listen", W("can listen like nobody else", "умеет слушать, как никто другой")),
    )

    /** Something odd about the hero. */
    val quirks = listOf(
        Line("rhymes", W("speaks only in rhymes", "говорит только стихами"), "glyph:notes"),
        Line("butterflies", W("is afraid of butterflies", "боится бабочек"), "beast:bee"),
        Line("thimble", W("is the size of a thimble", "ростом с напёрсток"), "glyph:shrink"),
        Line("sneeze", W("sneezes when telling fibs", "чихает, когда говорит неправду"), "glyph:sneeze"),
        Line("dancing", W("can't stop dancing", "не может перестать танцевать"), "glyph:notes"),
        Line("buttons", W("collects buttons", "собирает пуговицы"), "item:button"),
        Line("hungry", W("is hungry all the time", "всё время хочет есть"), "glyph:cake"),
        Line("hiccups", W("has hiccups", "всё время икает"), "glyph:bubbles"),
        Line("backwards", W("walks backwards", "ходит задом наперёд"), "glyph:footprints"),
        Line("sleeps", W("falls asleep anywhere", "засыпает где попало"), "glyph:zzz"),
        Line("counts", W("counts everything", "всё на свете пересчитывает"), "glyph:question"),
        Line("laughs", W("laughs when scared", "смеётся, когда страшно"), "glyph:sun"),
        Line("sea", W("dreams of seeing the sea", "мечтает увидеть море"), "place:island"),
        Line("moon", W("talks to the moon", "разговаривает с луной"), "glyph:crescent"),
        Line("whistle", W("can't whistle", "не умеет свистеть"), "glyph:wind"),
        Line("twice", W("says everything twice", "всё повторяет дважды"), "glyph:echo"),
        Line("dark", W("is afraid of the dark", "боится темноты"), "item:candle"),
        Line("socks", W("wears odd socks", "носит разные носки"), "item:boot"),
        Line("letters", W("writes letters to the stars", "пишет письма звёздам"), "thing:letter"),
        Line("pebble", W("keeps a lucky pebble", "носит в кармане счастливый камешек"), "item:pebble"),
        Line("brave", W("is learning not to be afraid", "учится не бояться"), "glyph:heart"),
        Line("flowers", W("grows flowers in a hat", "выращивает цветы в шляпе"), "item:hat"),
        Line("no", W("can't say no", "не умеет говорить «нет»"), "glyph:question"),
        Line("rain", W("loves the rain", "обожает дождь"), "glyph:rain"),
        Line("hums", W("hums all the time", "всё время что-то напевает"), "glyph:notes"),
        Line("pillow", W("carries a pillow everywhere", "всюду носит с собой подушку"), "item:pillow"),
        Line("forgets", W("forgets things at once", "всё сразу забывает"), "glyph:question"),
        Line("why", W("keeps asking why", "всё время спрашивает «почему?»"), "glyph:question"),
        Line("secret", W("is keeping one secret", "хранит один секрет"), "glyph:heart"),
        Line("birds", W("understands birds", "понимает птиц"), "beast:crow"),
        Line("nightlight", W("sleeps with a lantern lit", "спит с зажжённым фонариком"), "item:lantern"),
        Line("upside", W("sleeps upside down", "спит вверх ногами"), "beast:bat"),
    )

    /** What the lantern shows mid-tale, when the phone is turned back over. */
    val twists = listOf(
        Line("buttons", W("Suddenly it started snowing buttons", "Вдруг пошёл снег из пуговиц"), "item:button"),
        Line("door", W("A door appeared right in a tree", "Прямо в дереве появилась дверь"), "glyph:door"),
        Line("backwards", W("All the animals started talking backwards", "Все звери заговорили задом наперёд"), "glyph:echo"),
        Line("letter", W("The hero found a letter with no name on it", "Герой нашёл письмо без подписи"), "thing:letter"),
        Line("lonely", W("It turned out the villain was just lonely", "Оказалось, злодею просто одиноко"), "glyph:heart"),
        Line("whale", W("A huge fish swallowed everyone — and it's cosy inside", "Огромная рыба проглотила всех — а внутри уютно"), "beast:whale"),
        Line("map", W("The map was upside down all along", "Оказалось, карта была вверх ногами"), "item:map"),
        Line("moon", W("The moon came down to see what was happening", "Луна спустилась посмотреть, что происходит"), "glyph:crescent"),
        Line("star", W("A star fell right at their feet", "Прямо под ноги упала звезда"), "glyph:shootingStar"),
        Line("tiny", W("Everybody suddenly became tiny", "Все вдруг стали крошечными"), "glyph:shrink"),
        Line("pies", W("The wind brought a smell of pies", "Ветер принёс запах пирогов"), "glyph:wind"),
        Line("lemonade", W("It started raining lemonade", "Пошёл дождь из лимонада"), "glyph:rain"),
        Line("egg", W("An egg hatched — but who came out?", "Из яйца кто-то вылупился — кто?"), "item:egg"),
        Line("dream", W("The hero fell asleep and dreamed the answer", "Герой уснул и увидел подсказку во сне"), "glyph:zzz"),
        Line("rainbow", W("A rainbow turned into a bridge", "Радуга превратилась в мост"), "place:rainbow"),
        Line("footprints", W("Someone left footprints that glow", "Кто-то оставил светящиеся следы"), "glyph:footprints"),
        Line("shadows", W("Everybody swapped shadows", "Все поменялись тенями"), "glyph:bigShadow"),
        Line("pocket", W("A tiny voice said \"Help!\" from a pocket", "Из кармана раздался тоненький голосок: «Помогите!»"), "beast:mouse"),
        Line("broke", W("The magic thing broke in two", "Волшебная вещь раскололась надвое"), "glyph:sparkles"),
        Line("giant", W("A sleeping giant lay across the road", "Дорогу загородил спящий великан"), "glyph:bigShadow"),
        Line("candyfloss", W("The clouds turned into candyfloss", "Облака превратились в сладкую вату"), "place:cloudCity"),
        Line("tunnel", W("There was a secret tunnel under the rug", "Под ковриком нашёлся подземный ход"), "item:carpet"),
        Line("hat", W("The hero's hat began to talk", "Шляпа героя заговорила"), "item:hat"),
        Line("time", W("Time stopped for everyone except the hero", "Время остановилось для всех, кроме героя"), "item:hourglass"),
        Line("friends", W("The baddie asked to be friends", "Злодей попросился в друзья"), "glyph:heart"),
        Line("balloon", W("A balloon carried the hero off", "Героя унёс воздушный шарик"), "item:balloon"),
        Line("forest", W("The forest began to sing", "Лес вдруг запел"), "glyph:notes"),
        Line("parcel", W("A parcel arrived with no address", "Пришла посылка без адреса"), "glyph:gift"),
        Line("chocolate", W("Everything around turned to chocolate", "Всё вокруг стало шоколадным"), "glyph:cake"),
        Line("reflection", W("The hero's reflection winked", "Отражение героя подмигнуло"), "item:mirror"),
        Line("day", W("For one minute the night turned into day", "На одну минуту ночь стала днём"), "glyph:sun"),
        Line("lights", W("A fog came, and there were little lights in it", "Пришёл туман, а в нём — огоньки"), "glyph:fog"),
        Line("river", W("The river started to flow backwards", "Река потекла вспять"), "place:bridge"),
        Line("wasDream", W("It was all a dream… or was it?", "Оказалось, это был сон… или нет?"), "glyph:bed"),
        Line("grow", W("The smallest one suddenly grew huge", "Самый маленький вдруг стал огромным"), "glyph:grow"),
        Line("invisible", W("Somebody became invisible", "Кто-то стал невидимым"), "glyph:invisible"),
    )

    /** How a tale ends: slowly, quietly, towards sleep. */
    val endings = listOf(
        Line("blanket", W("And they all went home and fell asleep under one big blanket.", "И все вернулись домой и уснули под одним большим одеялом."), "glyph:bed"),
        Line("moon", W("The moon tucked everyone in and switched the stars off one by one.", "Луна укрыла всех и выключила звёзды — одну за другой."), "glyph:crescent"),
        Line("milk", W("They drank warm milk with honey and yawned.", "Они выпили тёплого молока с мёдом и сладко зевнули."), "item:teapot"),
        Line("lantern", W("The lantern went out by itself. That meant it was time.", "Фонарь погас сам. Значит, пора спать."), "item:lantern"),
        Line("morning", W("And in the morning everything was all right.", "А наутро всё было хорошо."), "glyph:sun"),
        Line("pillow", W("The hero hid the magic thing under the pillow — just in case.", "Герой спрятал волшебную вещь под подушку — на всякий случай."), "item:pillow"),
        Line("tomorrow", W("They agreed to meet tomorrow, in the next tale.", "Они договорились встретиться завтра — в следующей сказке."), "glyph:door"),
        Line("wind", W("The wind sang a lullaby over the rooftops.", "Ветер спел колыбельную над крышами."), "place:rooftops"),
        Line("count", W("They all counted stars and fell asleep at seventeen.", "Все считали звёзды и уснули на семнадцатой."), "glyph:shootingStar"),
        Line("kettle", W("It was warm at home, and the kettle sang its little song.", "Дома было тепло, и чайник пел свою песенку."), "place:grannyHouse"),
        Line("book", W("The hero wrote it all in a book, so as not to forget.", "Герой записал всё в книгу, чтобы не забыть."), "item:book"),
        Line("snow", W("Snow covered all the paths, soft as a blanket.", "Снег укрыл все дорожки — мягко, как одеяло."), "glyph:snowflake"),
        Line("windows", W("The friends waved goodbye, and the windows went dark one by one.", "Друзья помахали друг другу, и окна погасли одно за другим."), "place:village"),
        Line("train", W("The train rocked them all to sleep until morning.", "Поезд укачал всех до самого утра."), "place:nightTrain"),
        Line("still", W("And if they haven't woken up, they're sleeping still.", "И если не проснулись — спят до сих пор."), "glyph:zzz"),
        Line("carried", W("The smallest one was carried home, fast asleep.", "Самого маленького отнесли домой на руках, сонного."), "glyph:house"),
        Line("owl", W("The owl said \"Good night\" to everyone.", "Сова сказала всем: «Спокойной ночи»."), "beast:owl"),
        Line("forever", W("They've been friends ever since, and every night they say good night to each other.", "С тех пор они дружат и каждый вечер желают друг другу спокойной ночи."), "glyph:heart"),
    )

    /** Propp's tale, shortened to the steps a parent can lean on. */
    val steps = listOf(
        Step("home", W("Home", "Дом"),
            W("Show where the hero lives and what they love. Slowly: there's time.", "Покажите, где живёт герой и что он любит. Не спешите: время есть."),
            W("Where does our hero live?", "Где живёт наш герой?")),
        Step("ban", W("A warning", "Запрет"),
            W("Someone warns the hero: don't go there, don't touch that.", "Кто-то предупреждает героя: не ходи туда, не трогай это."),
            W("What was the hero told not to do?", "Что герою запретили?")),
        Step("broken", W("The rule is broken", "Нарушение"),
            W("Of course the hero does it anyway. Curiosity is stronger.", "Конечно, герой всё равно это делает. Любопытство сильнее."),
            W("And what did the hero do?", "А что сделал герой?")),
        Step("trouble", W("Trouble", "Беда"),
            W("Now the trouble from the slide happens.", "Тут и случается беда со стекла."),
            W("What went wrong?", "Что случилось?")),
        Step("road", W("Setting off", "В путь"),
            W("The hero goes to put it right. Packs a bag, says goodbye.", "Герой отправляется всё исправлять. Собирает узелок, прощается."),
            W("What does the hero take along?", "Что герой возьмёт с собой?")),
        Step("helper", W("The helper", "Встреча"),
            W("On the way the helper turns up, and asks something or tests the hero.", "По дороге появляется помощник — и о чём-то просит или испытывает героя."),
            W("What did the helper ask for?", "О чём попросил помощник?")),
        Step("gift", W("The gift", "Дар"),
            W("Because the hero was kind, they get the magic thing.", "За доброту герой получает волшебную вещь."),
            W("What can the magic thing do?", "Что умеет волшебная вещь?")),
        Step("trial", W("The hardest part", "Испытание"),
            W("The most difficult moment. Let it be scary only a little.", "Самый трудный момент. Пусть будет страшно, но совсем чуть-чуть."),
            W("What was the hardest part?", "Что было самым трудным?")),
        Step("win", W("Putting it right", "Победа"),
            W("The hero wins with wits and kindness, not just strength.", "Герой побеждает хитростью и добротой, а не только силой."),
            W("How did the hero manage it?", "Как герой справился?")),
        Step("back", W("Home again", "Возвращение"),
            W("The way back. Shorter and lighter than the way there.", "Дорога назад. Короче и легче, чем туда."),
            W("Who is waiting at home?", "Кто ждёт героя дома?")),
        Step("feast", W("All together", "Праздник"),
            W("Everyone together, everything well. Tea, pies, friends.", "Все вместе, всё хорошо. Чай, пироги, друзья."),
            W("How did they celebrate?", "Как все отпраздновали?")),
        Step("quiet", W("The quiet ending", "Тихий финал"),
            W("Speak more slowly and more softly. Draw the ending slide.", "Говорите медленнее и тише. Вытяните стекло финала."),
            W("Shall we say good night to the hero?", "Пожелаем герою спокойной ночи?")),
    )

    /** For the morning after: questions that bring the tale back. */
    val morning = listOf(
        W("Who was in yesterday's tale?", "Кто был во вчерашней сказке?"),
        W("Where did it all happen?", "Где всё случилось?"),
        W("What could the magic thing do?", "Что умела волшебная вещь?"),
        W("Who helped the hero?", "Кто помог герою?"),
        W("What was the funniest bit?", "Что было самым смешным?"),
        W("How did it end?", "Чем всё закончилось?"),
        W("What could happen next?", "А что могло бы случиться дальше?"),
    )

    fun creature(key: String) = creatures.firstOrNull { it.key == key }
    fun role(key: String) = roles.firstOrNull { it.key == key }
    fun place(key: String) = places.firstOrNull { it.key == key }
    fun thing(key: String) = things.firstOrNull { it.key == key }
    fun power(key: String) = powers.firstOrNull { it.key == key }
    fun trouble(key: String) = troubles.firstOrNull { it.key == key }
    fun skill(key: String) = skills.firstOrNull { it.key == key }
    fun quirk(key: String) = quirks.firstOrNull { it.key == key }
    fun twist(key: String) = twists.firstOrNull { it.key == key }
    fun ending(key: String) = endings.firstOrNull { it.key == key }

    /** Every rare glass, as the box keeps them: "creature:dragon", "role:jester". */
    val rares: List<String> by lazy {
        creatures.filter { it.rare }.map { "creature:${it.key}" } + roles.filter { it.rare }.map { "role:${it.key}" } +
            places.filter { it.rare }.map { "place:${it.key}" } + things.filter { it.rare }.map { "thing:${it.key}" }
    }
}
