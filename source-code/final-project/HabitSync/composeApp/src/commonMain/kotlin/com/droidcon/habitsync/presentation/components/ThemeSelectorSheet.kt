package com.droidcon.habitsync.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidcon.habitsync.presentation.screen.theme.ThemeManager
import kotlinx.coroutines.launch
import org.koin.compose.getKoin

@Composable
fun ThemeSelectorSheet(
    onDismiss: () -> Unit
) {
    val themeManager = getKoin().get<ThemeManager>()
    val theme by themeManager.themeFlow.collectAsState(initial = "system")
    val scope = rememberCoroutineScope()

    Column(Modifier.background(MaterialTheme.colors.surface).fillMaxSize()) {
        Text("Choose Theme", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(16.dp))

        // Options: Light, Dark, System
        listOf("light", "dark", "system").forEach { mode ->
            OutlinedButton(
                onClick = {
                    scope.launch {
                        themeManager.setTheme(mode)
                        onDismiss()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (theme == mode) "✓ ${mode.capitalize()}" else mode.capitalize())
            }
            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
            Text("Cancel")
        }
    }
}