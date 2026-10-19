package app.lantern.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.lantern.domain.Age
import app.lantern.domain.Hand
import app.lantern.domain.Slide
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONObject

private val Context.dataStore by preferencesDataStore("settings")

/** What plays after the tale, made on the phone as it plays. */
enum class Lullaby { RAIN, SEA, CRICKETS, FIRE, WIND, NONE }

/**
 * Tonight's evening, kept on disk so a phone that kills apps in the dark (HyperOS does) can't
 * lose the glass halfway through a tale. Empty [slides]: nothing dealt yet.
 */
data class Evening(
    val slides: List<Slide> = emptyList(),
    /** Slots already swapped: each glass may be changed once. */
    val swapped: Set<Int> = emptySet(),
    /** The shared-evening code it was dealt from, or "". */
    val code: String = "",
    /** The tale this one carries on from, or 0. */
    val sequelOf: Long = 0,
    /** When the telling began, or 0 while the glass is still being looked at. */
    val startedAt: Long = 0,
    val twists: List<Slide> = emptyList(),
    /** Where the parent is in Propp's steps. */
    val step: Int = 0,
    val ending: Slide? = null,
    /** The recording under way, a file name, or "". */
    val recording: String = "",
    val seed: Long = 0,
) {
    val dealt: Boolean get() = slides.isNotEmpty()
    val telling: Boolean get() = startedAt > 0

    fun encode(): String = JSONObject()
        .put("slides", Slide.join(slides)).put("swapped", swapped.joinToString(",")).put("code", code).put("sequelOf", sequelOf)
        .put("startedAt", startedAt).put("twists", Slide.join(twists)).put("step", step).put("ending", ending?.encode() ?: "")
        .put("recording", recording).put("seed", seed)
        .toString()

    companion object {
        fun decode(s: String?): Evening {
            if (s.isNullOrEmpty()) return Evening()
            val o = runCatching { JSONObject(s) }.getOrNull() ?: return Evening()
            return Evening(
                slides = Slide.list(o.optString("slides")),
                swapped = o.optString("swapped").split(',').mapNotNull { it.toIntOrNull() }.toSet(),
                code = o.optString("code"),
                sequelOf = o.optLong("sequelOf"),
                startedAt = o.optLong("startedAt"),
                twists = Slide.list(o.optString("twists")),
                step = o.optInt("step"),
                ending = Slide.decode(o.optString("ending")),
                recording = o.optString("recording"),
                seed = o.optLong("seed"),
            )
        }
    }
}

data class Settings(
    val onboarded: Boolean = false,
    val hand: Hand = Hand(),
    /** The candle: minutes until it's time for the quiet ending, or 0 for no candle. */
    val candle: Int = 15,
    /** Telling in turns round the table. */
    val relay: Boolean = false,
    val tellers: List<String> = emptyList(),
    /** Seconds per turn. */
    val turn: Int = 60,
    /** Face down, the screen goes dark; face up again, a twist. */
    val faceDown: Boolean = true,
    /** Turn the screen right down while the tale is told. */
    val dim: Boolean = true,
    val record: Boolean = false,
    val lullaby: Lullaby = Lullaby.RAIN,
    val lullabyMin: Int = 15,
    /** Old friends from earlier tales may come back. */
    val friends: Boolean = true,
    val haptics: Boolean = true,
    /** Rare glass already taken out of a parcel: "creature:dragon". */
    val opened: Set<String> = emptySet(),
    /** Hints already read. */
    val hints: Set<String> = emptySet(),
    val evening: Evening = Evening(),
)

class SettingsStore(context: Context) {
    private val store = context.applicationContext.dataStore

    val settings: Flow<Settings> = store.data.map { it.read() }

    private fun Preferences.read() = Settings(
        onboarded = this[ONBOARDED] ?: false,
        hand = Hand(
            age = runCatching { Age.valueOf(this[AGE] ?: "") }.getOrDefault(Age.BIG),
            item = this[ITEM] ?: true,
            trouble = this[TROUBLE] ?: true,
            helper = this[HELPER] ?: true,
            quirk = this[QUIRK] ?: false,
            fear = this[FEAR] ?: 2,
        ),
        candle = this[CANDLE] ?: 15,
        relay = this[RELAY] ?: false,
        tellers = (this[TELLERS] ?: "").split('\n').map { it.trim() }.filter { it.isNotEmpty() },
        turn = this[TURN] ?: 60,
        faceDown = this[FACE_DOWN] ?: true,
        dim = this[DIM] ?: true,
        record = this[RECORD] ?: false,
        lullaby = runCatching { Lullaby.valueOf(this[LULLABY] ?: "") }.getOrDefault(Lullaby.RAIN),
        lullabyMin = this[LULLABY_MIN] ?: 15,
        friends = this[FRIENDS] ?: true,
        haptics = this[HAPTICS] ?: true,
        opened = (this[OPENED] ?: "").split(',').filter { it.isNotBlank() }.toSet(),
        hints = (this[HINTS] ?: "").split(',').filter { it.isNotBlank() }.toSet(),
        evening = Evening.decode(this[EVENING]),
    )

    suspend fun now(): Settings = settings.first()

    suspend fun update(change: (Settings) -> Settings) {
        store.edit {
            val s = change(it.read())
            it[ONBOARDED] = s.onboarded
            it[AGE] = s.hand.age.name
            it[ITEM] = s.hand.item
            it[TROUBLE] = s.hand.trouble
            it[HELPER] = s.hand.helper
            it[QUIRK] = s.hand.quirk
            it[FEAR] = s.hand.fear
            it[CANDLE] = s.candle
            it[RELAY] = s.relay
            it[TELLERS] = s.tellers.joinToString("\n")
            it[TURN] = s.turn
            it[FACE_DOWN] = s.faceDown
            it[DIM] = s.dim
            it[RECORD] = s.record
            it[LULLABY] = s.lullaby.name
            it[LULLABY_MIN] = s.lullabyMin
            it[FRIENDS] = s.friends
            it[HAPTICS] = s.haptics
            it[OPENED] = s.opened.joinToString(",")
            it[HINTS] = s.hints.joinToString(",")
            it[EVENING] = if (s.evening.dealt) s.evening.encode() else ""
        }
    }

    suspend fun evening(change: (Evening) -> Evening) = update { it.copy(evening = change(it.evening)) }

    private companion object {
        val ONBOARDED = booleanPreferencesKey("onboarded")
        val AGE = stringPreferencesKey("age")
        val ITEM = booleanPreferencesKey("item")
        val TROUBLE = booleanPreferencesKey("trouble")
        val HELPER = booleanPreferencesKey("helper")
        val QUIRK = booleanPreferencesKey("quirk")
        val FEAR = intPreferencesKey("fear")
        val CANDLE = intPreferencesKey("candle")
        val RELAY = booleanPreferencesKey("relay")
        val TELLERS = stringPreferencesKey("tellers")
        val TURN = intPreferencesKey("turn")
        val FACE_DOWN = booleanPreferencesKey("face_down")
        val DIM = booleanPreferencesKey("dim")
        val RECORD = booleanPreferencesKey("record")
        val LULLABY = stringPreferencesKey("lullaby")
        val LULLABY_MIN = intPreferencesKey("lullaby_min")
        val FRIENDS = booleanPreferencesKey("friends")
        val HAPTICS = booleanPreferencesKey("haptics")
        val OPENED = stringPreferencesKey("opened")
        val HINTS = stringPreferencesKey("hints")
        val EVENING = stringPreferencesKey("evening")
    }
}
