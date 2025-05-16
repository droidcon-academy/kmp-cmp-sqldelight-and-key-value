package com.droidcon.habitsync

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController


fun MainViewController() = ComposeUIViewController {
    val dbHelper = remember {
        DatabaseHelper(DriverFactory().createDriver())
    }

    MaterialTheme {
        MainScreen(dbHelper = dbHelper, prefs = createDataStore())
    }
}