package com.droidcon.habitsync.presentation.screen.habitdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.droidcon.habitsync.domain.repository.HabitLogRepository
import org.koin.compose.getKoin

@Composable
fun HabitDetailScreen(
    habitId: String,
    onBack: () -> Unit
) {
    val logRepo = getKoin().get<HabitLogRepository>()
    val viewModel = viewModel { HabitDetailViewModel(habitId, logRepo) }
    val isDoneToday by viewModel.isCompletedToday.collectAsState()
    val completedDates by viewModel.completedDates.collectAsState()
    val streakInfo by viewModel.streakInfo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Habit Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            // Checkbox to toggle today's completion
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                    text = if (isDoneToday) "Completed today" else "Mark as done today",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onBackground
                )
            }

            Spacer(Modifier.height(24.dp))

            // Calendar Grid
            Text(
                text = "📅 Past 42 Days",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onBackground
            )
            Spacer(Modifier.height(8.dp))
            CalendarGridView(completedDates)

            Spacer(Modifier.height(24.dp))

            // Streak Info
            Text(
                text = "🔥 Current Streak: ${streakInfo.currentStreak} days",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onBackground
            )
            Text(
                text = "🏆 Best Streak: ${streakInfo.bestStreak} days",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}



