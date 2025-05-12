package com.droidcon.habitsync.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidcon.habitsync.db.Habit
import com.droidcon.habitsync.repository.HabitLogRepository
import com.droidcon.habitsync.ui.add_edit.AddEditHabitScreen
import com.droidcon.habitsync.ui.debug.DebugScreen
import com.droidcon.habitsync.ui.habit_detail.HabitDetailScreen
import com.droidcon.habitsync.ui.settings.ThemeManager
import com.droidcon.habitsync.utils.formatDateTimeKMP
import com.droidcon.habitsync.viewmodel.AddEditMode
import com.droidcon.habitsync.viewmodel.HabitDetailViewModel
import com.droidcon.habitsync.viewmodel.HabitFilter
import com.droidcon.habitsync.viewmodel.HabitViewModel
import kotlinx.coroutines.launch

@Composable
fun MainHabitUI(
    habitViewModel: HabitViewModel,
    logRepo: HabitLogRepository,
    dbHelper: com.droidcon.habitsync.db.DatabaseHelper,
    themeManager: ThemeManager
) {
    var screenMode by remember { mutableStateOf<AddEditMode?>(null) }
    var selectedHabitId by remember { mutableStateOf<String?>(null) }
    var isDebugScreen by remember { mutableStateOf(false) }
    var showThemeSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            ThemeSelectorSheet(
                themeManager = themeManager,
                onDismiss = {
                    scope.launch { sheetState.hide() }
                    showThemeSheet = false
                }
            )
        }
    ) {
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
            FilterRow(selected = selectedFilter, onSelect = viewModel::setFilter)

            if (habits.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No habits yet. Tap + to add one.")
                }
            } else {
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

@Composable
fun FilterRow(selected: HabitFilter, onSelect: (HabitFilter) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
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

@Composable
fun HabitItem(
    habit: Habit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(habit.title, style = MaterialTheme.typography.subtitle1)
                    Text("Created: ${formatDateTimeKMP(habit.createdAt)}", style = MaterialTheme.typography.caption)
                }
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }

            Divider(
                color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                thickness = 2.dp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ThemeSelectorSheet(
    themeManager: ThemeManager,
    onDismiss: () -> Unit
) {
    val theme by themeManager.themeFlow.collectAsState(initial = "system")
    val scope = rememberCoroutineScope()

    Column(Modifier.padding(16.dp)) {
        Text("Choose Theme", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(16.dp))

        listOf("light", "dark", "system").forEach { mode ->
            OutlinedButton(
                onClick = {
                    scope.launch {
                        themeManager.setTheme(mode)
                        onDismiss()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (theme == mode) "✓ ${mode.capitalize()}" else mode.capitalize())
            }
            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
            Text("Cancel")
        }
    }
}
