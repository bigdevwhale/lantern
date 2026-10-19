package app.lantern.ui.shelf

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.lantern.R
import app.lantern.book.Book
import app.lantern.data.Tale
import app.lantern.domain.Kind
import app.lantern.domain.streak
import app.lantern.ui.Go
import app.lantern.ui.Home
import app.lantern.ui.T
import app.lantern.ui.art.Art
import app.lantern.ui.components.Glass
import app.lantern.ui.components.GlassStrip
import app.lantern.ui.components.UCard
import app.lantern.ui.ctx
import app.lantern.ui.theme.U
import app.lantern.ui.theme.UType
import app.lantern.ui.theme.glass
import kotlinx.coroutines.launch

@Composable
fun ShelfTab(home: Home, go: Go) {
    val c = ctx()
    val scope = rememberCoroutineScope()
    var making by remember { mutableStateOf(false) }
    val byMonth = remember(home.tales) { home.tales.groupBy { T.local(it.endedAt).toLocalDate().withDayOfMonth(1) } }

    Column(Modifier.fillMaxSize().background(U.c.bg).statusBarsPadding()) {
        Row(Modifier.fillMaxWidth().padding(start = 20.dp, end = 6.dp, top = 10.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.tab_shelf), style = UType.Display)
                if (home.tales.isNotEmpty()) {
                    val n = streak(home.tales, home.today)
                    Text(
                        pluralStringResource(R.plurals.tales, home.tales.size, home.tales.size) + if (n > 1) " · " + pluralStringResource(R.plurals.nights_in_row, n, n) else "",
                        style = UType.Muted,
                    )
                }
            }
            if (home.tales.isNotEmpty()) IconButton(onClick = {
                if (making) return@IconButton
                making = true
                scope.launch {
                    val ok = Book.share(c, home.tales.sortedBy { it.endedAt }, home)
                    making = false
                    if (!ok) Toast.makeText(c, R.string.book_failed, Toast.LENGTH_SHORT).show()
                }
            }) { Icon(Icons.Rounded.MenuBook, stringResource(R.string.book), tint = if (making) U.c.faint else U.c.lamp) }
        }
        if (home.tales.isEmpty()) {
            Empty()
            return@Column
        }
        LazyColumn(contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp)) {
            byMonth.forEach { (month, tales) ->
                item(key = "m$month") {
                    Text(T.month(c, month).uppercase(), style = UType.Eyebrow, modifier = Modifier.padding(start = 4.dp, top = 20.dp, bottom = 10.dp))
                }
                items(tales, key = { it.id }) { t ->
                    TaleRow(t, home) { go.tale(t.id) }
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun TaleRow(t: Tale, home: Home, onClick: () -> Unit) {
    val c = ctx()
    UCard(onClick = onClick, padding = PaddingValues(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            GlassStrip(t.glass.take(3), home, 50.dp, 14.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(t.title, style = UType.Name, maxLines = 2)
                Spacer(Modifier.height(2.dp))
                Text(T.weekday(c, t.endedAt) + " · " + pluralStringResource(R.plurals.minutes, t.minutes.coerceAtLeast(1), t.minutes.coerceAtLeast(1)), style = UType.Small)
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (t.favourite) Icon(Icons.Rounded.Favorite, null, tint = Kind.QUIRK.glass(), modifier = Modifier.size(16.dp))
                if (t.recording.isNotEmpty()) Icon(Icons.Rounded.Mic, null, tint = U.c.muted, modifier = Modifier.size(16.dp))
                if (t.retold) Icon(Icons.Rounded.WbSunny, null, tint = U.c.lamp, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun Empty() {
    Column(Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Box(contentAlignment = Alignment.Center) { Glass(Art.ref("item:book"), Kind.ITEM.glass(), 200.dp, light = 0.5f) }
        Spacer(Modifier.height(12.dp))
        Text(stringResource(R.string.shelf_empty), style = UType.Title, textAlign = TextAlign.Center)
        Spacer(Modifier.height(6.dp))
        Text(stringResource(R.string.shelf_empty_sub), style = UType.Sub, textAlign = TextAlign.Center)
    }
}
