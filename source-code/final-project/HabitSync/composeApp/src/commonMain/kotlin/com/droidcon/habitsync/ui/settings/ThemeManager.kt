package com.droidcon.habitsync.ui.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.droidcon.habitsync.keys.THEME_MODE_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeManager(private val prefs: DataStore<Preferences>) {

    val themeFlow: Flow<String> = prefs.data.map {
        it[THEME_MODE_KEY] ?: "system"
    }

    suspend fun setTheme(value: String) {
        prefs.updateData {
            it.toMutablePreferences().apply { this[THEME_MODE_KEY] = value }
        }
    }
}
