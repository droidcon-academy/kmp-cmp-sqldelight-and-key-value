package com.droidcon.habitsync.presentation.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.droidcon.habitsync.domain.repository.HabitLogRepository
import com.droidcon.habitsync.domain.repository.HabitRepository
import com.droidcon.habitsync.presentation.components.FilterRow
import com.droidcon.habitsync.presentation.components.HabitItem
import org.koin.compose.getKoin
import org.koin.compose.koinInject


@Composable
fun HomeScreen(
    onAdd: () -> Unit,
    onEdit: (String) -> Unit,
    onDetail: (String) -> Unit,
    onDebugClick: () -> Unit,
    onShowTheme: () -> Unit,
    habitRepository: HabitRepository = koinInject<HabitRepository>(),
    habitLogRepository: HabitLogRepository = koinInject<HabitLogRepository>()
) {
    val viewModel =  viewModel {HabitViewModel(habitRepository, habitLogRepository)}
    val habits by viewModel.filteredHabits.collectAsState()
    val selectedFilter by viewModel.filter.collectAsState()
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Habits") },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }

                    // Dropdown menu for Debug + Theme options
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            menuExpanded = false
                            onDebugClick()
                        }) {
                            Text("Debug Tools")
                        }

                        Divider()

                        DropdownMenuItem(onClick = {
                            menuExpanded = false
                            onShowTheme()
                        }) {
                            Text("Theme")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Text("+")
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Filter chips for habit filtering (All, Completed, Missed)
            FilterRow(selected = selectedFilter, onSelect = viewModel::setFilter)

            if (habits.isEmpty()) {
                // Show empty state
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No habits yet. Tap + to add one.")
                }
            } else {
                // List all habits
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        Spacer(Modifier.height(12.dp))
                    }
                    items(habits) { habit ->
                        HabitItem(
                            habit = habit,
                            onDelete = { viewModel.deleteHabit(habit.id) },
                            onEdit = { onEdit(habit.id) },
                            onClick = { onDetail(habit.id) }
                        )
                    }
                    item {
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}





