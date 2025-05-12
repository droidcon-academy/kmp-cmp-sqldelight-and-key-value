package com.droidcon.habitsync.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidcon.habitsync.db.Habit
import com.droidcon.habitsync.viewmodel.AddEditMode
import com.droidcon.habitsync.viewmodel.HabitFilter
import com.droidcon.habitsync.viewmodel.HabitViewModel
import kotlinx.coroutines.launch

@Composable
fun MainHabitUI(viewModel: HabitViewModel) {
    var screenMode by remember { mutableStateOf<AddEditMode?>(null) }

    if (screenMode != null) {
        AddEditHabitScreen(
            viewModel = viewModel,
            mode = screenMode!!,
            onSaved = { screenMode = null }
        )
    } else {
        HomeScreen(
            viewModel = viewModel,
            onAdd = { screenMode = AddEditMode.Add },
            onEdit = { screenMode = AddEditMode.Edit(it) }
        )
    }
}

@Composable
fun HomeScreen(
    viewModel: HabitViewModel,
    onAdd: () -> Unit,
    onEdit: (String) -> Unit
) {
    val habits by viewModel.filteredHabits.collectAsState()
    val selectedFilter by viewModel.filter.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Text("+")
            }
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding).fillMaxSize()) {

            FilterRow(selected = selectedFilter, onSelect = viewModel::setFilter)

            Spacer(Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(habits) { habit ->
                    HabitItem(
                        habit = habit,
                        onDelete = { viewModel.deleteHabit(habit.id) },
                        onEdit = { onEdit(habit.id) }
                    )
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
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        HabitFilter.values().forEach { filter ->
            Button(
                onClick = { onSelect(filter) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (selected == filter) MaterialTheme.colors.primary else MaterialTheme.colors.surface
                )
            ) {
                Text(filter.name)
            }
        }
    }
}

@Composable
fun HabitItem(
    habit: Habit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onEdit() },
        elevation = 2.dp
    ) {
        Row(
            Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(habit.title, style = MaterialTheme.typography.h6)
                Text("Created: ${habit.createdAt}", style = MaterialTheme.typography.caption)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun AddEditHabitScreen(
    viewModel: HabitViewModel,
    mode: AddEditMode,
    onSaved: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var reminderTime by remember { mutableStateOf("") }

    // Pre-fill in Edit mode
    LaunchedEffect(mode) {
        if (mode is AddEditMode.Edit) {
            viewModel.getHabitById(mode.habitId)?.let {
                title = it.title
                reminderTime = it.reminderTime ?: ""
            }
        }
    }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = reminderTime,
            onValueChange = { reminderTime = it },
            label = { Text("Reminder Time (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (title.isBlank()) return@Button

                scope.launch {
                    val now = kotlinx.datetime.Clock.System.now().toString()
                    when (mode) {
                        is AddEditMode.Add -> viewModel.addHabit(title, now, reminderTime)
                        is AddEditMode.Edit -> viewModel.updateHabit(mode.habitId, title, reminderTime)
                    }
                    onSaved()
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save")
        }
    }
}
