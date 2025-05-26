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
import com.droidcon.habitsync.presentation.components.timepicker.TimePickerSheetContent
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



