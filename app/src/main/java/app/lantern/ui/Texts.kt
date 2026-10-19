package app.lantern.ui

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import app.lantern.R
import app.lantern.data.Lullaby
import app.lantern.domain.Age
import app.lantern.domain.Kind
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Words and dates in the app's language. Plain functions over a [Context] so the widget can use them too. */
object T {
    private fun locale(c: Context): Locale = c.resources.configuration.locales[0]

    fun plural(c: Context, id: Int, n: Int): String = c.resources.getQuantityString(id, n, n)

    fun local(ms: Long, zone: ZoneId = ZoneId.systemDefault()) = Instant.ofEpochMilli(ms).atZone(zone)

    /** "17 Oct 2026". */
    fun date(c: Context, d: LocalDate): String = d.format(DateTimeFormatter.ofPattern("d MMMM yyyy", locale(c)))
    fun date(c: Context, ms: Long): String = date(c, local(ms).toLocalDate())

    /** "Saturday, 17 October". */
    fun weekday(c: Context, ms: Long): String =
        local(ms).toLocalDate().format(DateTimeFormatter.ofPattern("EEEE, d MMMM", locale(c))).replaceFirstChar { it.titlecase(locale(c)) }

    /** "October 2026", for the shelf's sections. */
    fun month(c: Context, d: LocalDate): String =
        d.format(DateTimeFormatter.ofPattern("LLLL yyyy", locale(c))).replaceFirstChar { it.titlecase(locale(c)) }

    fun kind(c: Context, k: Kind): String = c.getString(
        when (k) {
            Kind.HERO -> R.string.kind_hero
            Kind.PLACE -> R.string.kind_place
            Kind.ITEM -> R.string.kind_item
            Kind.TROUBLE -> R.string.kind_trouble
            Kind.HELPER -> R.string.kind_helper
            Kind.QUIRK -> R.string.kind_quirk
            Kind.TWIST -> R.string.kind_twist
            Kind.ENDING -> R.string.kind_ending
        }
    )

    fun age(c: Context, a: Age): String = c.getString(if (a == Age.LITTLE) R.string.age_little else R.string.age_big)
    fun ageSub(c: Context, a: Age): String = c.getString(if (a == Age.LITTLE) R.string.age_little_sub else R.string.age_big_sub)

    fun fear(c: Context, n: Int): String = c.getString(
        when (n) {
            1 -> R.string.fear_1
            2 -> R.string.fear_2
            else -> R.string.fear_3
        }
    )

    fun lullaby(c: Context, l: Lullaby): String = c.getString(
        when (l) {
            Lullaby.RAIN -> R.string.lullaby_rain
            Lullaby.SEA -> R.string.lullaby_sea
            Lullaby.CRICKETS -> R.string.lullaby_crickets
            Lullaby.FIRE -> R.string.lullaby_fire
            Lullaby.WIND -> R.string.lullaby_wind
            Lullaby.NONE -> R.string.lullaby_none
        }
    )

    /** "4:05" on the candle and the hourglass. */
    fun clock(ms: Long): String {
        val s = (ms / 1000).coerceAtLeast(0)
        return "%d:%02d".format(s / 60, s % 60)
    }
}

/** In-app language switching (persisted by AppCompat; shows up in Android 13+ per-app language settings). */
object Language {
    const val SYSTEM = ""
    const val EN = "en"
    const val RU = "ru"

    fun current(): String = AppCompatDelegate.getApplicationLocales().toLanguageTags().substringBefore('-').substringBefore(',')

    fun set(tag: String) {
        AppCompatDelegate.setApplicationLocales(if (tag == SYSTEM) LocaleListCompat.getEmptyLocaleList() else LocaleListCompat.forLanguageTags(tag))
    }
}

@Composable
fun ctx(): Context = LocalContext.current
