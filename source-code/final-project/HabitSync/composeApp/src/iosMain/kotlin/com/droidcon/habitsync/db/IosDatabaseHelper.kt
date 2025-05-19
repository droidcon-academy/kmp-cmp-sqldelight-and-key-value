package com.droidcon.habitsync.db

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.droidcon.habitsync.data.datastore.db.DatabaseHelper

fun createDatabaseHelper(): DatabaseHelper {
    // Create a SQLDelight driver using the native iOS SQLite engine
    val driver = NativeSqliteDriver(
        schema = HabitDatabase.Schema, // Pass the generated database schema
        name = "habit.db"              // Name of the SQLite database file
    )

    // Return an implementation of DatabaseHelper interface
    return object : DatabaseHelper {
        override val db: HabitDatabase = HabitDatabase(driver)
    }
}
