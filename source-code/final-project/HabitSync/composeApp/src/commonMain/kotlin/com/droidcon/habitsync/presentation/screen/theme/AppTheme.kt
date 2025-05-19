package com.droidcon.habitsync.presentation.screen.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.*
import org.koin.compose.getKoin

/**
 * AppTheme is the top-level composable responsible for applying
 * the light or dark theme based on the user’s preference.
 *
 * @param themeManager used to fetch the current theme preference (light, dark, or system).
 * @param content composable UI content that will use the applied theme.
 */
@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val themeManager = getKoin().get<ThemeManager>()
    // Collect the current theme value (light/dark/system) from the DataStore
    val themePref by themeManager.themeFlow.collectAsState(initial = "system")

    // Determine whether dark theme should be applied
    val darkTheme = when (themePref) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme() // Follow system setting for "system"
    }

    // Apply MaterialTheme with the appropriate color scheme
    MaterialTheme(
        colors = if (darkTheme) darkColors() else lightColors(),
        content = content
    )
}
