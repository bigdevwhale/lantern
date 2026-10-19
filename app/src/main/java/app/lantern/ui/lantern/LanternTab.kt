package app.lantern.ui.lantern

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.WbIncandescent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.lantern.R
import app.lantern.container
import app.lantern.content.Deck
import app.lantern.data.Evenings
import app.lantern.domain.Age
import app.lantern.domain.Hand
import app.lantern.domain.Kind
import app.lantern.domain.Lantern
import app.lantern.domain.Morning
import app.lantern.ui.Go
import app.lantern.ui.Home
import app.lantern.ui.T
import app.lantern.ui.art.Art
import app.lantern.ui.components.Chip
import app.lantern.ui.components.Glass
import app.lantern.ui.components.GlassStrip
import app.lantern.ui.components.PrimaryButton
import app.lantern.ui.components.Segmented
import app.lantern.ui.components.ShakeEffect
import app.lantern.ui.components.SlideRow
import app.lantern.ui.components.SoftButton
import app.lantern.ui.components.UCard
import app.lantern.ui.ctx
import app.lantern.ui.theme.U
import app.lantern.ui.theme.UType
import app.lantern.ui.theme.glass
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LanternTab(home: Home, go: Go) {
    val c = ctx()
    val container = c.container
    val e = home.settings.evening
    val scope = rememberCoroutineScope()
    var sharing by remember { mutableStateOf(false) }
    var lighting by remember { mutableStateOf(false) }

    fun light(code: String = "") {
        if (lighting) return
        lighting = true
        scope.launch {
            Evenings.deal(container, code)
            lighting = false
        }
    }

    ShakeEffect(enabled = !e.dealt) { light() }

    Column(Modifier.fillMaxSize().background(U.c.bg).statusBarsPadding()) {
        Row(Modifier.fillMaxWidth().padding(start = 20.dp, end = 6.dp, top = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.app_name), style = UType.Display)
                Text(T.weekday(c, home.now), style = UType.Muted)
            }
            IconButton(onClick = go::settings) { Icon(Icons.Rounded.Settings, stringResource(R.string.settings), tint = U.c.muted) }
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            if (!e.dealt) {
                home.morning?.let { MorningCard(it, home) }
                Projector(home)
                Spacer(Modifier.height(6.dp))
                Text(stringResource(R.string.lantern_lead), style = UType.Tale.copy(fontSize = 18.sp), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp))
                Spacer(Modifier.height(18.dp))
                HandPicker(home.settings.hand) { h -> container.write { settings.update { it.copy(hand = h) } } }
                Spacer(Modifier.height(20.dp))
                PrimaryButton(stringResource(R.string.light), { light() }, Modifier.fillMaxWidth(), icon = Icons.Rounded.WbIncandescent, enabled = !lighting)
                Spacer(Modifier.height(8.dp))
                Text(stringResource(R.string.or_shake), style = UType.Small, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(14.dp))
                SoftButton(stringResource(R.string.shared_evening), { sharing = true }, Modifier.fillMaxWidth(), color = U.c.muted, soft = U.c.surface, icon = Icons.Rounded.Group)
                Spacer(Modifier.height(24.dp))
            } else {
                Dealt(home, go)
            }
        }
    }
    if (sharing) ShareDialog(home.settings.hand, onDismiss = { sharing = false }) { code -> sharing = false; light(code) }
}

/** The glass on the table: revealed one by one, the lamp warming each. */
@Composable
private fun Dealt(home: Home, go: Go) {
    val c = ctx()
    val container = c.container
    val e = home.settings.evening
    val scope = rememberCoroutineScope()
    // Only a fresh deal is revealed slowly; coming back to the tab shows it at once.
    val instant = remember(e.seed) { Reveal.seed == e.seed }
    var shown by remember(e.seed) { mutableIntStateOf(if (instant) e.slides.size else 0) }
    LaunchedEffect(e.seed) {
        if (!instant) for (i in e.slides.indices) {
            delay(if (i == 0) 150 else 520)
            shown = i + 1
        }
        Reveal.seed = e.seed
    }
    if (e.code.isNotEmpty()) {
        UCard(padding = androidx.compose.foundation.layout.PaddingValues(14.dp)) {
            Text(stringResource(R.string.shared_on, Lantern.Code.pretty(e.code)), style = UType.Strong, color = U.c.lamp)
            Text(stringResource(R.string.shared_on_sub), style = UType.Muted)
        }
        Spacer(Modifier.height(8.dp))
    } else if (e.sequelOf > 0) {
        Text(stringResource(R.string.sequel_of, home.tale(e.sequelOf)?.title.orEmpty()), style = UType.Line, modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 6.dp))
    }
    Spacer(Modifier.height(6.dp))
    e.slides.forEachIndexed { i, s ->
        AnimatedVisibility(i < shown, enter = fadeIn(tween(700)) + slideInHorizontally(tween(700)) { -it / 6 }) {
            val warm = remember(e.seed, i) { Animatable(if (instant) 1f else 0.15f) }
            LaunchedEffect(e.seed, i) { warm.animateTo(1f, tween(1400)) }
            Column {
                SlideRow(
                    s, home, light = warm.value, friend = home.isFriend(s), seed = i,
                    canSwap = i !in e.swapped && !(e.sequelOf > 0 && s.kind == Kind.HERO),
                    onSwap = { scope.launch { Evenings.swap(container, i) } },
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
    if (shown >= e.slides.size) {
        Text(stringResource(R.string.swap_hint), style = UType.Small, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        PrimaryButton(stringResource(R.string.begin), go::story, Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = { container.write { Evenings.drop(this) } }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.put_away), style = UType.Button, color = U.c.muted)
        }
        Spacer(Modifier.height(24.dp))
    }
}

/** How big tonight's hand is: the age, which glass, how scary. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HandPicker(h: Hand, onChange: (Hand) -> Unit) {
    val c = ctx()
    Segmented(listOf(Age.LITTLE, Age.BIG), h.age, { T.age(c, it) }, { onChange(h.copy(age = it)) })
    Spacer(Modifier.height(6.dp))
    Text(T.ageSub(c, h.age), style = UType.Muted, modifier = Modifier.padding(horizontal = 4.dp))
    Spacer(Modifier.height(10.dp))
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Chip(T.kind(c, Kind.ITEM), h.item, { onChange(h.copy(item = !h.item)) }, Kind.ITEM.glass())
        if (h.age == Age.BIG) {
            Chip(T.kind(c, Kind.TROUBLE), h.trouble, { onChange(h.copy(trouble = !h.trouble)) }, Kind.TROUBLE.glass())
            Chip(T.kind(c, Kind.HELPER), h.helper, { onChange(h.copy(helper = !h.helper)) }, Kind.HELPER.glass())
            Chip(T.kind(c, Kind.QUIRK), h.quirk, { onChange(h.copy(quirk = !h.quirk)) }, Kind.QUIRK.glass())
        }
    }
    if (h.age == Age.BIG && h.trouble) {
        Spacer(Modifier.height(4.dp))
        Text(stringResource(R.string.how_scary), style = UType.Small, modifier = Modifier.padding(start = 4.dp, top = 6.dp, bottom = 4.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            (1..3).forEach { n -> Chip(T.fear(c, n), h.fear == n, { onChange(h.copy(fear = n)) }, Kind.TROUBLE.glass()) }
        }
    }
}

/**
 * The lantern on the table, throwing its circle on the wall: a slow show of whoever might
 * come out tonight.
 */
@Composable
private fun Projector(home: Home) {
    val box = home.box
    var who by remember { mutableStateOf(box.creatures.random().key) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(2600)
            who = box.creatures.filter { it.key != who }.random().key
        }
    }
    Box(Modifier.fillMaxWidth().height(400.dp), contentAlignment = Alignment.TopCenter) {
        Canvas(Modifier.fillMaxSize()) {
            // The beam: from the lens up to the circle on the wall.
            val lens = Offset(size.width / 2, size.height - 62.dp.toPx())
            val r = 104.dp.toPx()
            val disc = Offset(size.width / 2, 8.dp.toPx() + r)
            val beam = Path().apply {
                moveTo(lens.x - 12.dp.toPx(), lens.y)
                lineTo(disc.x - r * 0.92f, disc.y + r * 0.2f)
                lineTo(disc.x + r * 0.92f, disc.y + r * 0.2f)
                lineTo(lens.x + 12.dp.toPx(), lens.y)
                close()
            }
            drawPath(beam, Brush.verticalGradient(listOf(Color(0x22F2B04F), Color(0x55F2B04F)), disc.y, lens.y))
            lanternBody(lens)
        }
        Crossfade(who, animationSpec = tween(900), label = "projected") { k ->
            Glass(Art.hero(k, ""), Kind.HERO.glass(), 270.dp, light = 0.62f)
        }
    }
}

/** A brass magic lantern seen from the front, its lens at [lens]. */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.lanternBody(lens: Offset) {
    val u = density * 1f
    val brass = Brush.verticalGradient(listOf(Color(0xFFE2C27E), Color(0xFFB08A45), Color(0xFF6E5428)), lens.y - 50 * u, lens.y + 50 * u)
    drawRoundRect(Color(0xFF3A2B16), Offset(lens.x - 62 * u, lens.y + 34 * u), Size(124 * u, 14 * u), androidx.compose.ui.geometry.CornerRadius(6 * u))
    drawRoundRect(brass, Offset(lens.x - 50 * u, lens.y - 36 * u), Size(100 * u, 72 * u), androidx.compose.ui.geometry.CornerRadius(10 * u))
    drawRect(brass, Offset(lens.x - 12 * u, lens.y - 66 * u), Size(24 * u, 32 * u))
    drawRoundRect(Color(0xFF8C6A33), Offset(lens.x - 20 * u, lens.y - 74 * u), Size(40 * u, 10 * u), androidx.compose.ui.geometry.CornerRadius(4 * u))
    // Little vents with the flame showing through.
    for (i in -1..1) drawRoundRect(Color(0xFFF6C46A), Offset(lens.x + i * 12 * u - 3 * u, lens.y - 58 * u), Size(6 * u, 14 * u), androidx.compose.ui.geometry.CornerRadius(3 * u))
    drawCircle(Brush.radialGradient(listOf(Color(0xFFFFF0C8), Color(0xFFF2B04F), Color(0xFF9A5E1C)), lens, 26 * u), 24 * u, lens)
    drawCircle(Color(0xFF6E5428), 26 * u, lens, style = Stroke(5 * u))
    drawCircle(Color(0x33FFFFFF), 10 * u, lens - Offset(8 * u, 8 * u))
    for (x in listOf(-38f, 38f)) drawCircle(Color(0xFF6E5428), 4 * u, lens + Offset(x * u, 22 * u))
}

/** The morning after: last night's glass and three questions for the child. */
@Composable
private fun MorningCard(t: app.lantern.data.Tale, home: Home) {
    val c = ctx()
    val container = c.container
    UCard {
        Text(stringResource(R.string.morning_title).uppercase(), style = UType.Eyebrow, color = U.c.lamp)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            GlassStrip(t.glass.take(3), home, 52.dp)
            Spacer(Modifier.width(12.dp))
            Text(t.title, style = UType.Name, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(10.dp))
        Text(stringResource(R.string.morning_ask), style = UType.Muted)
        Morning.questions(t).forEach { q -> Text("·  " + Deck.morning[q].of(home.ru), style = UType.Tale.copy(fontSize = 17.sp), modifier = Modifier.padding(top = 4.dp)) }
        Spacer(Modifier.height(12.dp))
        SoftButton(stringResource(R.string.morning_done), { container.write { db.dao().update(t.copy(retold = true)) } }, Modifier.fillMaxWidth(), icon = Icons.Rounded.Check)
    }
    Spacer(Modifier.height(10.dp))
}

/** A shared evening: one phone makes a code, the other types it in, and both deal the same glass. */
@Composable
private fun ShareDialog(hand: Hand, onDismiss: () -> Unit, onCode: (String) -> Unit) {
    val mine = remember { Lantern.Code.make(hand, Random(System.nanoTime())) }
    var typed by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = U.c.surface2,
        title = { Text(stringResource(R.string.shared_evening), style = UType.Title) },
        text = {
            Column {
                Text(stringResource(R.string.shared_lead), style = UType.Sub)
                Spacer(Modifier.height(14.dp))
                Text(stringResource(R.string.shared_mine), style = UType.Small)
                Text(Lantern.Code.pretty(mine), style = UType.Hero.copy(color = U.c.lamp, fontSize = 40.sp, fontFeatureSettings = "lnum"))
                Spacer(Modifier.height(4.dp))
                SoftButton(stringResource(R.string.shared_use_mine), { onCode(mine) }, Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                Text(stringResource(R.string.shared_theirs), style = UType.Small)
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    typed, { typed = it.filter(Char::isDigit).take(5) },
                    singleLine = true,
                    textStyle = UType.Title.copy(fontFeatureSettings = "lnum", letterSpacing = 4.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    placeholder = { Text("2·4817", color = U.c.faint, style = UType.Title) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = U.c.lamp, unfocusedBorderColor = U.c.line, cursorColor = U.c.lamp),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onCode(typed) }, enabled = Lantern.Code.hand(typed, hand) != null) {
                Text(stringResource(R.string.shared_join), style = UType.Button, color = if (Lantern.Code.hand(typed, hand) != null) U.c.lamp else U.c.faint)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel), color = U.c.muted, style = UType.Button) } },
    )
}

/** The deal last shown, so coming back to the tab doesn't reveal it all over again. */
private object Reveal {
    var seed = 0L
}
