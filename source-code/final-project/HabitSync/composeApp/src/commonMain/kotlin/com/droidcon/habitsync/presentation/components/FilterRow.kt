package com.droidcon.habitsync.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidcon.habitsync.domain.model.HabitFilter

@Composable
fun FilterRow(selected: HabitFilter, onSelect: (HabitFilter) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Show all filter types as buttons
        HabitFilter.values().forEach { filter ->
            OutlinedButton(
                onClick = { onSelect(filter) },
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = if (selected == filter)
                        MaterialTheme.colors.primary.copy(alpha = 0.2f)
                    else
                        MaterialTheme.colors.surface,
                    contentColor = if (selected == filter)
                        MaterialTheme.colors.primary
                    else
                        MaterialTheme.colors.onSurface
                ),
                elevation = ButtonDefaults.elevation(defaultElevation = 2.dp)
            ) {
                Text(filter.displayName)
            }
        }
    }
}