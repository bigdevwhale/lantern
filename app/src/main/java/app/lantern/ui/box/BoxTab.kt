package app.lantern.ui.box

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.lantern.R
import app.lantern.container
import app.lantern.content.Deck
import app.lantern.content.Season
import app.lantern.domain.Kind
import app.lantern.domain.Names
import app.lantern.domain.Parcels
import app.lantern.ui.Go
import app.lantern.ui.Home
import app.lantern.ui.T
import app.lantern.ui.art.Art
import app.lantern.ui.art.Figure
import app.lantern.ui.art.Places
import app.lantern.ui.art.Treasures
import app.lantern.ui.components.Glass
import app.lantern.ui.components.PrimaryButton
import app.lantern.ui.components.SectionTitle
import app.lantern.ui.components.UCard
import app.lantern.ui.ctx
import app.lantern.ui.theme.U
import app.lantern.ui.theme.UType
import app.lantern.ui.theme.glass
import kotlinx.coroutines.launch
import kotlin.random.Random

/** One glass in the box, as the box shows it. */
private class Pane(val key: String, val fig: Figure?, val kind: Kind, val name: String, val open: Boolean, val sleeping: Boolean = false)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BoxTab(home: Home, go: Go) {
    val c = ctx()
    val container = c.container
    val scope = rememberCoroutineScope()
    val opened = home.settings.opened
    var look by remember { mutableStateOf<Pane?>(null) }
    var unwrapped by remember { mutableStateOf<String?>(null) }
    val box = home.box
    val ru = home.ru

    val creatures = Deck.creatures.map { cr ->
        Pane("creature:${cr.key}", Art.hero(cr.key, ""), Kind.HERO, cr.name.of(ru), !cr.rare || "creature:${cr.key}" in opened, cr.season == Season.WINTER && box.creatures.none { it.key == cr.key })
    }
    val roles = Deck.roles.map { r ->
        Pane("role:${r.key}", Art.hero("fox", r.key), Kind.HERO, r.of(ru, app.lantern.content.G.M).replaceFirstChar { it.titlecase() }, !r.rare || "role:${r.key}" in opened)
    }
    val places = Deck.places.map { p ->
        Pane("place:${p.key}", Places.all[p.key], Kind.PLACE, p.name.of(ru), !p.rare || "place:${p.key}" in opened, p.season == Season.WINTER && box.places.none { it.key == p.key })
    }
    val things = Deck.things.map { t -> Pane("thing:${t.key}", Treasures.all[t.key], Kind.ITEM, t.name.of(ru), !t.rare || "thing:${t.key}" in opened) }
    val total = creatures.size + roles.size + places.size + things.size
    val have = (creatures + roles + places + things).count { it.open }

    Column(Modifier.fillMaxSize().background(U.c.bg).statusBarsPadding()) {
        Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 4.dp)) {
            Text(stringResource(R.string.tab_box), style = UType.Display)
            Text(stringResource(R.string.box_sub, have, total), style = UType.Muted)
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(8.dp))
            if (home.parcels > 0) {
                UCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Glass(Art.ref("glyph:gift"), Kind.TWIST.glass(), 84.dp)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(stringResource(R.string.parcel_title), style = UType.Title)
                            Text(stringResource(R.string.parcel_sub), style = UType.Muted)
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    PrimaryButton(stringResource(R.string.parcel_open), {
                        scope.launch {
                            val key = Parcels.open(opened, Random(System.nanoTime())) ?: return@launch
                            container.settings.update { it.copy(opened = it.opened + key) }
                            unwrapped = key
                        }
                    }, Modifier.fillMaxWidth())
                }
            } else if (Parcels.locked(opened).isNotEmpty()) {
                Text(stringResource(R.string.parcel_how), style = UType.Sub, modifier = Modifier.padding(horizontal = 4.dp))
            }

            SectionTitle(stringResource(R.string.own_glass))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                home.drawings.forEach { d ->
                    Tile(Pane("own:${d.id}", Art.painted(d.id, d.strokes), d.kindOf, d.name, true), 76.dp) { go.draw(d.id) }
                }
                Column(
                    Modifier.width(76.dp).clip(U.RMd).clickable { go.draw(0) }.padding(vertical = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(Modifier.size(70.dp).clip(CircleShape).border(2.dp, U.c.line, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Rounded.Add, stringResource(R.string.draw_new), tint = U.c.lamp, modifier = Modifier.size(30.dp))
                    }
                    Text(stringResource(R.string.draw_new), style = UType.Small, textAlign = TextAlign.Center)
                }
            }
            if (home.drawings.isEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(stringResource(R.string.own_glass_sub), style = UType.Muted, modifier = Modifier.padding(horizontal = 4.dp))
            }

            Section(stringResource(R.string.box_creatures), creatures) { look = it }
            Section(stringResource(R.string.box_roles), roles) { look = it }
            Section(stringResource(R.string.box_places), places) { look = it }
            Section(stringResource(R.string.box_things), things) { look = it }
            Spacer(Modifier.height(24.dp))
        }
    }

    look?.let { p ->
        AlertDialog(
            onDismissRequest = { look = null },
            containerColor = U.c.surface2,
            confirmButton = { TextButton(onClick = { look = null }) { Text(stringResource(R.string.ok), color = U.c.lamp, style = UType.Button) } },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Glass(if (p.open) p.fig else null, p.kind.glass(), 220.dp, light = if (p.open && !p.sleeping) 1f else 0.25f)
                    Text(if (p.open) p.name else stringResource(R.string.locked_name), style = UType.Title, textAlign = TextAlign.Center)
                    Text(
                        stringResource(
                            when {
                                !p.open -> R.string.locked_sub
                                p.sleeping -> R.string.winter_sub
                                else -> R.string.in_the_box
                            }
                        ),
                        style = UType.Muted, textAlign = TextAlign.Center,
                    )
                }
            },
        )
    }
    unwrapped?.let { key -> Unwrapped(key, home) { unwrapped = null } }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Section(title: String, panes: List<Pane>, onLook: (Pane) -> Unit) {
    SectionTitle(title + "  " + panes.count { it.open } + "/" + panes.size)
    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        panes.forEach { p -> Tile(p, 64.dp) { onLook(p) } }
    }
}

@Composable
private fun Tile(p: Pane, size: Dp, onClick: () -> Unit) {
    Column(Modifier.width(size + 8.dp).clip(U.RMd).clickable(onClick = onClick), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.Center) {
            Glass(if (p.open) p.fig else null, p.kind.glass(), size, light = if (!p.open) 0.14f else if (p.sleeping) 0.35f else 0.92f, halo = false, flicker = false)
            if (!p.open) Text("?", style = UType.Title.copy(color = U.c.faint))
        }
        Text(if (p.open) p.name else "", style = UType.Small.copy(fontSize = 11.sp, color = if (p.open) U.c.muted else Color.Transparent), textAlign = TextAlign.Center, maxLines = 2)
    }
}

/** A parcel unwrapped: the new glass grows out of the dark. */
@Composable
private fun Unwrapped(key: String, home: Home, onDone: () -> Unit) {
    val (kind, k) = key.split(':', limit = 2).let { it[0] to it[1] }
    val fig = when (kind) {
        "creature" -> Art.hero(k, "")
        "role" -> Art.hero("fox", k)
        "place" -> Places.all[k]
        else -> Treasures.all[k]
    }
    val glassKind = when (kind) {
        "place" -> Kind.PLACE
        "thing" -> Kind.ITEM
        else -> Kind.HERO
    }
    val name = when (kind) {
        "creature" -> Deck.creature(k)?.name?.of(home.ru)
        "role" -> Names.hero("fox", k, home.ru)
        "place" -> Deck.place(k)?.name?.of(home.ru)
        else -> Deck.thing(k)?.name?.of(home.ru)
    }.orEmpty()
    val grow = remember { Animatable(0.3f) }
    LaunchedEffect(Unit) { grow.animateTo(1f, tween(1600)) }
    AlertDialog(
        onDismissRequest = onDone,
        containerColor = U.c.surface2,
        confirmButton = { TextButton(onClick = onDone) { Text(stringResource(R.string.into_box), color = U.c.lamp, style = UType.Button) } },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.parcel_had).uppercase(), style = UType.Eyebrow, color = U.c.lamp)
                Spacer(Modifier.height(6.dp))
                Glass(fig, glassKind.glass(), 230.dp, Modifier.scale(grow.value), light = grow.value)
                Text(name, style = UType.Display, textAlign = TextAlign.Center)
                Text(stringResource(R.string.parcel_now), style = UType.Muted, textAlign = TextAlign.Center)
            }
        },
    )
}
