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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.droidcon.habitsync.viewmodel.HabitDetailViewModel
import com.droidcon.habitsync.utils.StreakInfo
import kotlinx.datetime.*

@Composable
fun HabitDetailScreen(
    viewModel: HabitDetailViewModel,
    onBack: () -> Unit
) {
    val isDoneToday by viewModel.isCompletedToday.collectAsState()
    val completedDates by viewModel.completedDates.collectAsState()
    val streakInfo by viewModel.streakInfo.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text("Habit Detail", style = MaterialTheme.typography.h6)
        }
        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isDoneToday,
                onCheckedChange = { viewModel.toggleTodayCompletion() }
            )
            Spacer(Modifier.width(8.dp))
            Text(if (isDoneToday) "Completed today" else "Mark as done today")
        }

        Spacer(Modifier.height(24.dp))

        Text("Past 42 Days", style = MaterialTheme.typography.subtitle1)
        CalendarGridView(completedDates)

        Spacer(Modifier.height(24.dp))

        Text("🔥 Current Streak: ${streakInfo.currentStreak} days")
        Text("🏆 Best Streak: ${streakInfo.bestStreak} days")
    }
}

@Composable
fun CalendarGridView(completedDates: List<String>) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val past42Days = (0..41).map { today.minus(it, DateTimeUnit.DAY) }.reversed()
    val completedSet = remember(completedDates) { completedDates.toSet() }

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

            Column(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = if (completed) Color.Green else Color.LightGray,
                        shape = MaterialTheme.shapes.small
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.caption,
                    color = Color.White
                )
            }
        }
    }
}
