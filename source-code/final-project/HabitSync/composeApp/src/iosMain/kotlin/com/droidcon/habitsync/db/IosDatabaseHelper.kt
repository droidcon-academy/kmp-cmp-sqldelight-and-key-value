package com.droidcon.habitsync.db

import app.cash.sqldelight.driver.native.NativeSqliteDriver

fun createDatabaseHelper(): DatabaseHelper {
    val driver = NativeSqliteDriver(HabitDatabase.Schema, "habit.db")
    return object : DatabaseHelper {
        override val db: HabitDatabase = HabitDatabase(driver)
    }
}
