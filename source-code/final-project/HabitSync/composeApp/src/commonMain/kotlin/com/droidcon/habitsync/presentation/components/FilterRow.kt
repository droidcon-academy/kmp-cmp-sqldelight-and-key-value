package com.droidcon.habitsync.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import com.droidcon.habitsync.domain.model.HabitFilter

@Composable
fun FilterRow(selected: HabitFilter, onSelect: (HabitFilter) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
    ) {
        HabitFilter.values().forEach { filter ->
            FilterChip(
                text = filter.displayName,
                selected = selected == filter,
                onClick = { onSelect(filter) },
                shape = RoundedCornerShape(24.dp)
            )
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    shape: Shape
) {
    Surface(
        modifier = Modifier
            .clickable(onClick = onClick),
        color = if (selected)
            MaterialTheme.colors.primary.copy(alpha = 0.15f)
        else
            MaterialTheme.colors.onSurface.copy(alpha = 0.05f),
        contentColor = if (selected)
            MaterialTheme.colors.primary
        else
            MaterialTheme.colors.onSurface,
        shape = shape,
        elevation = 1.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.body2
        )
    }
}
