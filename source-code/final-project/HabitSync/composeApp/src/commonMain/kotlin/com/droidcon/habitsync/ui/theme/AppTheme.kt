package com.droidcon.habitsync.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.*

@Composable
fun AppTheme(
    themeManager: ThemeManager,
    content: @Composable () -> Unit
) {
    val themePref by themeManager.themeFlow.collectAsState(initial = "system")

    val darkTheme = when (themePref) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }

    MaterialTheme(
        colors = if (darkTheme) darkColors() else lightColors(),
        content = content
    )
}
