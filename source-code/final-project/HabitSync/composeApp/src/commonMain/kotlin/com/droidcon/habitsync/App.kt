package com.droidcon.habitsync

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.droidcon.habitsync.db.User
import com.droidcon.habitsync.db.UserQueries
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Define preference keys
val COUNT_KEY = intPreferencesKey("count")
val TEXT_KEY = stringPreferencesKey("text")

@Composable
fun App(prefs: DataStore<Preferences>) {
    val scope = rememberCoroutineScope()

    // Read current count and text values from DataStore
    var count by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("") }

    // Load initial values from DataStore when Composable starts
    LaunchedEffect(Unit) {
        val preferences = prefs.data.first()
        count = preferences[COUNT_KEY] ?: 0
        text = preferences[TEXT_KEY] ?: ""
    }

    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {

            // Display and modify count
            Text("Count: $count", style = MaterialTheme.typography.h1)
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                Button(onClick = {
                    count++
                    scope.launch {
                        prefs.updateData {
                            it.toMutablePreferences().apply { this[COUNT_KEY] = count }
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
                            it.toMutablePreferences().apply { this[COUNT_KEY] = count }
                        }
                    }
                }) {
                    Text("Decrease")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Edit and save text
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                    scope.launch {
                        prefs.updateData { prefs ->
                            prefs.toMutablePreferences().apply { this[TEXT_KEY] = text }
                        }
                    }
                },
                label = { Text("Enter text") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun UserScreen(dbHelper: DatabaseHelper) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var userIdToUpdate by remember { mutableStateOf("") }
    var userList by remember { mutableStateOf(emptyList<User>()) }

    // Load all users initially and on every change
    fun refreshUsers() {
        userList = dbHelper.getAllUsers()
    }

    LaunchedEffect(true) {
        refreshUsers()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Add / Update User", style = MaterialTheme.typography.h4)

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
                refreshUsers()
                name = ""
                email = ""
                userIdToUpdate = ""
            }) {
                Text("Add")
            }

            Spacer(Modifier.width(16.dp))

            Button(onClick = {
                userIdToUpdate.toLongOrNull()?.let { id ->
                    dbHelper.updateUser(id, name, email)
                    refreshUsers()
                    name = ""
                    email = ""
                    userIdToUpdate = ""
                }
            }) {
                Text("Update")
            }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Text("Users List", style = MaterialTheme.typography.h4)

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
                    refreshUsers()
                }) {
                    Text("Delete")
                }
            }
        }
    }
}


