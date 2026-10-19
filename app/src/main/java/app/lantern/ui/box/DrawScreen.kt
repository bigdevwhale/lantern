package app.lantern.ui.box

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.lantern.R
import app.lantern.container
import app.lantern.data.Drawing
import app.lantern.domain.Kind
import app.lantern.ui.Go
import app.lantern.ui.Home
import app.lantern.ui.T
import app.lantern.ui.art.Strokes
import app.lantern.ui.components.Chip
import app.lantern.ui.components.ConfirmDialog
import app.lantern.ui.components.PrimaryButton
import app.lantern.ui.components.TopBar
import app.lantern.ui.components.UTextField
import app.lantern.ui.components.glass
import app.lantern.ui.ctx
import app.lantern.ui.theme.U
import app.lantern.ui.theme.UType
import app.lantern.ui.theme.glass

/** The child paints a slide with a finger; it comes out as a cut-out, like all the others. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DrawScreen(id: Long, home: Home, go: Go) {
    val c = ctx()
    val container = c.container
    val old = home.drawing(id)
    val strokes = remember { mutableStateListOf<Strokes.Stroke>() }
    var name by remember { mutableStateOf("") }
    var kind by remember { mutableStateOf(Kind.HERO) }
    var width by remember { mutableStateOf(7f) }
    var erase by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf(false) }
    var loaded by remember { mutableStateOf(false) }
    var current by remember { mutableStateOf<List<Pair<Float, Float>>>(emptyList()) }
    LaunchedEffect(old) {
        if (old != null && !loaded) {
            strokes.addAll(Strokes.parse(old.strokes)); name = old.name; kind = old.kindOf; loaded = true
        }
    }
    val text = Strokes.write(strokes + listOfNotNull(current.takeIf { it.isNotEmpty() }?.let { Strokes.Stroke(if (erase) -width else width, it) }))
    val fig = remember(text) { Strokes.figure(text) }

    Column(Modifier.fillMaxSize().background(U.c.bg).statusBarsPadding().imePadding()) {
        TopBar(stringResource(if (old == null) R.string.draw_new else R.string.draw_edit), go::back) {
            IconButton(onClick = { if (strokes.isNotEmpty()) strokes.removeAt(strokes.lastIndex) }) { Icon(Icons.AutoMirrored.Rounded.Undo, stringResource(R.string.undo), tint = U.c.muted) }
            if (old != null) IconButton(onClick = { deleting = true }) { Icon(Icons.Rounded.DeleteOutline, stringResource(R.string.delete), tint = U.c.muted) }
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState(), enabled = false).padding(horizontal = 16.dp)) {
            Box(Modifier.fillMaxWidth().aspectRatio(1f), contentAlignment = Alignment.Center) {
                Canvas(
                    Modifier.fillMaxSize()
                        .pointerInput(erase, width) {
                            fun grid(o: Offset) = toGrid(o, size.width.toFloat(), size.height.toFloat())
                            detectDragGestures(
                                onDragStart = { current = listOf(grid(it)) },
                                onDrag = { ch, _ -> current = current + grid(ch.position) },
                                onDragEnd = { strokes += Strokes.Stroke(if (erase) -width else width, current); current = emptyList() },
                                onDragCancel = { current = emptyList() },
                            )
                        }
                        .pointerInput(erase, width) {
                            detectTapGestures { o -> strokes += Strokes.Stroke(if (erase) -width else width, listOf(toGrid(o, size.width.toFloat(), size.height.toFloat()))) }
                        },
                ) {
                    glass(fig, kind.glass(), center, size.minDimension / 2 * 0.94f, seed = 3)
                }
            }
            Spacer(Modifier.height(10.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(3f to R.string.brush_thin, 7f to R.string.brush_mid, 14f to R.string.brush_thick).forEach { (w, l) ->
                    Chip(stringResource(l), !erase && width == w, { erase = false; width = w })
                }
                Chip(stringResource(R.string.brush_cut), erase, { erase = true; width = 6f })
            }
            Spacer(Modifier.height(4.dp))
            Text(stringResource(R.string.draw_hint), style = UType.Small)
        }
        Column(Modifier.padding(16.dp).navigationBarsPadding()) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(Kind.HERO, Kind.HELPER, Kind.ITEM, Kind.PLACE).forEach { k -> Chip(T.kind(c, k), kind == k, { kind = k }, k.glass()) }
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                UTextField(name, { name = it }, Modifier.weight(1f), placeholder = stringResource(R.string.draw_name), words = true)
            }
            Spacer(Modifier.height(10.dp))
            PrimaryButton(stringResource(R.string.save), {
                val s = Strokes.write(strokes)
                container.write {
                    if (old == null) db.dao().insert(Drawing(name = name.trim(), kind = kind.name, strokes = s))
                    else db.dao().update(old.copy(name = name.trim(), kind = kind.name, strokes = s))
                }
                go.back()
            }, Modifier.fillMaxWidth(), enabled = strokes.isNotEmpty() && name.isNotBlank())
        }
    }
    if (deleting && old != null) ConfirmDialog(stringResource(R.string.delete_drawing), stringResource(R.string.delete_drawing_text), stringResource(R.string.delete), {
        container.write { db.dao().delete(old) }
        go.back()
    }, { deleting = false }, danger = true)
}

/** A touch on the canvas to the 100-unit grid the cut-out is drawn on (the inner square of the glass). */
private fun toGrid(o: Offset, w: Float, h: Float): Pair<Float, Float> {
    val fig = minOf(w, h) * 0.94f * 0.9f
    return ((o.x - w / 2) / fig * 100f + 50f).coerceIn(0f, 100f) to ((o.y - h / 2) / fig * 100f + 50f).coerceIn(0f, 100f)
}
