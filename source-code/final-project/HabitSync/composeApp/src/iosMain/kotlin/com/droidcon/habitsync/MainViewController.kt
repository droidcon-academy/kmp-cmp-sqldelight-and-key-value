package com.droidcon.habitsync

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.droidcon.habitsync.datastore.createDataStore
import com.droidcon.habitsync.db.DatabaseHelper
import com.droidcon.habitsync.db.createDatabaseHelper
import com.droidcon.habitsync.di.initKoinIos
import com.droidcon.habitsync.repository.HabitLogRepository
import com.droidcon.habitsync.repository.HabitRepository
import com.droidcon.habitsync.ui.home.MainHabitUI
import com.droidcon.habitsync.ui.theme.ThemeManager
import com.droidcon.habitsync.ui.theme.AppTheme
import com.droidcon.habitsync.viewmodel.HabitViewModel
import org.koin.mp.KoinPlatform.getKoin

// Entry point for the iOS app using Compose Multiplatform UI
fun MainViewController() = ComposeUIViewController {
    // Initialize Koin for iOS platform
    initKoinIos()

    // Retrieve dependencies from the Koin container
    val themeManager: ThemeManager = getKoin().get()
    val habitViewModel: HabitViewModel = getKoin().get()
    val dbHelper: DatabaseHelper = getKoin().get()
    val logRepo: HabitLogRepository = getKoin().get()

    // Apply app-wide theming
    AppTheme(themeManager) {
        // Render the main Habit UI
        MainHabitUI(
            habitViewModel = habitViewModel,
            logRepo = logRepo,
            dbHelper = dbHelper,
            themeManager = themeManager
        )
    }
}
