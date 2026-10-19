package app.lantern.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.WbIncandescent
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.lantern.R
import app.lantern.container
import app.lantern.data.Drawing
import app.lantern.data.Settings
import app.lantern.data.Tale
import app.lantern.domain.Box as GlassBox
import app.lantern.domain.Morning
import app.lantern.domain.Names
import app.lantern.domain.Own
import app.lantern.domain.Parcels
import app.lantern.domain.Slide
import app.lantern.domain.World
import app.lantern.ru
import app.lantern.ui.art.Art
import app.lantern.ui.art.Figure
import app.lantern.ui.box.BoxTab
import app.lantern.ui.box.DrawScreen
import app.lantern.ui.components.rememberNow
import app.lantern.ui.lantern.LanternTab
import app.lantern.ui.onboarding.WelcomeScreen
import app.lantern.ui.settings.SettingsScreen
import app.lantern.ui.shelf.ShelfTab
import app.lantern.ui.shelf.TaleScreen
import app.lantern.ui.story.FinaleScreen
import app.lantern.ui.story.StoryScreen
import app.lantern.ui.theme.Sans
import app.lantern.ui.theme.U
import app.lantern.ui.world.WorldTab
import java.time.LocalDate
import java.time.ZoneId

enum class Tab { LANTERN, SHELF, WORLD, BOX }

/** Everything stored, plus what is derived from it. Screens read slides through here. */
@Immutable
class Home(val tales: List<Tale>, val drawings: List<Drawing>, val settings: Settings, val now: Long, val ru: Boolean) {
    val zone: ZoneId = ZoneId.systemDefault()
    val today: LocalDate = LocalDate.now(zone)

    fun tale(id: Long) = tales.firstOrNull { it.id == id }
    fun drawing(id: Long) = drawings.firstOrNull { it.id == id }

    fun ownName(id: Long): String = drawing(id)?.name.orEmpty()
    fun ownFigure(id: Long): Figure? = drawing(id)?.let { Art.painted(it.id, it.strokes) }

    fun title(s: Slide): String = Names.title(s, ru, ::ownName)
    fun sub(s: Slide): String = Names.sub(s, ru)
    fun figure(s: Slide): Figure? = Art.of(s, ::ownFigure)
    fun taleName(slides: List<Slide>): String = Names.tale(slides, ru, ::ownName)

    val friends: List<World.Friend> by lazy { World.friends(tales) }
    val visited: List<Pair<String, Int>> by lazy { World.visited(tales) }
    val box: GlassBox get() = GlassBox(settings.opened, today, drawings.map { Own(it.id, it.kindOf) })
    val parcels: Int get() = Parcels.waiting(tales, settings.opened)
    val morning: Tale? by lazy { Morning.tale(tales, now, zone) }
    fun isFriend(s: Slide) = World.isFriend(s, tales)
}

/** Everything a screen can open. */
class Go(private val nav: NavHostController, val tab: (Tab) -> Unit) {
    fun story() = nav.navigate("story") { popUpTo("main") { inclusive = false }; launchSingleTop = true }
    fun finale() = nav.navigate("finale") { popUpTo("main") { inclusive = false }; launchSingleTop = true }
    fun tale(id: Long) = nav.navigate("tale/$id") { launchSingleTop = true }
    fun draw(id: Long = 0) = nav.navigate("draw/$id") { launchSingleTop = true }
    fun settings() = nav.navigate("settings") { launchSingleTop = true }
    fun home() { nav.popBackStack("main", false) }
    fun lantern() { home(); tab(Tab.LANTERN) }
    fun back() { nav.popBackStack() }
}

@Composable
fun LanternRoot(settings: Settings) {
    val context = LocalContext.current
    val container = context.container
    val nav = rememberNavController()
    var tab by rememberSaveable { mutableStateOf(Tab.LANTERN) }
    val start = remember { if (settings.onboarded) "main" else "welcome" }
    val dao = container.db.dao()

    val tales by dao.observeTales().collectAsStateWithLifecycle(initialValue = null)
    val drawings by dao.observeDrawings().collectAsStateWithLifecycle(initialValue = null)
    val tick = rememberNow(60_000)
    val ru = context.ru
    val home = remember(tales, drawings, settings, tick, ru) { Home(tales.orEmpty(), drawings.orEmpty(), settings, System.currentTimeMillis(), ru) }
    val go = remember(nav) { Go(nav) { tab = it } }

    // Opened in the middle of a tale (the phone killed us in the dark): straight back to it.
    LaunchedEffect(Unit) {
        val e = settings.evening
        if (settings.onboarded && e.telling) if (e.ending != null) go.finale() else go.story()
    }
    // Onboarded elsewhere (a restored backup, the demo seed): leave the welcome screens.
    val route = nav.currentBackStackEntryAsState().value?.destination?.route
    LaunchedEffect(settings.onboarded, route) {
        if (settings.onboarded && route == "welcome") nav.navigate("main") { popUpTo("welcome") { inclusive = true } }
    }
    val open by container.open.collectAsStateWithLifecycle()
    LaunchedEffect(open, tales != null, settings.onboarded) {
        val (where, _) = open ?: return@LaunchedEffect
        if (tales == null || !settings.onboarded) return@LaunchedEffect
        container.open.value = null
        when (where) {
            "morning", "lantern" -> go.lantern()
        }
    }

    Box(Modifier.fillMaxSize().background(U.c.bg)) {
        NavHost(
            navController = nav,
            startDestination = start,
            modifier = Modifier.background(U.c.bg),
            enterTransition = { fadeIn(tween(260)) },
            exitTransition = { fadeOut(tween(180)) },
            popEnterTransition = { fadeIn(tween(260)) },
            popExitTransition = { fadeOut(tween(180)) },
        ) {
            composable("welcome") {
                WelcomeScreen { hand, tellers ->
                    container.write { this.settings.update { it.copy(onboarded = true, hand = hand, tellers = tellers) } }
                    nav.navigate("main") { popUpTo("welcome") { inclusive = true } }
                }
            }
            composable("main") { MainTabs(tab, { tab = it }, go, home) }
            composable("story") { StoryScreen(home, go) }
            composable("finale") { FinaleScreen(home, go) }
            composable("tale/{id}", arguments = listOf(navArgument("id") { type = NavType.LongType })) { e ->
                TaleScreen(e.arguments?.getLong("id") ?: 0L, home, go)
            }
            composable("draw/{id}", arguments = listOf(navArgument("id") { type = NavType.LongType })) { e ->
                DrawScreen(e.arguments?.getLong("id") ?: 0L, home, go)
            }
            composable("settings") { SettingsScreen(home, go) }
        }
    }
}

@Composable
private fun MainTabs(tab: Tab, onTab: (Tab) -> Unit, go: Go, home: Home) {
    Column(Modifier.fillMaxSize().background(U.c.bg)) {
        Box(Modifier.weight(1f)) {
            when (tab) {
                Tab.LANTERN -> LanternTab(home, go)
                Tab.SHELF -> ShelfTab(home, go)
                Tab.WORLD -> WorldTab(home, go)
                Tab.BOX -> BoxTab(home, go)
            }
        }
        HorizontalDivider(color = U.c.line)
        Row(
            Modifier.fillMaxWidth().background(U.c.surface).navigationBarsPadding().height(66.dp).padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NavItem(Icons.Rounded.WbIncandescent, stringResource(R.string.tab_lantern), tab == Tab.LANTERN) { onTab(Tab.LANTERN) }
            NavItem(Icons.Rounded.AutoStories, stringResource(R.string.tab_shelf), tab == Tab.SHELF) { onTab(Tab.SHELF) }
            NavItem(Icons.Rounded.Map, stringResource(R.string.tab_world), tab == Tab.WORLD) { onTab(Tab.WORLD) }
            NavItem(Icons.Rounded.Inventory2, stringResource(R.string.tab_box), tab == Tab.BOX, badge = home.parcels > 0) { onTab(Tab.BOX) }
        }
    }
}

@Composable
private fun RowScope.NavItem(icon: ImageVector, label: String, selected: Boolean, badge: Boolean = false, onClick: () -> Unit) {
    val color = if (selected) U.c.lamp else U.c.faint
    Column(
        Modifier
            .weight(1f)
            .clip(RoundedCornerShape(14.dp))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick)
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier.clip(RoundedCornerShape(50)).background(if (selected) U.c.lampSoft else Color.Transparent).padding(horizontal = 18.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, null, Modifier.size(22.dp), tint = color)
            if (badge) Box(Modifier.align(Alignment.TopEnd).padding(start = 18.dp).size(8.dp).clip(CircleShape).background(U.c.lamp))
        }
        Spacer(Modifier.height(3.dp))
        Text(label, fontFamily = Sans, fontSize = 12.5.sp, color = color, maxLines = 1)
    }
}
