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
import com.droidcon.habitsync.data.db.DatabaseHelper
import com.droidcon.habitsync.domain.repository.HabitLogRepository
import com.droidcon.habitsync.presentation.screen.addedit.AddEditHabitScreen
import com.droidcon.habitsync.presentation.screen.habitdetail.HabitDetailScreen
import com.droidcon.habitsync.presentation.screen.theme.ThemeManager
import com.droidcon.habitsync.domain.model.AddEditMode
import com.droidcon.habitsync.presentation.screen.habitdetail.HabitDetailViewModel
import com.droidcon.habitsync.presentation.components.FilterRow
import com.droidcon.habitsync.presentation.components.HabitItem
import com.droidcon.habitsync.presentation.screen.debug.DebugScreen
import kotlinx.coroutines.launch

@Composable
fun MainHabitUI(
    habitViewModel: HabitViewModel,
    logRepo: HabitLogRepository,
    dbHelper: DatabaseHelper,
    themeManager: ThemeManager
) {
    // State to control which screen is currently shown
    var screenMode by remember { mutableStateOf<AddEditMode?>(null) }
    var selectedHabitId by remember { mutableStateOf<String?>(null) }
    var isDebugScreen by remember { mutableStateOf(false) }
    var showThemeSheet by remember { mutableStateOf(false) }

    // Bottom sheet state for theme selector
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    when {
        isDebugScreen -> {
            DebugScreen(onBack = { isDebugScreen = false }, db = dbHelper)
        }

        screenMode != null -> {
            AddEditHabitScreen(
                viewModel = habitViewModel,
                mode = screenMode!!,
                onSaved = { screenMode = null }
            )
        }

        selectedHabitId != null -> {
            val detailViewModel = remember(selectedHabitId) {
                HabitDetailViewModel(selectedHabitId!!, logRepo)
            }
            HabitDetailScreen(
                viewModel = detailViewModel,
                onBack = { selectedHabitId = null }
            )
        }

        else -> {
            // Default: show home screen
            HomeScreen(
                viewModel = habitViewModel,
                onAdd = { screenMode = AddEditMode.Add },
                onEdit = { screenMode = AddEditMode.Edit(it) },
                onDetail = { selectedHabitId = it },
                onDebugClick = { isDebugScreen = true },
                onShowTheme = {
                    showThemeSheet = true
                    scope.launch { sheetState.show() }
                },
                themeManager = themeManager
            )
        }
    }

}

@Composable
fun HomeScreen(
    viewModel: HabitViewModel,
    onAdd: () -> Unit,
    onEdit: (String) -> Unit,
    onDetail: (String) -> Unit,
    onDebugClick: () -> Unit,
    onShowTheme: () -> Unit,
    themeManager: ThemeManager
) {
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





