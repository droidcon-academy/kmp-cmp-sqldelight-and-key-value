package com.droidcon.habitsync.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        // Top App Bar
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

        Spacer(Modifier.height(8.dp))

        listOf(
            ThemeOption("light", "☀️  Light"),
            ThemeOption("dark", "🌙  Dark"),
            ThemeOption("system", "⚙️  System Default")
        ).forEach { option ->
            val isSelected = theme == option.key

            Surface(
                color = if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0.1f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable {
                        scope.launch {
                            themeManager.setTheme(option.key)
                            onDismiss()
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option.label,
                        style = if (isSelected)
                            MaterialTheme.typography.body1.copy(
                                color = MaterialTheme.colors.primary,
                                fontSize = 16.sp
                            )
                        else
                            MaterialTheme.typography.body1.copy(
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 16.sp
                            )
                    )
                    Spacer(Modifier.weight(1f))
                    if (isSelected) {
                        Text("✓", color = MaterialTheme.colors.primary)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Divider(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(8.dp))

        TextButton(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                "Cancel",
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

data class ThemeOption(val key: String, val label: String)
