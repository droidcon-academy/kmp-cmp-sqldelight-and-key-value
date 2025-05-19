package com.droidcon.habitsync.presentation.screen.habitdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn

/**
 * Calendar-like grid to show the last 42 days of habit completion.
 */
@Composable
fun CalendarGridView(completedDates: List<String>) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    // Generate a list of the past 42 days
    val past42Days = (0..41).map { today.minus(it, DateTimeUnit.DAY) }.reversed()

    // Convert completed dates to a set for fast lookup
    val completedSet = remember(completedDates) { completedDates.toSet() }

    // Define UI styles based on theme
    val shape: Shape = MaterialTheme.shapes.small
    val completedColor = MaterialTheme.colors.primary
    val uncompletedColor = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
    val textColor = MaterialTheme.colors.onPrimary

    // Display grid of past 42 days
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(past42Days) { date ->
            val dateStr = date.toString()
            val completed = completedSet.contains(dateStr)

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(shape)
                    .background(
                        color = if (completed) completedColor else uncompletedColor
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.caption,
                    color = if (completed) textColor else MaterialTheme.colors.onSurface
                )
            }
        }
    }
}