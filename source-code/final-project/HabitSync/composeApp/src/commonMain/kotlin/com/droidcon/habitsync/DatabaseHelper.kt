package com.droidcon.habitsync

import app.cash.sqldelight.db.SqlDriver
import com.droidcon.habitsync.db.HabitDatabase
import com.droidcon.habitsync.db.User


class DatabaseHelper(driver: SqlDriver) {
    private val database = HabitDatabase(driver)
    private val userQueries = database.userQueries

    fun insertUser(name: String, email: String) {
        userQueries.insertUser(name, email)
    }

    fun getAllUsers(): List<User> = userQueries.selectAllUsers().executeAsList()

    fun getUserById(id: Long): User? = userQueries.selectUserById(id).executeAsOneOrNull()

    fun updateUser(id: Long, name: String, email: String) {
        userQueries.updateUser(name, email, id)
    }

    fun deleteUserById(id: Long) {
        userQueries.deleteUserById(id)
    }
}
