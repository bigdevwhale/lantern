package app.lantern.ui.shelf

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material.icons.rounded.WbIncandescent
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.lantern.R
import app.lantern.book.Book
import app.lantern.container
import app.lantern.data.Evenings
import app.lantern.domain.Kind
import app.lantern.domain.Lantern
import app.lantern.sound.Player
import app.lantern.ui.Go
import app.lantern.ui.Home
import app.lantern.ui.T
import app.lantern.ui.components.ConfirmDialog
import app.lantern.ui.components.PrimaryButton
import app.lantern.ui.components.SectionTitle
import app.lantern.ui.components.SlideRow
import app.lantern.ui.components.SoftButton
import app.lantern.ui.components.TopBar
import app.lantern.ui.components.UCard
import app.lantern.ui.components.UTextField
import app.lantern.ui.components.rememberNow
import app.lantern.ui.ctx
import app.lantern.ui.theme.U
import app.lantern.ui.theme.UType
import app.lantern.ui.theme.glass
import kotlinx.coroutines.launch

@Composable
fun TaleScreen(id: Long, home: Home, go: Go) {
    val c = ctx()
    val container = c.container
    val t = home.tale(id)
    LaunchedEffect(t == null, home.tales.isNotEmpty()) { if (t == null && home.tales.isNotEmpty()) go.back() }
    t ?: return
    val scope = rememberCoroutineScope()
    var title by rememberSaveable(id) { mutableStateOf(t.title) }
    var note by rememberSaveable(id) { mutableStateOf(t.note) }
    var deleting by remember { mutableStateOf(false) }
    val player = remember { Player() }
    var playing by remember { mutableStateOf(false) }
    DisposableEffect(Unit) { onDispose { player.stop() } }
    val tick = rememberNow(if (playing) 500 else 60_000)

    // Edits are kept as you go.
    LaunchedEffect(title, note) {
        if (title != t.title || note != t.note) {
            kotlinx.coroutines.delay(600)
            container.db.dao().update(t.copy(title = title.ifBlank { t.title }, note = note))
        }
    }

    Column(Modifier.fillMaxSize().background(U.c.bg).statusBarsPadding().imePadding()) {
        TopBar(T.date(c, t.endedAt), go::back) {
            IconButton(onClick = { container.write { db.dao().update(t.copy(favourite = !t.favourite)) } }) {
                Icon(if (t.favourite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder, stringResource(R.string.favourite), tint = if (t.favourite) Kind.QUIRK.glass() else U.c.muted)
            }
            IconButton(onClick = { scope.launch { if (!Book.share(c, listOf(t), home)) Toast.makeText(c, R.string.book_failed, Toast.LENGTH_SHORT).show() } }) {
                Icon(Icons.AutoMirrored.Rounded.MenuBook, stringResource(R.string.book), tint = U.c.muted)
            }
            IconButton(onClick = { deleting = true }) { Icon(Icons.Rounded.DeleteOutline, stringResource(R.string.delete), tint = U.c.muted) }
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp).navigationBarsPadding()) {
            UTextField(title, { title = it })
            Spacer(Modifier.height(6.dp))
            val meta = buildList {
                add(T.weekday(c, t.endedAt))
                add(pluralStringResource(R.plurals.minutes, t.minutes.coerceAtLeast(1), t.minutes.coerceAtLeast(1)))
                if (t.tellers.isNotEmpty()) add(t.tellers.replace(",", ", "))
                if (t.code.isNotEmpty()) add(stringResource(R.string.shared_code, Lantern.Code.pretty(t.code)))
            }
            Text(meta.joinToString(" · "), style = UType.Small, modifier = Modifier.padding(start = 4.dp))

            if (t.recording.isNotEmpty()) {
                val file = container.recorder.file(t.recording)
                if (file.exists()) {
                    Spacer(Modifier.height(12.dp))
                    UCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(stringResource(R.string.recording_title), style = UType.Strong)
                                tick.let { }
                                Text(if (playing) T.clock(player.position.toLong()) + " / " + T.clock(player.duration.toLong()) else stringResource(R.string.recording_sub), style = UType.Muted)
                            }
                            SoftButton(
                                stringResource(if (playing) R.string.stop else R.string.listen),
                                { if (playing) { player.stop(); playing = false } else playing = player.play(file) { playing = false } },
                                icon = if (playing) Icons.Rounded.Stop else Icons.Rounded.PlayArrow,
                            )
                        }
                    }
                }
            }

            SectionTitle(stringResource(R.string.the_glass))
            t.glass.forEachIndexed { i, s -> SlideRow(s, home, size = 88.dp, seed = i); Spacer(Modifier.height(8.dp)) }
            if (t.twistList.isNotEmpty()) {
                SectionTitle(stringResource(R.string.the_twists))
                t.twistList.forEachIndexed { i, s -> SlideRow(s, home, size = 76.dp, seed = i + 10); Spacer(Modifier.height(8.dp)) }
            }
            t.end?.let { e ->
                SectionTitle(stringResource(R.string.kind_ending))
                SlideRow(e, home, size = 76.dp, seed = 20)
            }

            SectionTitle(stringResource(R.string.note))
            UTextField(note, { note = it }, placeholder = stringResource(R.string.note_hint), minLines = 3)

            Spacer(Modifier.height(20.dp))
            PrimaryButton(stringResource(R.string.sequel), {
                scope.launch {
                    val keep = listOfNotNull(t.of(Kind.HERO)).associateBy { it.kind }
                    Evenings.deal(container, keep = keep, sequelOf = t.id)
                    go.lantern()
                }
            }, Modifier.fillMaxWidth(), icon = Icons.Rounded.WbIncandescent)
            Text(stringResource(R.string.sequel_sub), style = UType.Small, modifier = Modifier.fillMaxWidth().padding(top = 6.dp, start = 4.dp))
            Spacer(Modifier.width(1.dp).height(28.dp))
        }
    }
    if (deleting) ConfirmDialog(stringResource(R.string.delete_tale), stringResource(R.string.delete_tale_text), stringResource(R.string.delete), {
        container.write {
            if (t.recording.isNotEmpty()) recorder.file(t.recording).delete()
            db.dao().delete(t)
        }
        go.back()
    }, { deleting = false }, danger = true)
}
