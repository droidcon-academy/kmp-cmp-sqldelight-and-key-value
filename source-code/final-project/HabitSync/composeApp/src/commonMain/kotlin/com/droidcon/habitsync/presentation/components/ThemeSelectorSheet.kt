package com.droidcon.habitsync.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
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

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.surface)
            .fillMaxSize()
    ) {
        // Top Bar with Back Button
        TopAppBar(
            title = { Text("Choose Theme") },
            navigationIcon = {
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface,
            elevation = 4.dp
        )

        Spacer(Modifier.height(16.dp))

        // Theme selection options
        listOf("light", "dark", "system").forEach { mode ->
            OutlinedButton(
                onClick = {
                    scope.launch {
                        themeManager.setTheme(mode)
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(if (theme == mode) "✓ ${mode.capitalize()}" else mode.capitalize())
            }
            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Cancel")
        }
    }
}
