package com.droidcon.habitsync.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.droidcon.habitsync.db.DatabaseHelper
import com.droidcon.habitsync.db.User
import org.koin.compose.getKoin

@Composable
fun SqlDelightUserScreen() {
    val dbHelper: DatabaseHelper = getKoin().get()
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

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("User Entry", style = MaterialTheme.typography.h5)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        OutlinedTextField(
            value = userIdToUpdate,
            onValueChange = { userIdToUpdate = it },
            label = { Text("User ID (for update)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
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
