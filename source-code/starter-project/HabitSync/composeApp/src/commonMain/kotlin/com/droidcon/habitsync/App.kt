package com.droidcon.habitsync

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.droidcon.habitsync.db.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Keys for DataStore
val PREF_COUNT_KEY = intPreferencesKey("count")
val PREF_TEXT_KEY = stringPreferencesKey("text")

/**
 * Entry point Composable that lets user toggle between two screens:
 * - DataStore example
 * - SQLDelight User CRUD example
 */
@Composable
fun MainScreen(prefs: DataStore<Preferences>, dbHelper: DatabaseHelper) {
    var currentScreen by remember { mutableStateOf(ScreenOption.DATASTORE) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Toggle buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { currentScreen = ScreenOption.DATASTORE },
                enabled = currentScreen != ScreenOption.DATASTORE
            ) {
                Text("DataStore Demo")
            }

            Button(
                onClick = { currentScreen = ScreenOption.SQLDELIGHT },
                enabled = currentScreen != ScreenOption.SQLDELIGHT
            ) {
                Text("User CRUD (SQLDelight)")
            }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // Render selected screen
        when (currentScreen) {
            ScreenOption.DATASTORE -> DataStoreScreen(prefs)
            ScreenOption.SQLDELIGHT -> SqlDelightUserScreen(dbHelper)
        }
    }
}

// Enum for clarity
private enum class ScreenOption {
    DATASTORE,
    SQLDELIGHT
}
@Composable
fun DataStoreScreen(prefs: DataStore<Preferences>) {
    val scope = rememberCoroutineScope()
    var count by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("") }

    // Load values from DataStore once
    LaunchedEffect(Unit) {
        val preferences = prefs.data.first()
        count = preferences[PREF_COUNT_KEY] ?: 0
        text = preferences[PREF_TEXT_KEY] ?: ""
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Count: $count", style = MaterialTheme.typography.h5)

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Button(onClick = {
                count++
                scope.launch {
                    prefs.updateData {
                        it.toMutablePreferences().apply { this[PREF_COUNT_KEY] = count }
                    }
                }
            }) {
                Text("Increase")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                count--
                scope.launch {
                    prefs.updateData {
                        it.toMutablePreferences().apply { this[PREF_COUNT_KEY] = count }
                    }
                }
            }) {
                Text("Decrease")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                scope.launch {
                    prefs.updateData {
                        it.toMutablePreferences().apply { this[PREF_TEXT_KEY] = text }
                    }
                }
            },
            label = { Text("Enter text") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
@Composable
fun SqlDelightUserScreen(dbHelper: DatabaseHelper) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var userIdToUpdate by remember { mutableStateOf("") }
    var userList by remember { mutableStateOf(emptyList<User>()) }

    fun loadUsers() {
        userList = dbHelper.getAllUsers()
    }

    LaunchedEffect(Unit) {
        loadUsers()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("User Entry", style = MaterialTheme.typography.h5)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        OutlinedTextField(
            value = userIdToUpdate,
            onValueChange = { userIdToUpdate = it },
            label = { Text("User ID (for update)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Button(onClick = {
                dbHelper.insertUser(name, email)
                loadUsers()
                name = ""
                email = ""
                userIdToUpdate = ""
            }) {
                Text("Add")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = {
                userIdToUpdate.toLongOrNull()?.let { id ->
                    dbHelper.updateUser(id, name, email)
                    loadUsers()
                    name = ""
                    email = ""
                    userIdToUpdate = ""
                }
            }) {
                Text("Update")
            }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Text("Users List", style = MaterialTheme.typography.h5)

        userList.forEach { user ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("ID: ${user.id}")
                    Text("Name: ${user.name}")
                    Text("Email: ${user.email}")
                }
                Button(onClick = {
                    dbHelper.deleteUserById(user.id)
                    loadUsers()
                }) {
                    Text("Delete")
                }
            }
        }
    }
}
