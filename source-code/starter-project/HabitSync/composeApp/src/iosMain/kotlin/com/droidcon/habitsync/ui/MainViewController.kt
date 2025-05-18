package com.droidcon.habitsync.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.droidcon.habitsync.datastore.createDataStore
import com.droidcon.habitsync.db.DatabaseHelper
import com.droidcon.habitsync.db.DriverFactory
import com.droidcon.habitsync.di.sharedModule
import org.koin.core.context.startKoin


fun MainViewController() = ComposeUIViewController {
    val dbHelper = remember {
        DatabaseHelper(DriverFactory().createDriver())
    }

    val prefs = createDataStore()

    startKoin {
        modules(sharedModule(dbHelper, prefs))
    }
    MaterialTheme {
        MainScreen()
    }
}