package com.droidcon.habitsync.presentation.screen.addedit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.droidcon.habitsync.domain.model.AddEditMode
import com.droidcon.habitsync.domain.repository.HabitLogRepository
import com.droidcon.habitsync.domain.repository.HabitRepository
import com.droidcon.habitsync.presentation.screen.home.HabitViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.compose.koinInject

@Composable
fun AddEditHabitScreen(
    mode: AddEditMode,
    onSaved: () -> Unit,
    habitRepository: HabitRepository = koinInject(),
    habitLogRepository: HabitLogRepository = koinInject(),
) {
    val scope = rememberCoroutineScope()
    val viewModel = viewModel { HabitViewModel(habitRepository, habitLogRepository) }

    var title by remember { mutableStateOf("") }
    var reminderTime by remember { mutableStateOf("08:00") }
    var createdAt by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var hour by remember { mutableStateOf(8) }
    var minute by remember { mutableStateOf(0) }

    LaunchedEffect(mode) {
        if (mode is AddEditMode.Edit) {
            viewModel.filteredHabits.collect { habits ->
                habits.find { it.id == mode.habitId }?.let { habit ->
                    title = habit.title
                    reminderTime = habit.reminderTime ?: "08:00"
                    createdAt = habit.createdAt

                    val parts = reminderTime.split(":")
                    hour = parts.getOrNull(0)?.toIntOrNull() ?: 8
                    minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
                }
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            TimePickerSheetContent(
                hour = hour,
                minute = minute,
                onHourSelected = { hour = it },
                onMinuteSelected = { minute = it },
                onDone = {
                    reminderTime = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
                    scope.launch { sheetState.hide() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (mode is AddEditMode.Add) "Add Habit" else "Edit Habit") },
                    navigationIcon = {
                        IconButton(onClick = onSaved) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val isEnabled = title.isNotBlank()
                        if (isEnabled) {
                            scope.launch {
                                val now = Clock.System.now().toString()
                                when (mode) {
                                    is AddEditMode.Add -> viewModel.addHabit(
                                        title,
                                        now,
                                        reminderTime
                                    )

                                    is AddEditMode.Edit -> viewModel.updateHabit(
                                        mode.habitId,
                                        title,
                                        reminderTime
                                    )
                                }
                                onSaved()
                            }
                        }
                    },
                    backgroundColor = if (title.isNotBlank()) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
                    contentColor = if (title.isNotBlank()) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Save")
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = reminderTime,
                        onValueChange = {},
                        label = { Text("Reminder Time") },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch { sheetState.show() }
                            }
                    )
                    Button(
                        onClick = {
                            scope.launch { sheetState.show() }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text("Select Time")
                    }

                    if (mode is AddEditMode.Edit) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Created On:", color = MaterialTheme.colors.onBackground)
                            Text(
                                createdAt,
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                }
            }
        }

    }
}


@Composable
fun TimePickerSheetContent(
    hour: Int,
    minute: Int,
    onHourSelected: (Int) -> Unit,
    onMinuteSelected: (Int) -> Unit,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .heightIn(min = 300.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select Time", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
        ) {
            ScrollableNumberPicker(
                title = "Hour",
                values = (0..23).toList(),
                selected = hour,
                onSelected = onHourSelected
            )
            Spacer(modifier = Modifier.width(24.dp)) // spacing between columns
            ScrollableNumberPicker(
                title = "Minute",
                values = (0..59).toList(),
                selected = minute,
                onSelected = onMinuteSelected
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onDone) {
            Text("Done")
        }
    }
}


@Composable
fun ScrollableNumberPicker(
    title: String,
    values: List<Int>,
    selected: Int,
    onSelected: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Text(
            title,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .height(150.dp)

        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(values) { value ->
                    val isSelected = value == selected
                    Text(
                        text = value.toString().padStart(2, '0'),
                        style = if (isSelected) MaterialTheme.typography.h6 else MaterialTheme.typography.body2,
                        color = if (isSelected)
                            MaterialTheme.colors.primary
                        else
                            MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier
                            .clickable { onSelected(value) }
                            .padding(vertical = 6.dp)
                    )
                }
            }
        }
    }
}



