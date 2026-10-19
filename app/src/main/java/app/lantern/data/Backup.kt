package app.lantern.data

import android.content.Context
import android.net.Uri
import org.json.JSONArray
import org.json.JSONObject

/**
 * Every tale, painted slide and opened parcel in one JSON file. Recordings stay on the phone:
 * they're too big for a backup file. Nothing leaves the phone unless you export it.
 */
object Backup {
    private const val VERSION = 1

    fun encode(all: Everything, opened: Set<String>): String = JSONObject()
        .put("app", "lantern")
        .put("version", VERSION)
        .put("opened", JSONArray(opened.toList()))
        .put("tales", JSONArray(all.tales.map {
            JSONObject().put("id", it.id).put("title", it.title).put("slides", it.slides).put("twists", it.twists).put("ending", it.ending)
                .put("startedAt", it.startedAt).put("endedAt", it.endedAt).put("note", it.note).put("tellers", it.tellers)
                .put("code", it.code).put("retold", it.retold).put("favourite", it.favourite)
        }))
        .put("drawings", JSONArray(all.drawings.map {
            JSONObject().put("id", it.id).put("name", it.name).put("kind", it.kind).put("strokes", it.strokes).put("createdAt", it.createdAt)
        }))
        .toString(2)

    private fun JSONObject.list(key: String) = optJSONArray(key)?.let { a -> (0 until a.length()).map { a.getJSONObject(it) } }.orEmpty()

    /** Throws [IllegalArgumentException] when the text is not a Lantern backup. */
    fun decode(text: String): Pair<Everything, Set<String>> {
        val root = runCatching { JSONObject(text) }.getOrNull()
        require(root != null && root.optString("app") == "lantern") { "Not a Lantern backup" }
        val opened = root.optJSONArray("opened")?.let { a -> (0 until a.length()).map { a.getString(it) } }.orEmpty().toSet()
        return Everything(
            tales = root.list("tales").map {
                Tale(
                    id = it.getLong("id"), title = it.optString("title"), slides = it.optString("slides"), twists = it.optString("twists"),
                    ending = it.optString("ending"), startedAt = it.optLong("startedAt"), endedAt = it.optLong("endedAt"), note = it.optString("note"),
                    tellers = it.optString("tellers"), code = it.optString("code"), retold = it.optBoolean("retold"), favourite = it.optBoolean("favourite"),
                )
            },
            drawings = root.list("drawings").map {
                Drawing(id = it.getLong("id"), name = it.optString("name"), kind = it.optString("kind", "HERO"), strokes = it.optString("strokes"), createdAt = it.optLong("createdAt"))
            },
        ) to opened
    }

    fun write(context: Context, uri: Uri, all: Everything, opened: Set<String>) {
        context.contentResolver.openOutputStream(uri)?.use { it.write(encode(all, opened).toByteArray()) } ?: error("Can't write there")
    }

    fun read(context: Context, uri: Uri): Pair<Everything, Set<String>> {
        val text = context.contentResolver.openInputStream(uri)?.use { it.readBytes().decodeToString() } ?: error("Can't read that")
        return decode(text)
    }
}
