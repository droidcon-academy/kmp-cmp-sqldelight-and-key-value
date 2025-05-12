package com.droidcon.habitsync.ui.habit_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.droidcon.habitsync.viewmodel.HabitDetailViewModel
import kotlinx.datetime.*

@Composable
fun HabitDetailScreen(
    viewModel: HabitDetailViewModel,
    onBack: () -> Unit
) {
    val isDoneToday by viewModel.isCompletedToday.collectAsState()
    val completedDates by viewModel.completedDates.collectAsState()
    val streakInfo by viewModel.streakInfo.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colors.onBackground
                )
            }
            Spacer(Modifier.width(8.dp))
            Text("Habit Detail", style = MaterialTheme.typography.h6, color = MaterialTheme.colors.onBackground)
        }

        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isDoneToday,
                onCheckedChange = { viewModel.toggleTodayCompletion() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.primary,
                    uncheckedColor = MaterialTheme.colors.onSurface
                )
            )
            Spacer(Modifier.width(8.dp))
            Text(
                if (isDoneToday) "Completed today" else "Mark as done today",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onBackground
            )
        }

        Spacer(Modifier.height(24.dp))

        Text("📅 Past 42 Days", style = MaterialTheme.typography.subtitle1, color = MaterialTheme.colors.onBackground)
        Spacer(Modifier.height(8.dp))
        CalendarGridView(completedDates)

        Spacer(Modifier.height(24.dp))

        Text("🔥 Current Streak: ${streakInfo.currentStreak} days", style = MaterialTheme.typography.body1, color = MaterialTheme.colors.onBackground)
        Text("🏆 Best Streak: ${streakInfo.bestStreak} days", style = MaterialTheme.typography.body1, color = MaterialTheme.colors.onBackground)
    }
}

@Composable
fun CalendarGridView(completedDates: List<String>) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val past42Days = (0..41).map { today.minus(it, DateTimeUnit.DAY) }.reversed()
    val completedSet = remember(completedDates) { completedDates.toSet() }

    val shape: Shape = MaterialTheme.shapes.small
    val completedColor = MaterialTheme.colors.primary
    val uncompletedColor = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
    val textColor = MaterialTheme.colors.onPrimary

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
