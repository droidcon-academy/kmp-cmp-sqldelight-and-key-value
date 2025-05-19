package com.droidcon.habitsync.presentation.screen.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.droidcon.habitsync.presentation.screen.theme.keys.THEME_MODE_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * ThemeManager handles storing and retrieving the user's selected theme mode
 * using Jetpack DataStore.
 *
 * @param prefs The injected DataStore instance for accessing key-value storage.
 */
class ThemeManager(private val prefs: DataStore<Preferences>) {

    /**
     * A Flow that emits the current theme preference.
     * Falls back to "system" if no theme is explicitly set.
     */
    val themeFlow: Flow<String> = prefs.data.map {
        it[THEME_MODE_KEY] ?: "system"
    }

    /**
     * Saves the given theme mode ("light", "dark", or "system") into DataStore.
     *
     * @param value The selected theme mode.
     */
    suspend fun setTheme(value: String) {
        prefs.updateData {
            it.toMutablePreferences().apply {
                this[THEME_MODE_KEY] = value
            }
        }
    }
}
