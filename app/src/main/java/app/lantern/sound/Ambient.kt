package app.lantern.sound

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import app.lantern.data.Lullaby
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

/**
 * Night sounds made on the phone as they play: rain, the sea, crickets, a fire, the wind. No
 * recordings, nothing to download. It fades out over the last minute and stops by itself.
 */
object Ambient {
    private const val RATE = 22050
    private var job: Job? = null
    private val _playing = MutableStateFlow<Lullaby?>(null)
    val playing: StateFlow<Lullaby?> = _playing

    /** 0..1 of the way through, for the lantern to burn down with it. */
    val progress = MutableStateFlow(0f)

    fun play(scope: CoroutineScope, kind: Lullaby, minutes: Int) {
        stop()
        if (kind == Lullaby.NONE) return
        _playing.value = kind
        progress.value = 0f
        job = scope.launch(Dispatchers.Default) {
            val buf = AudioTrack.getMinBufferSize(RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT).coerceAtLeast(RATE / 5)
            val track = AudioTrack.Builder()
                .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
                .setAudioFormat(AudioFormat.Builder().setSampleRate(RATE).setEncoding(AudioFormat.ENCODING_PCM_16BIT).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
                .setBufferSizeInBytes(buf * 2)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()
            val total = minutes * 60L * RATE
            val fadeIn = RATE * 4L
            val fadeOut = minOf(RATE * 60L, total / 3)
            val gen = Generator(kind)
            val chunk = ShortArray(RATE / 10)
            var n = 0L
            try {
                track.play()
                while (isActive && n < total) {
                    for (i in chunk.indices) {
                        val t = n + i
                        val env = minOf(1.0, t.toDouble() / fadeIn, (total - t).toDouble() / fadeOut).coerceIn(0.0, 1.0)
                        chunk[i] = (gen.next() * env * 0.55 * Short.MAX_VALUE).toInt().coerceIn(-32767, 32767).toShort()
                    }
                    track.write(chunk, 0, chunk.size)
                    n += chunk.size
                    progress.value = n.toFloat() / total
                }
            } finally {
                runCatching { track.pause(); track.flush(); track.release() }
                _playing.value = null
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
        _playing.value = null
    }

    /** One sample at a time, roughly -1..1. Each kind is a little recipe of noise and filters. */
    private class Generator(val kind: Lullaby) {
        private val r = Random(System.nanoTime())
        private var brown = 0.0
        private var lp1 = 0.0
        private var lp2 = 0.0
        private var hp = 0.0
        private var lastIn = 0.0
        private var t = 0L
        private var drop = 0.0
        private var dropPhase = 0.0
        private var dropFreq = 0.0
        private var chirp = 0
        private var chirpGap = 0
        private var chirpPhase = 0.0
        private var crackle = 0.0
        private var gust = 0.5
        private var gustTarget = 0.5

        private fun white() = r.nextDouble() * 2 - 1
        private fun brownNoise(): Double {
            brown = (brown + white() * 0.02).coerceIn(-1.0, 1.0) * 0.998
            return brown
        }

        fun next(): Double {
            t++
            val s = t.toDouble() / RATE
            return when (kind) {
                Lullaby.RAIN -> {
                    // A soft hiss with drops landing on leaves and the sill.
                    val w = white()
                    lp1 += (w - lp1) * 0.18
                    hp = 0.97 * (hp + lp1 - lastIn); lastIn = lp1
                    if (r.nextDouble() < 0.0016) { drop = 0.5 + r.nextDouble() * 0.5; dropFreq = 900.0 + r.nextDouble() * 2400; dropPhase = 0.0 }
                    drop *= 0.9975
                    dropPhase += 2 * PI * dropFreq / RATE
                    hp * 0.55 + lp1 * 0.25 + sin(dropPhase) * drop * 0.12
                }
                Lullaby.SEA -> {
                    // Brown noise breathing in and out, a wave every eight seconds or so.
                    val swell = 0.25 + 0.75 * (0.5 + 0.5 * sin(2 * PI * s / 8.5)) * (0.75 + 0.25 * sin(2 * PI * s / 23))
                    val b = brownNoise()
                    lp1 += (white() - lp1) * (0.02 + 0.1 * swell)
                    (b * 1.6 + lp1 * 0.9) * swell
                }
                Lullaby.CRICKETS -> {
                    // A low night hum, and a cricket: three quick chirps, a rest, again.
                    lp1 += (white() - lp1) * 0.02
                    var c = 0.0
                    if (chirpGap > 0) chirpGap-- else {
                        chirp++
                        val phaseLen = RATE / 22
                        if (chirp < phaseLen) {
                            chirpPhase += 2 * PI * 4300.0 / RATE
                            val e = sin(PI * chirp / phaseLen)
                            c = sin(chirpPhase) * e * 0.22
                        } else {
                            chirp = 0
                            chirpGap = if (r.nextDouble() < 0.66) RATE / 30 else (RATE * (0.5 + r.nextDouble() * 0.9)).toInt()
                        }
                    }
                    lp1 * 0.5 + c
                }
                Lullaby.FIRE -> {
                    // A low roar with crackles: short sharp clicks, now and then a pop.
                    val b = brownNoise()
                    lp2 += (white() - lp2) * 0.05
                    if (r.nextDouble() < 0.0009) crackle = 0.6 + r.nextDouble() * 0.4
                    crackle *= 0.985
                    b * 1.2 + lp2 * 0.35 + white() * crackle * 0.5
                }
                Lullaby.WIND -> {
                    // Noise through a filter that opens and closes with the gusts.
                    if (t % (RATE / 2) == 0L) gustTarget = 0.15 + r.nextDouble() * 0.85
                    gust += (gustTarget - gust) * 0.00005
                    val k = 0.004 + gust * 0.03
                    lp1 += (white() - lp1) * k
                    lp2 += (lp1 - lp2) * k
                    lp2 * (3.2 + gust * 2.5)
                }
                Lullaby.NONE -> 0.0
            }.let { if (abs(it) > 1) it / abs(it) else it } * (1 - exp(-s * 3))
        }
    }
}
