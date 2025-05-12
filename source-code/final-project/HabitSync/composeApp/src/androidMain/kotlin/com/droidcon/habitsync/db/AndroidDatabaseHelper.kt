package com.droidcon.habitsync.db


import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

fun createDatabaseHelper(context: Context): DatabaseHelper {
    val driver = AndroidSqliteDriver(HabitDatabase.Schema, context, "habit.db")
    return object : DatabaseHelper {
        override val db: HabitDatabase = HabitDatabase(driver)
    }
}

