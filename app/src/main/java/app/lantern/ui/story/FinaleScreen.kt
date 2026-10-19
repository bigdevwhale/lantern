package app.lantern.ui.story

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.rounded.NightsStay
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.lantern.R
import app.lantern.container
import app.lantern.data.Evenings
import app.lantern.data.Lullaby
import app.lantern.domain.Kind
import app.lantern.domain.Slide
import app.lantern.sound.Ambient
import app.lantern.ui.Go
import app.lantern.ui.Home
import app.lantern.ui.T
import app.lantern.ui.components.Chip
import app.lantern.ui.components.Glass
import app.lantern.ui.components.PrimaryButton
import app.lantern.ui.components.SectionTitle
import app.lantern.ui.components.SoftButton
import app.lantern.ui.components.UTextField
import app.lantern.ui.ctx
import app.lantern.ui.theme.U
import app.lantern.ui.theme.UType
import app.lantern.ui.theme.glass
import kotlinx.coroutines.launch

/** The quiet ending: the last glass, a name for the tale, and then the lantern goes out. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FinaleScreen(home: Home, go: Go) {
    val c = ctx()
    val container = c.container
    val s = home.settings
    val ev = s.evening
    val scope = rememberCoroutineScope()
    // Once on the shelf the evening is cleared; the night falls on what was kept here.
    var night by rememberSaveable { mutableStateOf<String?>(null) }
    var reEnded by rememberSaveable { mutableStateOf(false) }
    var title by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(ev.slides) { if (title.isEmpty() && ev.dealt) title = home.taleName(ev.slides) }
    LaunchedEffect(ev.dealt, night) { if (!ev.dealt && night == null) go.home() }
    LaunchedEffect(ev.dealt, ev.ending) { if (ev.dealt && ev.ending == null) Evenings.end(container) }

    night?.let { end -> Night(Slide.decode(end), home, s.lullaby, s.lullabyMin) { Ambient.stop(); go.home() }; return }
    if (!ev.dealt) return
    val end = ev.ending

    Column(Modifier.fillMaxSize().background(U.c.bg).statusBarsPadding().imePadding()) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp)) {
            Spacer(Modifier.height(18.dp))
            Text(stringResource(R.string.finale_title).uppercase(), style = UType.Eyebrow, color = Kind.ENDING.glass(), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Glass(end?.let { home.figure(it) }, Kind.ENDING.glass(), 270.dp)
            }
            Text(end?.let { home.title(it) }.orEmpty(), style = UType.Tale.copy(fontSize = 22.sp, lineHeight = 31.sp), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp))
            if (!reEnded) {
                TextButton(onClick = { reEnded = true; scope.launch { Evenings.reEnd(container) } }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.other_ending), style = UType.Button, color = U.c.muted)
                }
            }
            Text(stringResource(R.string.finale_slow), style = UType.Muted, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 4.dp))

            SectionTitle(stringResource(R.string.name_the_tale))
            UTextField(title, { title = it })
            Spacer(Modifier.height(10.dp))
            UTextField(note, { note = it }, placeholder = stringResource(R.string.note_hint), minLines = 3)

            SectionTitle(stringResource(R.string.lullaby))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Lullaby.entries.forEach { l -> Chip(T.lullaby(c, l), s.lullaby == l, { container.write { settings.update { it.copy(lullaby = l) } } }) }
            }
            if (s.lullaby != Lullaby.NONE) FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(5, 15, 30, 45).forEach { m -> Chip(pluralStringResource(R.plurals.minutes, m, m), s.lullabyMin == m, { container.write { settings.update { it.copy(lullabyMin = m) } } }) }
            }
            Spacer(Modifier.height(20.dp))
        }
        Column(Modifier.padding(16.dp).navigationBarsPadding()) {
            PrimaryButton(stringResource(R.string.shelf_and_sleep), {
                val keep = end?.encode() ?: ""
                val name = title.ifBlank { home.taleName(ev.slides) }
                // Night first: once the tale is saved the evening is gone and this screen would leave.
                night = keep
                Ambient.play(container.scope, s.lullaby, s.lullabyMin)
                container.write { Evenings.save(this, name, note, if (s.relay) s.tellers else emptyList()) }
            }, Modifier.fillMaxWidth(), icon = Icons.Rounded.NightsStay)
            Spacer(Modifier.height(8.dp))
            SoftButton(stringResource(R.string.shelf_only), {
                scope.launch {
                    val id = Evenings.save(container, title.ifBlank { home.taleName(ev.slides) }, note, if (s.relay) s.tellers else emptyList())
                    go.home()
                    go.tale(id)
                }
            }, Modifier.fillMaxWidth(), color = U.c.muted, soft = U.c.surface)
        }
    }
}

/**
 * Lights out: the ending glass dims to nothing while the night sounds play, and the screen goes
 * black and lets itself sleep. A tap stops it all.
 */
@Composable
private fun Night(end: Slide?, home: Home, lullaby: Lullaby, minutes: Int, onStop: () -> Unit) {
    val c = ctx()
    val playing by Ambient.playing.collectAsState()
    val progress by Ambient.progress.collectAsState()
    val fade = remember { Animatable(1f) }
    val dimMs = if (lullaby == Lullaby.NONE) 20_000 else minOf(minutes * 60_000, 90_000)
    LaunchedEffect(Unit) { fade.animateTo(0f, tween(dimMs, easing = LinearEasing)) }
    val view = LocalView.current
    val activity = c as? Activity
    DisposableEffect(Unit) {
        view.keepScreenOn = true
        activity?.window?.let { w -> w.attributes = w.attributes.apply { screenBrightness = 0.02f } }
        onDispose {
            view.keepScreenOn = false
            activity?.window?.let { w -> w.attributes = w.attributes.apply { screenBrightness = -1f } }
        }
    }
    // When the sound has played out, let the phone sleep: the screen may turn off now.
    LaunchedEffect(playing, fade.value) { if (playing == null && fade.value == 0f) view.keepScreenOn = false }
    BackHandler(onBack = onStop)
    Box(
        Modifier.fillMaxSize().background(Color.Black).clickable(remember { MutableInteractionSource() }, null, onClick = onStop),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Glass(end?.let { home.figure(it) }, Kind.ENDING.glass(), 220.dp, light = 0.08f + 0.6f * fade.value, halo = fade.value > 0.02f)
            Spacer(Modifier.height(18.dp))
            Text(stringResource(R.string.good_night), style = UType.Display.copy(color = U.c.faint.copy(alpha = 0.25f + 0.6f * fade.value)))
            Spacer(Modifier.height(6.dp))
            if (playing != null) Text(
                T.lullaby(c, lullaby) + " · " + T.clock(((1f - progress) * minutes * 60_000).toLong()),
                style = UType.Small.copy(color = U.c.faint.copy(alpha = 0.5f)),
            )
            Spacer(Modifier.height(30.dp))
            Text(stringResource(R.string.tap_to_stop), style = UType.Small.copy(color = U.c.faint.copy(alpha = 0.35f)))
        }
    }
}
