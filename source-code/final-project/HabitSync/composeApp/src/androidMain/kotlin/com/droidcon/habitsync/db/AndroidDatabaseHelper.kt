package com.droidcon.habitsync.db

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

fun createDatabaseHelper(context: Context): DatabaseHelper {
    // Create the SQLDelight driver with a callback to handle creation and upgrade events
    val driver = AndroidSqliteDriver(
        schema = HabitDatabase.Schema,
        context = context,
        name = "habit.db",
        callback = object : AndroidSqliteDriver.Callback(HabitDatabase.Schema) {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                println("📦 Habit Tracker DB created")
            }

            override fun onUpgrade(
                db: SupportSQLiteDatabase,
                oldVersion: Int,
                newVersion: Int
            ) {
                super.onUpgrade(db, oldVersion, newVersion)
                println("Habit Tracker DB migrated from v$oldVersion → $newVersion")
            }
        }
    )

    // Log the current schema version (for debug visibility)
    val currentVersion = HabitDatabase.Schema.version
    println("Habit Tracker current schema version = $currentVersion")

    // Return your DatabaseHelper implementation
    return object : DatabaseHelper {
        override val db: HabitDatabase = HabitDatabase(driver)
    }
}
