package com.droidcon.habitsync

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.droidcon.habitsync.datastore.createDataStore
import com.droidcon.habitsync.db.createDatabaseHelper
import com.droidcon.habitsync.repository.HabitLogRepository
import com.droidcon.habitsync.repository.HabitRepository
import com.droidcon.habitsync.ui.home.MainHabitUI
import com.droidcon.habitsync.ui.theme.ThemeManager
import com.droidcon.habitsync.ui.theme.AppTheme
import com.droidcon.habitsync.viewmodel.HabitViewModel

// Entry point for iOS app using Compose Multiplatform
fun MainViewController() = ComposeUIViewController {
    // Create a key-value DataStore instance for iOS using KMP shared logic
    val prefs = remember { createDataStore() }

    // Manage the theme (light, dark, system) based on user preference stored in prefs
    val themeManager = remember { ThemeManager(prefs) }

    // Create the SQLDelight database helper instance for managing DB operations
    val dbHelper = remember { createDatabaseHelper() }

    // Set up repositories to handle business logic (habits and logs)
    val habitRepository = remember { HabitRepository(dbHelper) }
    val habitLogRepository = remember { HabitLogRepository(dbHelper) }

    // ViewModel that contains UI logic and communicates with repositories
    val habitViewModel = remember { HabitViewModel(habitRepository, habitLogRepository) }

    // Apply current theme and show the main UI for habit tracking
    AppTheme(themeManager) {
        MainHabitUI(
            habitViewModel = habitViewModel,
            logRepo = habitLogRepository,
            dbHelper = dbHelper,
            themeManager = themeManager
        )
    }
}
