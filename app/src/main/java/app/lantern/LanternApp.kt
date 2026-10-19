package app.lantern

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import app.lantern.data.DemoSeed
import app.lantern.data.LanternDatabase
import app.lantern.data.SettingsStore
import app.lantern.sound.Recorder
import app.lantern.widget.MorningWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AppContainer(private val context: Context) {
    val db = LanternDatabase.create(context)
    val settings = SettingsStore(context)
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val recorder = Recorder(context)

    /** Where a tap from outside wants to go ("lantern", "morning") and when it was made. */
    val open = MutableStateFlow<Pair<String, Long>?>(null)

    /** Fire-and-forget so a write survives the screen being closed right after tapping. */
    fun write(block: suspend AppContainer.() -> Unit) {
        scope.launch {
            block()
            changed()
        }
    }

    /** Something on the shelf changed: the morning widget may show a different tale now. */
    fun changed() = MorningWidget.update(context)
}

class LanternApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        container.scope.launch {
            if (BuildConfig.DEMO) DemoSeed.seedIfEmpty(this@LanternApp, container)
            container.changed()
        }
    }
}

val Context.container: AppContainer get() = (applicationContext as LanternApp).container

/** Resources in the in-app language, for the widget, which AppCompat doesn't localise before Android 13. */
fun Context.localized(): Context {
    val tags = AppCompatDelegate.getApplicationLocales()
    if (tags.isEmpty) return this
    val config = Configuration(resources.configuration)
    config.setLocale(tags[0])
    return createConfigurationContext(config)
}

/** Is this context in Russian? The deck has both languages and picks by this. */
val Context.ru: Boolean get() = resources.configuration.locales[0].language == "ru"
