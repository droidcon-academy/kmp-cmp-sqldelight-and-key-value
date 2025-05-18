package com.droidcon.habitsync

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
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