package app.lantern.sound

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/** A small bell: the candle has burnt down, or it's the next teller's turn. Soft enough for a dark room. */
object Chime {
    private const val RATE = 22050

    fun ring(notes: List<Double> = listOf(1318.5, 987.8), volume: Float = 0.35f) {
        Thread {
            val step = (RATE * 0.32).toInt()
            val len = step * notes.size + RATE
            val pcm = ShortArray(len)
            notes.forEachIndexed { k, f ->
                for (i in 0 until len - k * step) {
                    val t = i.toDouble() / RATE
                    // A bell: the note, a fifth-ish partial, fading out.
                    val v = (sin(2 * PI * f * t) + 0.35 * sin(2 * PI * f * 2.76 * t) * exp(-t * 6)) * exp(-t * 2.6) * volume
                    val j = k * step + i
                    pcm[j] = (pcm[j] + v * Short.MAX_VALUE * 0.5).toInt().coerceIn(-32767, 32767).toShort()
                }
            }
            val track = AudioTrack.Builder()
                .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build())
                .setAudioFormat(AudioFormat.Builder().setSampleRate(RATE).setEncoding(AudioFormat.ENCODING_PCM_16BIT).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
                .setBufferSizeInBytes(pcm.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()
            runCatching {
                track.write(pcm, 0, pcm.size)
                track.play()
                Thread.sleep((len * 1000L / RATE) + 200)
            }
            track.release()
        }.start()
    }

    /** The candle has gone out: three falling notes. */
    fun candle() = ring(listOf(1174.7, 987.8, 784.0), 0.3f)

    /** Pass the tale on. */
    fun turn() = ring(listOf(1568.0, 1318.5), 0.28f)
}

/** The tale as it was told, kept next to it on the shelf. Only on this phone. */
class Recorder(private val context: Context) {
    private var recorder: MediaRecorder? = null

    fun folder(): File = File(context.filesDir, "tales").apply { mkdirs() }

    fun file(name: String) = File(folder(), name)

    /** Starts recording to a new file and returns its name, or null if the microphone wouldn't. */
    fun start(): String? {
        stop()
        val name = "tale-${System.currentTimeMillis()}.m4a"
        val r = if (Build.VERSION.SDK_INT >= 31) MediaRecorder(context) else @Suppress("DEPRECATION") MediaRecorder()
        return runCatching {
            r.setAudioSource(MediaRecorder.AudioSource.MIC)
            r.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            r.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            r.setAudioEncodingBitRate(64_000)
            r.setAudioSamplingRate(22_050)
            r.setOutputFile(file(name).absolutePath)
            r.prepare()
            r.start()
            recorder = r
            name
        }.getOrElse { r.release(); null }
    }

    fun stop() {
        val r = recorder ?: return
        recorder = null
        runCatching { r.stop() }
        r.release()
    }

    val active: Boolean get() = recorder != null
}

/** Plays a recorded tale back. */
class Player {
    private var player: MediaPlayer? = null

    fun play(file: File, done: () -> Unit): Boolean {
        stop()
        return runCatching {
            player = MediaPlayer().apply {
                setDataSource(file.absolutePath)
                setOnCompletionListener { stop(); done() }
                prepare()
                start()
            }
        }.isSuccess
    }

    fun stop() {
        player?.let { runCatching { it.stop() }; it.release() }
        player = null
    }

    val position: Int get() = runCatching { player?.currentPosition ?: 0 }.getOrDefault(0)
    val duration: Int get() = runCatching { player?.duration ?: 0 }.getOrDefault(0)
}
