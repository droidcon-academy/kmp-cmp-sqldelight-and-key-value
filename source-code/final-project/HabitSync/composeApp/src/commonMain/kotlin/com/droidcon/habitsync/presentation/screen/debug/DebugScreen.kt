package  com.droidcon.habitsync.presentation.screen.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidcon.habitsync.data.db.DatabaseHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.koin.compose.getKoin

@Composable
fun DebugScreen(
    onBack: () -> Unit
) {
    val db = getKoin().get<DatabaseHelper>()
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }
    var debugText by remember { mutableStateOf("Loading...") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Debug Tools") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
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
                DebugActions(innerPadding, scope, db, onShowSheet = { debugText = it; showSheet = true })
            }
        } else {
            DebugActions(innerPadding, scope, db, onShowSheet = { debugText = it; showSheet = true })
        }
    }
}

@Composable
private fun DebugActions(
    innerPadding: PaddingValues,
    scope: CoroutineScope,
    db: DatabaseHelper,
    onShowSheet: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            scope.launch(Dispatchers.IO) {
                val habits = db.db.habitQueries.selectAll().executeAsList()
                val logs = db.db.habitLogQueries.selectAllLogs().executeAsList()

                val debugText = buildString {
                    append("Habit Table:\n")
                    if (habits.isEmpty()) append("  (No data)\n")
                    else habits.forEach { append("  $it\n") }

                    append("\nHabitLog Table:\n")
                    if (logs.isEmpty()) append("  (No data)\n")
                    else logs.forEach { append("  $it\n") }
                }

                onShowSheet(debugText)
            }
        }) {
            Text("Print All Tables")
        }

        Button(onClick = {
            scope.launch(Dispatchers.IO) {
                db.db.habitQueries.deleteAll()
                db.db.habitLogQueries.deleteAll()
            }
        }) {
            Text("Reset All Data")
        }
    }
}
