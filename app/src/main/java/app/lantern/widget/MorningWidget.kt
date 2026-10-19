package app.lantern.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import app.lantern.MainActivity
import app.lantern.R
import app.lantern.book.Book
import app.lantern.container
import app.lantern.content.Deck
import app.lantern.domain.Kind
import app.lantern.domain.Morning
import app.lantern.domain.Names
import app.lantern.domain.Slide
import app.lantern.localized
import app.lantern.ru
import app.lantern.ui.art.Art
import kotlinx.coroutines.launch

/**
 * On the home screen: in the morning, last night's hero and a question to tell it back; the rest
 * of the day, the lantern waiting for the evening.
 */
class MorningWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) = update(context)

    companion object {
        fun update(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, MorningWidget::class.java))
            if (ids.isEmpty()) return
            val app = context.applicationContext
            app.container.scope.launch {
                val c = app.localized()
                val dao = app.container.db.dao()
                val tales = dao.tales()
                val drawings = dao.drawings()
                val t = Morning.tale(tales, System.currentTimeMillis())
                val hero = t?.of(Kind.HERO) ?: Slide(Kind.HERO, "owl", "sleepyhead")
                val own = { id: Long -> drawings.firstOrNull { it.id == id }?.let { Art.painted(it.id, it.strokes) } }
                val bmp = Book.bitmap(Art.of(hero, own), hero, 280)
                val views = RemoteViews(context.packageName, R.layout.widget_morning)
                views.setImageViewBitmap(R.id.glass, bmp)
                if (t != null) {
                    views.setTextViewText(R.id.eyebrow, c.getString(R.string.morning_title))
                    views.setTextViewText(R.id.title, t.title)
                    views.setTextViewText(R.id.question, Deck.morning[Morning.questions(t).first()].of(c.ru))
                } else {
                    views.setTextViewText(R.id.eyebrow, c.getString(R.string.app_name))
                    views.setTextViewText(R.id.title, Names.title(hero, c.ru) { id -> drawings.firstOrNull { it.id == id }?.name.orEmpty() })
                    views.setTextViewText(R.id.question, c.getString(R.string.widget_evening))
                }
                val open = Intent(context, MainActivity::class.java).putExtra(MainActivity.EXTRA_OPEN, if (t != null) "morning" else "lantern").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                views.setOnClickPendingIntent(R.id.root, PendingIntent.getActivity(context, 1, open, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT))
                manager.updateAppWidget(ids, views)
            }
        }
    }
}
