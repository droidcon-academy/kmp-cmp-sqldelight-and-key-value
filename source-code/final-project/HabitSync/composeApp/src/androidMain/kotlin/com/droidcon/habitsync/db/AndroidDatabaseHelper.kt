package com.droidcon.habitsync.db


import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

fun createDatabaseHelper(context: Context): DatabaseHelper {

    val driver = AndroidSqliteDriver(
        schema = HabitDatabase.Schema,
        context = context,
        name = "habit.db",
        callback = object : AndroidSqliteDriver.Callback(HabitDatabase.Schema) {
            override fun onCreate(db: SupportSQLiteDatabase) {
                println("📦 Database created")
            }

            override fun onUpgrade(
                db: SupportSQLiteDatabase,
                oldVersion: Int,
                newVersion: Int
            ) {
                println("🔁 Habit Tracker DB migrated from v$oldVersion → $newVersion")
            }
        }
    )
    //val driver = AndroidSqliteDriver(HabitDatabase.Schema, context, "habit.db")
    val currentVersion = HabitDatabase.Schema.version
    println("Habit Tracker Current schema version = $currentVersion")
    return object : DatabaseHelper {
        override val db: HabitDatabase = HabitDatabase(driver)
    }
}

