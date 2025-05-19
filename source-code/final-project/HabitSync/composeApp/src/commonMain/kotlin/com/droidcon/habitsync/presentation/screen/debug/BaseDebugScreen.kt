package com.droidcon.habitsync.presentation.screen.debug

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidcon.habitsync.data.db.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.koin.compose.getKoin

@Composable
fun BaseDebugScreen(
    onBack: () -> Unit,
    onPrintClick: () -> Unit
) {
    val db = getKoin().get<DatabaseHelper>()
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

            // Resets all habit and log entries in the database
            Button(onClick = {
                scope.launch(Dispatchers.IO) {
                    db.db.habitQueries.deleteAll()
                    db.db.habitLogQueries.deleteAll()
                    println("✅ All habit and log data cleared.")
                }
            }) {
                Text("Reset All Data")
            }

            // Triggers DB info loading and opens the bottom sheet
            Button(onClick = onPrintClick) {
                Text("Print DB Info")
            }

            // Back navigation
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("← Back")
            }
        }
    }
}