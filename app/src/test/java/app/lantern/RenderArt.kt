package app.lantern

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import app.lantern.ui.art.Beasts
import app.lantern.ui.art.Figure
import app.lantern.ui.art.Glyphs
import app.lantern.ui.art.Places
import app.lantern.ui.art.Treasures
import app.lantern.ui.art.Wardrobe
import app.lantern.ui.art.figure
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

/** Draws the cut-outs on sheets (run with `-Prender`): a look at the art without a phone. */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class RenderArt {
    private val out = File(System.getProperty("render.out") ?: "build/art").apply { mkdirs() }

    private fun sheet(name: String, items: List<Pair<String, Figure>>, cols: Int = 6, cell: Float = 220f, round: Boolean = false) {
        val rows = (items.size + cols - 1) / cols
        val image = ImageBitmap((cell * cols).toInt(), (cell * rows).toInt())
        CanvasDrawScope().draw(Density(1f), LayoutDirection.Ltr, Canvas(image), Size(cell * cols, cell * rows)) {
            drawRect(Color(0xFF1A1410))
            val paint = android.graphics.Paint().apply { color = 0xFFE8D9B0.toInt(); textSize = 16f; isAntiAlias = true }
            items.forEachIndexed { i, (label, f) ->
                val at = Offset((i % cols) * cell, (i / cols) * cell)
                drawRect(Color(0xFFE9A23B), at + Offset(8f, 8f), Size(cell - 16, cell - 34))
                figure(f, Color(0xFF140D08), at + Offset(14f, 8f), cell - 34)
                if (round) clipRect(at.x, at.y, at.x + cell, at.y + cell - 26f) {
                    val r = (cell - 34) / 2
                    drawCircle(Color(0xFF1A1410), r + 60f, at + Offset(14f + r, 8f + r), style = androidx.compose.ui.graphics.drawscope.Stroke(120f))
                }
                drawContext.canvas.nativeCanvas.drawText(label, at.x + 10f, at.y + cell - 10f, paint)
            }
        }
        File(out, "$name.png").outputStream().use { image.asAndroidBitmap().compress(android.graphics.Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Test
    fun beasts() = sheet("beasts", Beasts.all.map { (k, b) -> k to b.art })

    @Test
    fun crowned() = sheet("crowned", Beasts.all.map { (k, b) -> k to Wardrobe.dressed(b, "crown", "lantern") }, cols = 7)

    @Test
    fun wardrobe() = sheet(
        "wardrobe",
        Wardrobe.hats.keys.map { it to Wardrobe.dressed(Beasts.fox, it, null) } + Wardrobe.things.keys.map { it to Wardrobe.dressed(Beasts.hare, null, it) },
        cols = 8,
    )

    @Test
    fun treasures() = sheet("treasures", Treasures.all.toList(), cols = 8, round = true)

    @Test
    fun places() = sheet("places", Places.all.toList(), cols = 7, round = true)

    @Test
    fun glyphs() = sheet("glyphs", Glyphs.all.toList(), cols = 8, round = true)
}
