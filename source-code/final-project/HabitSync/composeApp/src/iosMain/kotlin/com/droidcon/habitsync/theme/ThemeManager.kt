package com.droidcon.habitsync.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.droidcon.habitsync.keys.THEME_MODE_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeManager(private val prefs: DataStore<Preferences>) {
    val themeFlow: Flow<String> = prefs.data.map { it[THEME_MODE_KEY] ?: "system" }
    suspend fun setTheme(value: String) { prefs.edit { it[THEME_MODE_KEY] = value } }
}
