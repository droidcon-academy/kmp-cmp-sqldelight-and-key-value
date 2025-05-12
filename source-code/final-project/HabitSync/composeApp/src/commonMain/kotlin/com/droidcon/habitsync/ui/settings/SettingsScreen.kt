package com.droidcon.habitsync.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
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
fun SettingsScreen(prefs: DataStore<Preferences>) {
    val scope = rememberCoroutineScope()

    val themeFlow = prefs.data.map { it[THEME_MODE_KEY] ?: "system" }
    val reminderTimeFlow = prefs.data.map { it[REMINDER_TIME_KEY] ?: "08:00" }

    val theme by themeFlow.collectAsState(initial = "system")
    val reminderTime by reminderTimeFlow.collectAsState(initial = "08:00")

    Column(Modifier.padding(16.dp)) {
        Text("Theme Mode")

        Row {
            listOf("light", "dark", "system").forEach { option ->
                Button(
                    onClick = {
                        scope.launch {
                            prefs.edit { it[THEME_MODE_KEY] = option }
                        }
                    },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(if (theme == option) "✓ $option" else option)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = reminderTime,
            onValueChange = { newTime ->
                scope.launch {
                    prefs.edit { it[REMINDER_TIME_KEY] = newTime }
                }
            },
            label = { Text("Reminder Time (e.g., 08:00)") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
