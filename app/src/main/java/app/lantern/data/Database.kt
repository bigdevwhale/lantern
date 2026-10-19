package app.lantern.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Update
import app.lantern.domain.Kind
import app.lantern.domain.Slide
import kotlinx.coroutines.flow.Flow

/**
 * A tale told at bedtime: the glass it was told from, the twists that came, how it ended, and
 * whatever the family wrote down afterwards. [recording] is a file name in the app's own folder.
 */
@Entity(tableName = "tales", indices = [Index("endedAt")])
data class Tale(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    /** [Slide]s, see [Slide.join]. */
    val slides: String,
    val twists: String = "",
    val ending: String = "",
    val startedAt: Long,
    val endedAt: Long,
    val note: String = "",
    /** Who told it, comma separated, when it went round the table. */
    val tellers: String = "",
    val recording: String = "",
    /** The shared-evening code, when it was told with someone far away. */
    val code: String = "",
    /** The morning after, the child told it back. */
    val retold: Boolean = false,
    val favourite: Boolean = false,
) {
    val glass: List<Slide> get() = Slide.list(slides)
    val twistList: List<Slide> get() = Slide.list(twists)
    val end: Slide? get() = Slide.decode(ending)
    val all: List<Slide> get() = glass + twistList + listOfNotNull(end)
    fun of(kind: Kind) = glass.firstOrNull { it.kind == kind }
    val minutes: Int get() = ((endedAt - startedAt) / 60_000).toInt()
}

/** A slide the child painted with a finger. [strokes]: see Strokes. [kind] is a [Kind] name. */
@Entity(tableName = "drawings")
data class Drawing(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val kind: String = Kind.HERO.name,
    val strokes: String,
    val createdAt: Long = System.currentTimeMillis(),
) {
    val kindOf: Kind get() = runCatching { Kind.valueOf(kind) }.getOrDefault(Kind.HERO)
}

@Dao
abstract class LanternDao {
    @Query("SELECT * FROM tales ORDER BY endedAt DESC") abstract fun observeTales(): Flow<List<Tale>>
    @Query("SELECT * FROM drawings ORDER BY createdAt ASC") abstract fun observeDrawings(): Flow<List<Drawing>>

    @Query("SELECT * FROM tales ORDER BY endedAt DESC") abstract suspend fun tales(): List<Tale>
    @Query("SELECT * FROM drawings ORDER BY createdAt ASC") abstract suspend fun drawings(): List<Drawing>
    @Query("SELECT * FROM tales WHERE id = :id") abstract suspend fun tale(id: Long): Tale?
    @Query("SELECT * FROM drawings WHERE id = :id") abstract suspend fun drawing(id: Long): Drawing?
    @Query("SELECT * FROM tales ORDER BY endedAt DESC LIMIT 1") abstract suspend fun lastTale(): Tale?

    @Insert abstract suspend fun insert(t: Tale): Long
    @Update abstract suspend fun update(t: Tale)
    @Delete abstract suspend fun delete(t: Tale)

    @Insert abstract suspend fun insert(d: Drawing): Long
    @Update abstract suspend fun update(d: Drawing)
    @Delete abstract suspend fun delete(d: Drawing)

    @Query("DELETE FROM tales") protected abstract suspend fun clearTales()
    @Query("DELETE FROM drawings") protected abstract suspend fun clearDrawings()
    @Insert protected abstract suspend fun insertTales(l: List<Tale>)
    @Insert protected abstract suspend fun insertDrawings(l: List<Drawing>)

    @Transaction
    open suspend fun replaceAll(all: Everything) {
        clearTales(); clearDrawings()
        insertTales(all.tales); insertDrawings(all.drawings)
    }
}

/** The whole shelf, for backups. */
class Everything(val tales: List<Tale>, val drawings: List<Drawing>)

@Database(entities = [Tale::class, Drawing::class], version = 1, exportSchema = true)
abstract class LanternDatabase : RoomDatabase() {
    abstract fun dao(): LanternDao

    companion object {
        fun create(context: Context): LanternDatabase =
            Room.databaseBuilder(context.applicationContext, LanternDatabase::class.java, "lantern.db").build()
    }
}
