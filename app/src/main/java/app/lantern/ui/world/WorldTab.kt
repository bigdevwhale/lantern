package app.lantern.ui.world

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WbIncandescent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.lantern.R
import app.lantern.container
import app.lantern.content.Deck
import app.lantern.data.Evenings
import app.lantern.domain.Kind
import app.lantern.domain.Slide
import app.lantern.domain.World
import app.lantern.ui.Go
import app.lantern.ui.Home
import app.lantern.ui.T
import app.lantern.ui.art.Places
import app.lantern.ui.components.Glass
import app.lantern.ui.components.GlassStrip
import app.lantern.ui.components.PrimaryButton
import app.lantern.ui.components.SectionTitle
import app.lantern.ui.components.UCard
import app.lantern.ui.ctx
import app.lantern.ui.theme.U
import app.lantern.ui.theme.UType
import app.lantern.ui.theme.glass
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WorldTab(home: Home, go: Go) {
    val c = ctx()
    val container = c.container
    val scope = rememberCoroutineScope()
    var friend by remember { mutableStateOf<World.Friend?>(null) }
    var place by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize().background(U.c.bg).statusBarsPadding()) {
        Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 4.dp)) {
            Text(stringResource(R.string.tab_world), style = UType.Display)
            Text(stringResource(R.string.world_sub, home.visited.size, Deck.places.size), style = UType.Muted)
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(8.dp))
            WorldMap(home) { place = it }
            SectionTitle(stringResource(R.string.friends))
            if (home.friends.isEmpty()) {
                Text(stringResource(R.string.friends_empty), style = UType.Sub)
            } else {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    home.friends.take(60).forEach { f ->
                        Column(
                            Modifier.width(100.dp).clip(U.RMd).clickable { friend = f }.padding(vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Glass(home.figure(f.slide), Kind.HERO.glass(), 92.dp, halo = false, flicker = false, light = 0.9f)
                            Text(home.title(f.slide.copy(b = "")), style = UType.Strong.copy(fontSize = 14.sp), textAlign = TextAlign.Center, maxLines = 2)
                            Text(pluralStringResource(R.plurals.tales, f.count, f.count), style = UType.Small)
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    friend?.let { f ->
        ModalBottomSheet(onDismissRequest = { friend = null }, containerColor = U.c.surface2) {
            Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Glass(home.figure(f.slide), Kind.HERO.glass(), 200.dp)
                Text(home.title(f.slide), style = UType.Display, textAlign = TextAlign.Center)
                Text(stringResource(R.string.friend_since, T.date(c, f.first)), style = UType.Muted)
                Spacer(Modifier.height(14.dp))
                f.tales.mapNotNull(home::tale).sortedByDescending { it.endedAt }.take(6).forEach { t ->
                    UCard(onClick = { friend = null; go.tale(t.id) }, padding = androidx.compose.foundation.layout.PaddingValues(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            GlassStrip(t.glass.take(3), home, 38.dp, 10.dp)
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Text(t.title, style = UType.Strong, maxLines = 1)
                                Text(T.date(c, t.endedAt), style = UType.Small)
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
                Spacer(Modifier.height(8.dp))
                PrimaryButton(stringResource(R.string.call_into_tale), {
                    friend = null
                    scope.launch {
                        Evenings.deal(container, keep = mapOf(Kind.HERO to f.slide))
                        go.lantern()
                    }
                }, Modifier.fillMaxWidth(), icon = Icons.Rounded.WbIncandescent)
            }
        }
    }
    place?.let { key ->
        val p = Deck.place(key)
        val tales = home.tales.filter { t -> t.glass.any { it.kind == Kind.PLACE && it.a == key } }
        ModalBottomSheet(onDismissRequest = { place = null }, containerColor = U.c.surface2) {
            Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Glass(Places.all[key], Kind.PLACE.glass(), 200.dp)
                Text(p?.name?.of(home.ru).orEmpty(), style = UType.Display, textAlign = TextAlign.Center)
                Text(pluralStringResource(R.plurals.tales, tales.size, tales.size), style = UType.Muted)
                Spacer(Modifier.height(12.dp))
                tales.take(6).forEach { t ->
                    UCard(onClick = { place = null; go.tale(t.id) }, padding = androidx.compose.foundation.layout.PaddingValues(10.dp)) {
                        Text(t.title, style = UType.Strong, maxLines = 1)
                        Text(T.date(c, t.endedAt), style = UType.Small)
                    }
                    Spacer(Modifier.height(8.dp))
                }
                PrimaryButton(stringResource(R.string.go_back_there), {
                    place = null
                    scope.launch {
                        Evenings.deal(container, keep = mapOf(Kind.PLACE to Slide(Kind.PLACE, key)))
                        go.lantern()
                    }
                }, Modifier.fillMaxWidth(), icon = Icons.Rounded.WbIncandescent)
            }
        }
    }
}

/**
 * A night map of everywhere the tales can go. Visited places are lit glass joined by a dotted
 * road in the order they were found; the rest wait, dark.
 */
@Composable
private fun WorldMap(home: Home, onPlace: (String) -> Unit) {
    val visited = home.visited.toMap()
    val order = home.visited.map { it.first }
    val places = Deck.places.filter { !it.rare || it.key in visited || "place:${it.key}" in home.settings.opened }
    val spots = remember(places.size) { layout(places.map { it.key }) }
    BoxWithConstraints(
        Modifier.fillMaxWidth().aspectRatio(0.74f).clip(U.RLg)
            .background(Brush.radialGradient(listOf(Color(0xFF1E1822), Color(0xFF120E14)))).border(1.dp, U.c.line, U.RLg),
    ) {
        val w = constraints.maxWidth.toFloat()
        val h = constraints.maxHeight.toFloat()
        Canvas(Modifier.fillMaxSize()) {
            // Faint stars, and the road between the places found so far.
            val r = kotlin.random.Random(7)
            repeat(70) { drawCircle(Color(0xFFF1E4C8).copy(alpha = 0.05f + r.nextFloat() * 0.12f), 1f + r.nextFloat() * 1.6f, Offset(r.nextFloat() * size.width, r.nextFloat() * size.height)) }
            val pts = order.mapNotNull { spots[it] }.map { Offset(it.first * size.width, it.second * size.height) }
            for (i in 1 until pts.size) {
                drawLine(Color(0xFFC9A25A).copy(alpha = 0.55f), pts[i - 1], pts[i], 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(6.dp.toPx(), 6.dp.toPx())))
            }
            pts.firstOrNull()?.let { drawCircle(Color(0xFFC9A25A), 4.dp.toPx(), it, style = Stroke(2.dp.toPx())) }
        }
        val dp = with(androidx.compose.ui.platform.LocalDensity.current) { 1.dp.toPx() }
        places.forEach { p ->
            val (x, y) = spots[p.key] ?: return@forEach
            val seen = p.key in visited
            val size = if (seen) 58.dp else 34.dp
            val px = size.value * dp
            Box(
                Modifier.offset(x = ((x * w - px / 2) / dp).dp, y = ((y * h - px / 2) / dp).dp).size(size).clip(CircleShape).clickable(enabled = seen) { onPlace(p.key) },
                contentAlignment = Alignment.Center,
            ) {
                Glass(if (seen) Places.all[p.key] else null, Kind.PLACE.glass(), size, light = if (seen) 0.95f else 0.16f, halo = false, flicker = false)
                if (!seen) Text("?", style = UType.Strong.copy(color = U.c.faint))
            }
        }
    }
}

/**
 * Fixed spots on the map: a staggered grid, every place in its own cell with a little wobble,
 * shuffled the same way for every family so the map doesn't change between evenings.
 */
private fun layout(keys: List<String>): Map<String, Pair<Float, Float>> {
    val cols = 5
    val rows = (keys.size + cols - 1) / cols
    val r = kotlin.random.Random(42)
    return keys.sorted().shuffled(r).mapIndexed { i, k ->
        val row = i / cols
        val col = i % cols
        val shift = if (row % 2 == 1) 0.5f else 0.15f
        val x = (col + shift + 0.2f + (r.nextFloat() - 0.5f) * 0.25f) / (cols + 0.6f)
        val y = (row + 0.5f + (r.nextFloat() - 0.5f) * 0.3f) / rows
        k to (x.coerceIn(0.08f, 0.92f) to y.coerceIn(0.06f, 0.94f))
    }.toMap()
}
