package app.lantern.book

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import app.lantern.R
import app.lantern.data.Tale
import app.lantern.domain.Slide
import app.lantern.ui.Home
import app.lantern.ui.T
import app.lantern.ui.art.Figure
import app.lantern.ui.components.glass
import app.lantern.ui.components.mirror
import app.lantern.ui.theme.glass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * The shelf as a little book to print: one page per tale, its glass in a row, the title, the date,
 * and whatever was written down. Pale paper, so it prints without a pot of black ink.
 */
object Book {
    private const val W = 420
    private const val H = 595

    /** One glass, drawn to a bitmap the PDF can hold. */
    fun bitmap(fig: Figure?, s: Slide, px: Int): Bitmap {
        val image = ImageBitmap(px, px)
        CanvasDrawScope().draw(Density(1f), LayoutDirection.Ltr, Canvas(image), Size(px.toFloat(), px.toFloat())) {
            glass(fig, s.kind.glass(), Offset(px / 2f, px / 2f), px / 2f * 0.92f, mirror = s.mirror(), seed = s.hashCode())
        }
        return image.asAndroidBitmap()
    }

    suspend fun make(context: Context, tales: List<Tale>, home: Home): File = withContext(Dispatchers.Default) {
        val serif = ResourcesCompat.getFont(context, R.font.alegreya) ?: Typeface.SERIF
        val italic = ResourcesCompat.getFont(context, R.font.alegreya_italic) ?: Typeface.SERIF
        val pdf = PdfDocument()
        val paper = Paint().apply { color = 0xFFF7F0E1.toInt() }
        val ink = 0xFF2A1F17.toInt()
        val faint = 0xFF8A7A66.toInt()
        tales.forEachIndexed { i, t ->
            val page = pdf.startPage(PdfDocument.PageInfo.Builder(W, H, i + 1).create())
            val cv = page.canvas
            cv.drawRect(0f, 0f, W.toFloat(), H.toFloat(), paper)
            // The glass along the top, the biggest first.
            val glass = t.glass + listOfNotNull(t.end)
            val size = if (glass.size <= 4) 92 else 72
            val gap = 6
            var x = (W - (glass.size * size + (glass.size - 1) * gap)) / 2f
            glass.forEach { s ->
                val bmp = bitmap(home.figure(s), s, size * 3)
                cv.drawBitmap(bmp, null, android.graphics.RectF(x, 40f, x + size, 40f + size), Paint(Paint.FILTER_BITMAP_FLAG))
                bmp.recycle()
                x += size + gap
            }
            var y = 40f + size + 34f
            fun block(text: String, tf: Typeface, sp: Float, color: Int, after: Float) {
                if (text.isBlank()) return
                val p = TextPaint(Paint.ANTI_ALIAS_FLAG).apply { typeface = tf; textSize = sp; this.color = color }
                val l = StaticLayout.Builder.obtain(text, 0, text.length, p, W - 80).setAlignment(Layout.Alignment.ALIGN_CENTER).setLineSpacing(0f, 1.15f).build()
                cv.save(); cv.translate(40f, y); l.draw(cv); cv.restore()
                y += l.height + after
            }
            block(t.title, serif, 22f, ink, 8f)
            block(T.date(context, t.endedAt), italic, 11f, faint, 18f)
            glass.forEach { s ->
                val line = home.title(s) + home.sub(s).let { if (it.isEmpty()) "" else " — $it" }
                block(line, italic, 11.5f, faint, 3f)
            }
            y += 14f
            block(t.note, serif, 13.5f, ink, 0f)
            val foot = TextPaint(Paint.ANTI_ALIAS_FLAG).apply { typeface = italic; textSize = 9f; color = faint; textAlign = Paint.Align.CENTER }
            cv.drawText(context.getString(R.string.book_footer), W / 2f, H - 24f, foot)
            pdf.finishPage(page)
        }
        val dir = File(context.cacheDir, "books").apply { mkdirs() }
        val file = File(dir, context.getString(R.string.book_file) + ".pdf")
        file.outputStream().use { pdf.writeTo(it) }
        pdf.close()
        file
    }

    /** Makes the book and opens the share sheet: print it, send it to grandma. */
    suspend fun share(context: Context, tales: List<Tale>, home: Home): Boolean = runCatching {
        val file = make(context, tales, home)
        val uri = FileProvider.getUriForFile(context, context.packageName + ".files", file)
        val send = Intent(Intent.ACTION_SEND).setType("application/pdf").putExtra(Intent.EXTRA_STREAM, uri).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(send, context.getString(R.string.book)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }.isSuccess
}
