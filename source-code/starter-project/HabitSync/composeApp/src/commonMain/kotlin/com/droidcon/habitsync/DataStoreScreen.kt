package com.droidcon.habitsync

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val PREF_COUNT_KEY = intPreferencesKey("count")
val PREF_TEXT_KEY = stringPreferencesKey("text")

@Composable
fun DataStoreScreen(prefs: DataStore<Preferences>) {
    val scope = rememberCoroutineScope()
    var count by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("") }

    // Load values from DataStore once
    LaunchedEffect(Unit) {
        val preferences = prefs.data.first()
        count = preferences[PREF_COUNT_KEY] ?: 0
        text = preferences[PREF_TEXT_KEY] ?: ""
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Count: $count", style = MaterialTheme.typography.h5)

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Button(onClick = {
                count++
                scope.launch {
                    prefs.updateData {
                        it.toMutablePreferences().apply { this[PREF_COUNT_KEY] = count }
                    }
                }
            }) {
                Text("Increase")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                count--
                scope.launch {
                    prefs.updateData {
                        it.toMutablePreferences().apply { this[PREF_COUNT_KEY] = count }
                    }
                }
            }) {
                Text("Decrease")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                scope.launch {
                    prefs.updateData {
                        it.toMutablePreferences().apply { this[PREF_TEXT_KEY] = text }
                    }
                }
            },
            label = { Text("Enter text") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
