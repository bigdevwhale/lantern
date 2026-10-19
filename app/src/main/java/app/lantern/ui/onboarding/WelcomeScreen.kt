package app.lantern.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.lantern.R
import app.lantern.domain.Hand
import app.lantern.domain.Kind
import app.lantern.ui.art.Art
import app.lantern.ui.components.Glass
import app.lantern.ui.components.PrimaryButton
import app.lantern.ui.components.UTextField
import app.lantern.ui.lantern.HandPicker
import app.lantern.ui.theme.U
import app.lantern.ui.theme.UType
import app.lantern.ui.theme.glass

/** Three pages of how it goes, then who's listening. */
@Composable
fun WelcomeScreen(onDone: (Hand, List<String>) -> Unit) {
    var page by rememberSaveable { mutableIntStateOf(0) }
    var handState by androidx.compose.runtime.remember { mutableStateOf(Hand()) }
    var tellers by rememberSaveable { mutableStateOf("") }
    val pages = listOf(
        Triple(Art.ref("item:lantern"), Kind.HERO, R.string.welcome_1) to R.string.welcome_1_sub,
        Triple(Art.ref("glyph:door"), Kind.TWIST, R.string.welcome_2) to R.string.welcome_2_sub,
        Triple(Art.ref("glyph:crescent"), Kind.ENDING, R.string.welcome_3) to R.string.welcome_3_sub,
    )
    Column(Modifier.fillMaxSize().background(U.c.bg).statusBarsPadding().imePadding()) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(24.dp))
            AnimatedContent(page, label = "page") { p ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (p < pages.size) {
                        val (top, sub) = pages[p]
                        Glass(top.first, top.second.glass(), 280.dp)
                        Text(stringResource(top.third), style = UType.Display, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(10.dp))
                        Text(stringResource(sub), style = UType.Tale.copy(color = U.c.muted), textAlign = TextAlign.Center)
                    } else {
                        Glass(Art.hero("owl", "storyteller"), Kind.HERO.glass(), 220.dp)
                        Text(stringResource(R.string.welcome_who), style = UType.Display, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(16.dp))
                        HandPicker(handState) { handState = it }
                        Spacer(Modifier.height(16.dp))
                        Text(stringResource(R.string.set_tellers_sub), style = UType.Muted)
                        Spacer(Modifier.height(6.dp))
                        UTextField(tellers, { tellers = it }, placeholder = stringResource(R.string.tellers_hint), words = true)
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }
        Column(Modifier.padding(20.dp).navigationBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally) {
            Row {
                (0..pages.size).forEach { i ->
                    Box(Modifier.padding(4.dp).size(8.dp).clip(CircleShape).background(if (i == page) U.c.lamp else U.c.line))
                }
            }
            Spacer(Modifier.height(14.dp))
            PrimaryButton(stringResource(if (page < pages.size) R.string.next else R.string.welcome_go), {
                if (page < pages.size) page++ else onDone(handState, tellers.split(',', '\n').map { it.trim() }.filter { it.isNotEmpty() })
            }, Modifier.fillMaxWidth())
            Spacer(Modifier.width(1.dp))
        }
    }
}
