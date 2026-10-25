package app.lantern.ui.settings

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.lantern.R
import app.lantern.container
import app.lantern.data.Backup
import app.lantern.data.Everything
import app.lantern.ui.Go
import app.lantern.ui.Home
import app.lantern.ui.Language
import app.lantern.ui.components.Chip
import app.lantern.ui.components.CommunityLinks
import app.lantern.ui.components.ConfirmDialog
import app.lantern.ui.components.InfoRow
import app.lantern.ui.components.SectionTitle
import app.lantern.ui.components.Toggle
import app.lantern.ui.components.TopBar
import app.lantern.ui.components.UCard
import app.lantern.ui.components.UTextField
import app.lantern.ui.ctx
import app.lantern.ui.lantern.HandPicker
import app.lantern.ui.theme.UType
import app.lantern.ui.theme.U
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(home: Home, go: Go) {
    val c = ctx()
    val container = c.container
    val s = home.settings
    val scope = rememberCoroutineScope()
    var restoring by remember { mutableStateOf<android.net.Uri?>(null) }
    var tellers by remember { mutableStateOf(s.tellers.joinToString(", ")) }
    LaunchedEffect(tellers) {
        delay(500)
        val names = tellers.split(',', '\n').map { it.trim() }.filter { it.isNotEmpty() }
        if (names != s.tellers) container.settings.update { it.copy(tellers = names) }
    }

    val export = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        scope.launch {
            val ok = withContext(Dispatchers.IO) { runCatching { Backup.write(c, uri, Everything(home.tales, home.drawings), s.opened) }.isSuccess }
            Toast.makeText(c, if (ok) R.string.backup_done else R.string.backup_failed, Toast.LENGTH_SHORT).show()
        }
    }
    val import = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri -> restoring = uri }

    Column(Modifier.fillMaxSize().background(U.c.bg).statusBarsPadding().imePadding()) {
        TopBar(stringResource(R.string.settings), go::back)
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp).navigationBarsPadding()) {
            SectionTitle(stringResource(R.string.set_hand))
            UCard { HandPicker(s.hand) { h -> container.write { settings.update { it.copy(hand = h) } } } }

            SectionTitle(stringResource(R.string.set_telling))
            UCard {
                Text(stringResource(R.string.set_tellers), style = UType.Strong)
                Text(stringResource(R.string.set_tellers_sub), style = UType.Muted)
                Spacer(Modifier.height(8.dp))
                UTextField(tellers, { tellers = it }, placeholder = stringResource(R.string.tellers_hint), words = true)
                Spacer(Modifier.height(10.dp))
                Toggle(stringResource(R.string.face_down), stringResource(R.string.face_down_sub), s.faceDown) { v -> container.write { settings.update { it.copy(faceDown = v) } } }
                Toggle(stringResource(R.string.set_dim), stringResource(R.string.set_dim_sub), s.dim) { v -> container.write { settings.update { it.copy(dim = v) } } }
                Toggle(stringResource(R.string.set_friends), stringResource(R.string.set_friends_sub), s.friends) { v -> container.write { settings.update { it.copy(friends = v) } } }
                Toggle(stringResource(R.string.set_haptics), stringResource(R.string.set_haptics_sub), s.haptics) { v -> container.write { settings.update { it.copy(haptics = v) } } }
            }

            SectionTitle(stringResource(R.string.set_language))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val cur = Language.current()
                Chip(stringResource(R.string.set_lang_system), cur == Language.SYSTEM, { Language.set(Language.SYSTEM) })
                Chip("English", cur == Language.EN, { Language.set(Language.EN) })
                Chip("Русский", cur == Language.RU, { Language.set(Language.RU) })
            }

            SectionTitle(stringResource(R.string.set_data))
            UCard {
                InfoRow(Icons.Rounded.Upload, stringResource(R.string.backup_export_sub), stringResource(R.string.backup_export)) { export.launch("lantern-${LocalDate.now()}.json") }
                InfoRow(Icons.Rounded.Download, stringResource(R.string.backup_import_sub), stringResource(R.string.backup_import)) { import.launch(arrayOf("application/json", "text/plain", "*/*")) }
            }

            CommunityLinks()

            SectionTitle(stringResource(R.string.set_about))
            Text(stringResource(R.string.set_privacy), style = UType.Sub)
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.set_propp), style = UType.Muted)
            Spacer(Modifier.height(28.dp))
        }
    }
    restoring?.let { uri ->
        ConfirmDialog(stringResource(R.string.backup_import_title), stringResource(R.string.backup_import_text), stringResource(R.string.backup_import_go), {
            scope.launch {
                val ok = withContext(Dispatchers.IO) {
                    runCatching {
                        val (all, opened) = Backup.read(c, uri)
                        container.db.dao().replaceAll(all)
                        container.settings.update { it.copy(onboarded = true, opened = it.opened + opened) }
                        container.changed()
                    }.isSuccess
                }
                Toast.makeText(c, if (ok) R.string.backup_restored else R.string.backup_bad, Toast.LENGTH_SHORT).show()
            }
        }, { restoring = null })
    }
}
