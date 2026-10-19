package app.lantern.ui.story

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import app.lantern.R
import app.lantern.container
import app.lantern.content.Deck
import app.lantern.data.Evenings
import app.lantern.domain.Slide
import app.lantern.sound.Chime
import app.lantern.ui.Go
import app.lantern.ui.Home
import app.lantern.ui.T
import app.lantern.ui.components.Chip
import app.lantern.ui.components.FaceDownEffect
import app.lantern.ui.components.Glass
import app.lantern.ui.components.GlassStrip
import app.lantern.ui.components.PrimaryButton
import app.lantern.ui.components.SoftButton
import app.lantern.ui.components.Toggle
import app.lantern.ui.components.UCard
import app.lantern.ui.components.UTextField
import app.lantern.ui.components.mirror
import app.lantern.ui.components.rememberNow
import app.lantern.ui.ctx
import app.lantern.ui.theme.U
import app.lantern.ui.theme.UType
import app.lantern.ui.theme.glass
import kotlinx.coroutines.launch

@Composable
fun StoryScreen(home: Home, go: Go) {
    val e = home.settings.evening
    LaunchedEffect(e.dealt) { if (!e.dealt) go.home() }
    if (!e.dealt) return
    if (!e.telling) BeforeTelling(home, go) else Telling(home, go)
}

/** Tonight's options, just before the first word: the candle, turns round the table, the microphone. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BeforeTelling(home: Home, go: Go) {
    val c = ctx()
    val container = c.container
    val s = home.settings
    val scope = rememberCoroutineScope()
    var tellers by rememberSaveable { mutableStateOf(s.tellers.joinToString(", ")) }
    val askMic = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { ok ->
        container.write { settings.update { it.copy(record = ok) } }
    }
    val micOk = ContextCompat.checkSelfPermission(c, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

    Column(Modifier.fillMaxSize().background(U.c.bg).statusBarsPadding()) {
        Row(Modifier.fillMaxWidth().height(60.dp).padding(horizontal = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = go::home) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, stringResource(R.string.back), tint = U.c.ink) }
            Text(stringResource(R.string.before_title), style = UType.Title, modifier = Modifier.weight(1f))
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { GlassStrip(e(home).slides, home, 64.dp, 16.dp) }
            Spacer(Modifier.height(8.dp))
            Text(home.taleName(e(home).slides), style = UType.Name, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            UCard {
                Text(stringResource(R.string.candle), style = UType.Strong)
                Text(stringResource(R.string.candle_sub), style = UType.Muted)
                Spacer(Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(0, 5, 10, 15, 20, 30).forEach { m ->
                        Chip(if (m == 0) stringResource(R.string.candle_none) else pluralStringResource(R.plurals.minutes, m, m), s.candle == m, {
                            container.write { settings.update { it.copy(candle = m) } }
                        })
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            UCard {
                Toggle(stringResource(R.string.relay), stringResource(R.string.relay_sub), s.relay) { v -> container.write { settings.update { it.copy(relay = v) } } }
                if (s.relay) {
                    Spacer(Modifier.height(8.dp))
                    UTextField(tellers, { tellers = it }, placeholder = stringResource(R.string.tellers_hint), words = true)
                    Spacer(Modifier.height(8.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(30, 60, 90, 120).forEach { sec -> Chip(stringResource(R.string.seconds_n, sec), s.turn == sec, { container.write { settings.update { it.copy(turn = sec) } } }) }
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            UCard {
                Toggle(stringResource(R.string.record), stringResource(R.string.record_sub), s.record && micOk) { v ->
                    if (v && !micOk) askMic.launch(Manifest.permission.RECORD_AUDIO) else container.write { settings.update { it.copy(record = v) } }
                }
                Toggle(stringResource(R.string.face_down), stringResource(R.string.face_down_sub), s.faceDown) { v -> container.write { settings.update { it.copy(faceDown = v) } } }
            }
            Spacer(Modifier.height(20.dp))
        }
        Column(Modifier.padding(16.dp).navigationBarsPadding()) {
            PrimaryButton(stringResource(R.string.start_telling), {
                val names = tellers.split(',', '\n').map { it.trim() }.filter { it.isNotEmpty() }
                scope.launch {
                    container.settings.update { it.copy(tellers = names) }
                    Evenings.start(container, s.record && micOk)
                }
            }, Modifier.fillMaxWidth())
        }
    }
}

private fun e(home: Home) = home.settings.evening

@Composable
private fun Telling(home: Home, go: Go) {
    val c = ctx()
    val container = c.container
    val s = home.settings
    val ev = s.evening
    val scope = rememberCoroutineScope()
    val now = rememberNow(1000)
    val elapsed = (now - ev.startedAt).coerceAtLeast(0)
    var down by remember { mutableStateOf(false) }
    var downSince by remember { mutableLongStateOf(0L) }
    var fresh by remember { mutableStateOf<Slide?>(null) }
    var big by remember { mutableStateOf<Slide?>(null) }
    val vib = remember { c.getSystemService(Vibrator::class.java) }
    fun buzz(ms: Long = 60) { if (s.haptics) runCatching { vib?.vibrate(VibrationEffect.createOneShot(ms, 80)) } }

    // The screen stays on but goes as dark as it can while the tale is told.
    val view = LocalView.current
    val activity = c as? Activity
    DisposableEffect(Unit) {
        view.keepScreenOn = true
        onDispose {
            view.keepScreenOn = false
            activity?.window?.let { w -> w.attributes = w.attributes.apply { screenBrightness = -1f } }
        }
    }
    LaunchedEffect(down, s.dim) {
        activity?.window?.let { w -> w.attributes = w.attributes.apply { screenBrightness = if (down) 0.01f else if (s.dim) 0.12f else -1f } }
    }

    FaceDownEffect(s.faceDown) { isDown ->
        down = isDown
        if (isDown) downSince = System.currentTimeMillis()
        else if (System.currentTimeMillis() - downSince > 15_000) {
            // Turned back over after a good while of telling: the lantern offers a twist.
            scope.launch { fresh = Evenings.twist(container); buzz(40) }
        }
    }

    // The candle.
    val candleMs = s.candle * 60_000L
    val burnt = candleMs > 0 && elapsed >= candleMs
    var rung by remember { mutableStateOf(burnt) }
    LaunchedEffect(burnt) { if (burnt && !rung) { rung = true; Chime.candle(); buzz(220) } }

    // Turns round the table.
    val tellers = s.tellers.ifEmpty { listOf(stringResource(R.string.teller_default)) }
    val turnMs = s.turn * 1000L
    val turn = if (s.relay && tellers.size > 1) ((elapsed / turnMs) % tellers.size).toInt() else -1
    var lastTurn by remember { mutableIntStateOf(turn) }
    LaunchedEffect(turn) { if (turn != lastTurn) { lastTurn = turn; if (turn >= 0) { Chime.turn(); buzz(90) } } }

    BackHandler { go.home() }

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        Column(Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = go::home) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, stringResource(R.string.back), tint = U.c.faint) }
                Column(Modifier.weight(1f)) {
                    Text(home.taleName(ev.slides), style = UType.Name.copy(fontSize = 17.sp, color = U.c.muted), maxLines = 1)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (ev.recording.isNotEmpty()) {
                            Box(Modifier.size(8.dp).clip(CircleShape).background(U.c.danger))
                            Spacer(Modifier.width(6.dp))
                            Text(stringResource(R.string.recording), style = UType.Small)
                            Spacer(Modifier.width(10.dp))
                        }
                        Text(T.clock(elapsed), style = UType.Small)
                    }
                }
            }
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
                // All of tonight's glass, small; a tap shows one big.
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    (ev.slides + ev.twists).forEach { sl ->
                        Box(Modifier.clip(CircleShape).clickable { big = sl }) {
                            Glass(home.figure(sl), sl.kind.glass(), 52.dp, light = 0.75f, halo = false, mirror = sl.mirror(), flicker = false)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                if (candleMs > 0) Candle(elapsed, candleMs)
                if (turn >= 0) {
                    Spacer(Modifier.height(14.dp))
                    Relay(tellers, turn, elapsed % turnMs, turnMs)
                }
                Spacer(Modifier.height(18.dp))
                AnimatedVisibility(fresh != null, enter = fadeIn(tween(900)) + scaleIn(tween(900), 0.85f), exit = fadeOut()) {
                    fresh?.let { t -> TwistCard(t, home, onTake = { fresh = null }, onBack = { scope.launch { Evenings.untwist(container); fresh = null } }) }
                }
                if (burnt) {
                    UCard {
                        Text(stringResource(R.string.candle_out), style = UType.Name, color = U.c.lamp)
                        Text(stringResource(R.string.candle_out_sub), style = UType.Muted)
                    }
                    Spacer(Modifier.height(12.dp))
                }
                if (fresh == null) {
                    Text(
                        stringResource(if (s.faceDown) R.string.telling_face_down else R.string.telling_plain),
                        style = UType.Tale.copy(color = U.c.muted, fontSize = 18.sp), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
                    )
                }
                Spacer(Modifier.height(14.dp))
                Prompt(ev.step, home) { scope.launch { Evenings.step(container, it) } }
                Spacer(Modifier.height(20.dp))
            }
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SoftButton(stringResource(R.string.twist), { scope.launch { fresh = Evenings.twist(container) } }, Modifier.weight(1f), icon = Icons.Rounded.AutoAwesome, color = app.lantern.domain.Kind.TWIST.glass(), soft = U.c.surface)
                PrimaryButton(stringResource(R.string.to_ending), {
                    scope.launch { Evenings.end(container); go.finale() }
                }, Modifier.weight(1f), icon = Icons.Rounded.Bedtime, color = if (burnt) U.c.lamp else U.c.brassDark, content = if (burnt) U.c.cut else U.c.ink)
            }
        }
        // Face down: nothing on the screen at all.
        if (down) Box(Modifier.fillMaxSize().background(Color.Black).clickable(remember { MutableInteractionSource() }, null) { })
        big?.let { sl ->
            Box(
                Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.92f)).clickable(remember { MutableInteractionSource() }, null) { big = null },
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                    Glass(home.figure(sl), sl.kind.glass(), 320.dp, mirror = sl.mirror())
                    Text(T.kind(c, sl.kind).uppercase(), style = UType.Eyebrow, color = sl.kind.glass())
                    Spacer(Modifier.height(4.dp))
                    Text(home.title(sl), style = UType.Display, textAlign = TextAlign.Center)
                    home.sub(sl).takeIf { it.isNotEmpty() }?.let { Text(it, style = UType.Line.copy(fontSize = 19.sp), textAlign = TextAlign.Center) }
                }
            }
        }
    }
}

/** A candle burning down: how much of the tale's time is left. */
@Composable
private fun Candle(elapsed: Long, total: Long) {
    val left = (1f - elapsed.toFloat() / total).coerceIn(0f, 1f)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(Modifier.size(width = 34.dp, height = 70.dp)) {
            val w = size.width * 0.42f
            val full = size.height * 0.78f
            val h = (full * left).coerceAtLeast(4.dp.toPx())
            val x = (size.width - w) / 2
            val top = size.height - h
            drawRoundRect(Color(0xFFEAD9B5), Offset(x, top), Size(w, h), CornerRadius(3.dp.toPx()))
            drawRoundRect(Color(0xFF6E5428), Offset(x - 5.dp.toPx(), size.height - 4.dp.toPx()), Size(w + 10.dp.toPx(), 4.dp.toPx()), CornerRadius(2.dp.toPx()))
            if (left > 0f) {
                val f = Offset(size.width / 2, top - 9.dp.toPx())
                drawCircle(Brush.radialGradient(listOf(Color(0x99F2B04F), Color.Transparent), f, 18.dp.toPx()), 18.dp.toPx(), f)
                drawOval(Color(0xFFF6C46A), Offset(f.x - 4.dp.toPx(), f.y - 7.dp.toPx()), Size(8.dp.toPx(), 14.dp.toPx()))
                drawOval(Color(0xFFFFF0C8), Offset(f.x - 2.dp.toPx(), f.y - 2.dp.toPx()), Size(4.dp.toPx(), 7.dp.toPx()))
            }
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(stringResource(R.string.candle), style = UType.Small)
            Text(if (left > 0f) T.clock(total - elapsed) else stringResource(R.string.candle_gone), style = UType.Number.copy(fontSize = 22.sp, color = U.c.muted))
        }
    }
}

/** Whose turn it is, and the sand running out. */
@Composable
private fun Relay(tellers: List<String>, turn: Int, intoTurn: Long, turnMs: Long) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(Modifier.size(44.dp)) {
            val f = 1f - intoTurn.toFloat() / turnMs
            drawCircle(Color(0xFF30272F), size.minDimension / 2 - 3.dp.toPx(), style = Stroke(5.dp.toPx()))
            drawArc(Color(0xFF52AE80), -90f, 360f * f, false, Offset(3.dp.toPx(), 3.dp.toPx()), Size(size.width - 6.dp.toPx(), size.height - 6.dp.toPx()), style = Stroke(5.dp.toPx()))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(stringResource(R.string.turn_now), style = UType.Small)
            Text(tellers[turn], style = UType.Display.copy(fontSize = 26.sp))
        }
        Text(stringResource(R.string.turn_next, tellers[(turn + 1) % tellers.size]), style = UType.Muted)
    }
}

@Composable
private fun TwistCard(t: Slide, home: Home, onTake: () -> Unit, onBack: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().clip(U.RLg).background(U.c.surface).border(1.dp, t.kind.glass().copy(alpha = 0.4f), U.RLg).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(R.string.twist_came).uppercase(), style = UType.Eyebrow, color = t.kind.glass())
        Spacer(Modifier.height(8.dp))
        Glass(home.figure(t), t.kind.glass(), 220.dp)
        Text(home.title(t), style = UType.Title.copy(fontSize = 23.sp, lineHeight = 29.sp), textAlign = TextAlign.Center)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = onBack) { Text(stringResource(R.string.twist_back), style = UType.Button, color = U.c.muted) }
            TextButton(onClick = onTake) { Text(stringResource(R.string.twist_take), style = UType.Button, color = U.c.lamp) }
        }
    }
}

/**
 * Propp's steps, for the grown-up only: hidden until held, so the child doesn't read ahead
 * (and so it isn't always lit up in the dark).
 */
@Composable
private fun Prompt(step: Int, home: Home, onStep: (Int) -> Unit) {
    val st = Deck.steps[step.coerceIn(0, Deck.steps.lastIndex)]
    var held by remember { mutableStateOf(false) }
    Column(
        Modifier.fillMaxWidth().clip(U.RMd).background(U.c.surface).border(1.dp, U.c.line, U.RMd)
            .pointerInput(Unit) { detectTapGestures(onPress = { held = true; tryAwaitRelease(); held = false }) }
            .padding(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Rounded.Lightbulb, null, tint = U.c.brass, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.prompt_title).uppercase(), style = UType.Eyebrow, modifier = Modifier.weight(1f))
            Text("${step + 1} / ${Deck.steps.size}", style = UType.Small)
        }
        Spacer(Modifier.height(8.dp))
        if (held) {
            Text(st.title.of(home.ru), style = UType.Name, color = U.c.lamp)
            Spacer(Modifier.height(4.dp))
            Text(st.hint.of(home.ru), style = UType.Body)
            Spacer(Modifier.height(6.dp))
            Text(stringResource(R.string.prompt_ask, st.ask.of(home.ru)), style = UType.Line)
        } else {
            Text(stringResource(R.string.prompt_hold), style = UType.Muted)
        }
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onStep((step - 1).coerceAtLeast(0)) }, enabled = step > 0) { Icon(Icons.AutoMirrored.Rounded.KeyboardArrowLeft, stringResource(R.string.back), tint = U.c.muted) }
            Spacer(Modifier.weight(1f))
            TextButton(onClick = { onStep((step + 1).coerceAtMost(Deck.steps.lastIndex)) }, enabled = step < Deck.steps.lastIndex) {
                Text(stringResource(R.string.prompt_next), style = UType.Button, color = U.c.brass)
                Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, null, tint = U.c.brass)
            }
        }
    }
}
