// AddEditHabitScreen.kt
package com.droidcon.habitsync.presentation.screen.addedit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.droidcon.habitsync.domain.model.AddEditMode
import com.droidcon.habitsync.domain.repository.HabitLogRepository
import com.droidcon.habitsync.domain.repository.HabitRepository
import com.droidcon.habitsync.presentation.screen.home.HabitViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.compose.getKoin
import org.koin.compose.koinInject

/**
 * A Composable screen to add or edit a habit.
 * Shows a form with title, optional reminder time, and creation date in edit mode.
 */
@Composable
fun AddEditHabitScreen(
    mode: AddEditMode,
    onSaved: () -> Unit,
    habitRepository: HabitRepository = koinInject<HabitRepository>(),
    habitLogRepository: HabitLogRepository = koinInject<HabitLogRepository>(),
) {
    val scope = rememberCoroutineScope()
    val viewModel =  viewModel {HabitViewModel(habitRepository, habitLogRepository)}
    // UI state for form fields
    var title by remember { mutableStateOf("") }
    var reminderTime by remember { mutableStateOf("") }
    var createdAt by remember { mutableStateOf("") }

    // Pre-fill the form in edit mode
    LaunchedEffect(mode) {
        if (mode is AddEditMode.Edit) {
            viewModel.getHabitById(mode.habitId)?.let {
                title = it.title
                reminderTime = it.reminderTime ?: ""
                createdAt = it.createdAt
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (mode is AddEditMode.Add) "Add Habit" else "Edit Habit")
                },
                navigationIcon = {
                    IconButton(onClick = onSaved) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
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
                // Habit title input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.body1,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colors.onBackground,
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                        cursorColor = MaterialTheme.colors.primary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Reminder time input
                OutlinedTextField(
                    value = reminderTime,
                    onValueChange = { reminderTime = it },
                    label = { Text("Reminder Time (e.g., 08:00)") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.body1,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = MaterialTheme.colors.onBackground,
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                        cursorColor = MaterialTheme.colors.primary
                    )
                )

                // Created date display (only in Edit mode)
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

            // Save button
            Button(
                onClick = {
                    if (title.isBlank()) return@Button

                    scope.launch {
                        val now = Clock.System.now().toString()
                        when (mode) {
                            is AddEditMode.Add -> viewModel.addHabit(title, now, reminderTime)
                            is AddEditMode.Edit -> viewModel.updateHabit(
                                mode.habitId,
                                title,
                                reminderTime
                            )
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
}