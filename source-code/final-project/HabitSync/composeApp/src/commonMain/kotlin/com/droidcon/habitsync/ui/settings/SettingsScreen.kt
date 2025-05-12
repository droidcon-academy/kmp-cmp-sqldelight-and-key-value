package com.droidcon.habitsync.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidcon.habitsync.keys.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
@Composable
fun SettingsScreen(
    themeManager: ThemeManager,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val theme by themeManager.themeFlow.collectAsState(initial = "system")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Theme")

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("light", "dark", "system").forEach { mode ->
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                themeManager.setTheme(mode)
                            }
                        }
                    ) {
                        Text(if (theme == mode) "✓ $mode" else mode)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            OutlinedButton(onClick = onBack) { Text("← Back") }
        }
    }
}
