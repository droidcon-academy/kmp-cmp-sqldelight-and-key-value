package  com.droidcon.habitsync.presentation.screen.debug

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidcon.habitsync.data.db.DatabaseHelper
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

    // When showSheet is true, display the DB info inside a ModalBottomSheet
    if (showSheet) {
        ModalBottomSheetLayout(
            sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded),
            sheetContent = {
                Column(
                    Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("Database Tables", style = MaterialTheme.typography.h6)
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
            // Inside modal layout: show buttons and actions
            BaseDebugScreen(db, onBack, onPrintClick = {
                scope.launch(Dispatchers.IO) {
                    val habits = db.db.habitQueries.selectAll().executeAsList()
                    val logs = db.db.habitLogQueries.selectAllLogs().executeAsList()

                    // Format and build display string
                    debugText = buildString {
                        append(" Habit Table:\n")
                        if (habits.isEmpty()) append("  (No data)\n")
                        else habits.forEach { append("  $it\n") }

                        append("\nHabitLog Table:\n")
                        if (logs.isEmpty()) append("  (No data)\n")
                        else logs.forEach { append("  $it\n") }
                    }

                    showSheet = true
                }
            })
        }
    } else {
        // When no sheet is showing, show normal debug UI
        BaseDebugScreen(db, onBack, onPrintClick = {
            scope.launch(Dispatchers.IO) {
                val habits = db.db.habitQueries.selectAll().executeAsList()
                val logs = db.db.habitLogQueries.selectAllLogs().executeAsList()

                debugText = buildString {
                    append("Habit Table:\n")
                    if (habits.isEmpty()) append("  (No data)\n")
                    else habits.forEach { append("  $it\n") }

                    append("\n HabitLog Table:\n")
                    if (logs.isEmpty()) append("  (No data)\n")
                    else logs.forEach { append("  $it\n") }
                }

                showSheet = true
            }
        })
    }
}


