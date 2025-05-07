package com.droidcon.habitsync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.droidcon.habitsync.db.AppDatabase


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val driverFactory = DriverFactory(this)
        val dbHelper = DatabaseHelper(driverFactory.createDriver())

        setContent {
            MainScreen(dbHelper = dbHelper, prefs = createDataStore(applicationContext))
        }
    }
}
