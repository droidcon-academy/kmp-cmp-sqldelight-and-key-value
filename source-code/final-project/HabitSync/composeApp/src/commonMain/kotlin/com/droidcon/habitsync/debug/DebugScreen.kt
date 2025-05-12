package com.droidcon.habitsync.ui.debug

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidcon.habitsync.db.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Composable
fun DebugScreen(
    db: DatabaseHelper,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }
    var debugText by remember { mutableStateOf("Loading...") }

    if (showSheet) {
        ModalBottomSheetLayout(
            sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded),
            sheetContent = {
                Column(
                    Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("📊 Database Tables", style = MaterialTheme.typography.h6)
                    Spacer(Modifier.height(8.dp))
                    Text(debugText, style = MaterialTheme.typography.body2)
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { showSheet = false },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Close")
                    }
                }
            }
        ) {
            // Trigger the sheet inside this layout
            BaseDebugScreen(db, onBack, onPrintClick = {
                scope.launch(Dispatchers.IO) {
                    val habits = db.db.habitQueries.selectAll().executeAsList()
                    val logs = db.db.habitLogQueries.selectAllLogs().executeAsList()

                    debugText = buildString {
                        append("🗂 Habit Table:\n")
                        if (habits.isEmpty()) append("  (No data)\n")
                        else habits.forEach { append("  $it\n") }

                        append("\n🗂 HabitLog Table:\n")
                        if (logs.isEmpty()) append("  (No data)\n")
                        else logs.forEach { append("  $it\n") }
                    }

                    showSheet = true
                }
            })
        }
    } else {
        BaseDebugScreen(db, onBack, onPrintClick = {
            scope.launch(Dispatchers.IO) {
                val habits = db.db.habitQueries.selectAll().executeAsList()
                val logs = db.db.habitLogQueries.selectAllLogs().executeAsList()

                debugText = buildString {
                    append("🗂 Habit Table:\n")
                    if (habits.isEmpty()) append("  (No data)\n")
                    else habits.forEach { append("  $it\n") }

                    append("\n🗂 HabitLog Table:\n")
                    if (logs.isEmpty()) append("  (No data)\n")
                    else logs.forEach { append("  $it\n") }
                }

                showSheet = true
            }
        })
    }
}

@Composable
private fun BaseDebugScreen(
    db: DatabaseHelper,
    onBack: () -> Unit,
    onPrintClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Debug Tools") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("🧪 Developer Actions")

            Button(onClick = {
                scope.launch(Dispatchers.IO) {
                    db.db.habitQueries.deleteAll()
                    db.db.habitLogQueries.deleteAll()
                    println("✅ All habit and log data cleared.")
                }
            }) {
                Text("Reset All Data")
            }

            Button(onClick = onPrintClick) {
                Text("Print DB Info")
            }

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("← Back")
            }
        }
    }
}
