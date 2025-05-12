package com.droidcon.habitsync.ui.add_edit

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidcon.habitsync.viewmodel.AddEditMode
import com.droidcon.habitsync.viewmodel.HabitViewModel
import kotlinx.coroutines.launch

@Composable
fun AddEditHabitScreen(
    viewModel: HabitViewModel,
    mode: AddEditMode,
    onSaved: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var reminderTime by remember { mutableStateOf("") }
    var createdAt by remember { mutableStateOf("") }

    LaunchedEffect(mode) {
        if (mode is AddEditMode.Edit) {
            viewModel.getHabitById(mode.habitId)?.let {
                title = it.title
                reminderTime = it.reminderTime ?: ""
                createdAt = it.createdAt
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = if (mode is AddEditMode.Add) "Add Habit" else "Edit Habit",
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = reminderTime,
                onValueChange = { reminderTime = it },
                label = { Text("Reminder Time (e.g., 08:00)") },
                modifier = Modifier.fillMaxWidth()
            )

            if (mode is AddEditMode.Edit) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Created On:")
                    Text(createdAt, style = MaterialTheme.typography.caption)
                }
            }
        }

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
