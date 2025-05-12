package com.droidcon.habitsync.ui.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

@Composable
fun HabitSyncApp(prefs: DataStore<Preferences>) {
    val scope = rememberCoroutineScope()
    val themeManager = remember { ThemeManager(prefs) }

    val theme by themeManager.themeFlow.collectAsState(initial = "system")

    val isDark = when (theme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }

    MaterialTheme(
        colors = if (isDark) darkColors() else lightColors()
    ) {
       // AppContent(themeManager)
    }
}
