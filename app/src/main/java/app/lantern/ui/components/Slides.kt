package app.lantern.ui.components

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.lantern.R
import app.lantern.domain.Kind
import app.lantern.domain.Slide
import app.lantern.ui.Home
import app.lantern.ui.T
import app.lantern.ui.theme.U
import app.lantern.ui.theme.UType
import app.lantern.ui.theme.glass
import kotlin.math.sqrt

/** A helper looks at the hero: drawn facing left. */
fun Slide.mirror(): Boolean = kind == Kind.HELPER

/** A slide in a row: the glass, what kind it is, its name and its small print. */
@Composable
fun SlideRow(
    s: Slide,
    home: Home,
    modifier: Modifier = Modifier,
    size: Dp = 104.dp,
    light: Float = 1f,
    friend: Boolean = false,
    canSwap: Boolean = false,
    onSwap: (() -> Unit)? = null,
    seed: Int = 0,
) {
    val c = LocalContext.current
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Glass(home.figure(s), s.kind.glass(), size, light = light, mirror = s.mirror(), seed = seed)
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(T.kind(c, s.kind).uppercase(), style = UType.Eyebrow, color = s.kind.glass().copy(alpha = 0.9f))
                if (friend) {
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.old_friend), style = UType.Small.copy(fontSize = 11.5.sp), color = U.c.lamp,
                        modifier = Modifier.clip(U.RSm).background(U.c.lampSoft).padding(horizontal = 6.dp, vertical = 1.dp))
                }
            }
            Spacer(Modifier.height(3.dp))
            // A break allowed after the hyphen, so «Улитка-путешественница» wraps there and not mid-word.
            Text(home.title(s).replace("-", "-​"), style = if (s.kind in setOf(Kind.TROUBLE, Kind.TWIST, Kind.ENDING, Kind.QUIRK)) UType.Name.copy(fontSize = 18.sp) else UType.Title)
            val sub = home.sub(s)
            if (sub.isNotEmpty()) {
                Spacer(Modifier.height(2.dp))
                Text(sub, style = UType.Line)
            }
        }
        if (onSwap != null) {
            Box(
                Modifier.size(44.dp).clip(CircleShape).then(if (canSwap) Modifier.border(1.dp, U.c.line, CircleShape).clickable(onClick = onSwap) else Modifier),
                contentAlignment = Alignment.Center,
            ) {
                if (canSwap) Icon(Icons.Rounded.Autorenew, stringResource(R.string.swap), tint = U.c.muted, modifier = Modifier.size(22.dp))
            }
        }
    }
}

/** Small round glasses side by side, overlapping a little: a tale at a glance. */
@Composable
fun GlassStrip(slides: List<Slide>, home: Home, size: Dp = 46.dp, overlap: Dp = 12.dp, modifier: Modifier = Modifier) {
    Box(modifier.height(size).width(size + (size - overlap) * (slides.size - 1).coerceAtLeast(0))) {
        slides.forEachIndexed { i, s ->
            Glass(home.figure(s), s.kind.glass(), size, Modifier.offset(x = (size - overlap) * i), halo = false, mirror = s.mirror(), flicker = false, seed = i)
        }
    }
}

/** Calls [onShake] when the phone is given a good shake: that's how the child lights the lantern. */
@Composable
fun ShakeEffect(enabled: Boolean, onShake: () -> Unit) {
    val context = LocalContext.current
    val current = rememberUpdatedState(onShake)
    DisposableEffect(enabled) {
        if (!enabled) return@DisposableEffect onDispose { }
        val sm = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val acc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) ?: return@DisposableEffect onDispose { }
        var last = 0L
        var hits = 0
        var firstHit = 0L
        val l = object : SensorEventListener {
            override fun onSensorChanged(e: SensorEvent) {
                val g = sqrt(e.values[0] * e.values[0] + e.values[1] * e.values[1] + e.values[2] * e.values[2]) / SensorManager.GRAVITY_EARTH
                val now = System.currentTimeMillis()
                if (g > 2.4f) {
                    if (now - firstHit > 900) { hits = 0; firstHit = now }
                    hits++
                    if (hits >= 3 && now - last > 2000) { last = now; hits = 0; current.value() }
                }
            }

            override fun onAccuracyChanged(s: Sensor?, a: Int) {}
        }
        sm.registerListener(l, acc, SensorManager.SENSOR_DELAY_GAME)
        onDispose { sm.unregisterListener(l) }
    }
}

/**
 * Whether the phone lies face down: the screen goes dark and the tale is told without it.
 * [onChange] gets true going down, false coming back up.
 */
@Composable
fun FaceDownEffect(enabled: Boolean, onChange: (Boolean) -> Unit) {
    val context = LocalContext.current
    val current = rememberUpdatedState(onChange)
    DisposableEffect(enabled) {
        if (!enabled) return@DisposableEffect onDispose { }
        val sm = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val acc = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) ?: return@DisposableEffect onDispose { }
        var down = false
        var since = 0L
        val l = object : SensorEventListener {
            override fun onSensorChanged(e: SensorEvent) {
                val z = e.values[2]
                val now = System.currentTimeMillis()
                // A second of steady reading before it counts, so a wobble in the hand doesn't.
                val wants = if (down) z < -3.5f else z < -8.0f
                if (wants != down) {
                    if (since == 0L) since = now
                    if (now - since > 900) { down = wants; since = 0L; current.value(down) }
                } else since = 0L
            }

            override fun onAccuracyChanged(s: Sensor?, a: Int) {}
        }
        sm.registerListener(l, acc, SensorManager.SENSOR_DELAY_NORMAL)
        onDispose { sm.unregisterListener(l) }
    }
}
