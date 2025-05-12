package com.droidcon.habitsync.ui.add_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
