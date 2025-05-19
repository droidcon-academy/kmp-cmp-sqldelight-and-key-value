package com.droidcon.habitsync

import androidx.compose.ui.window.ComposeUIViewController
import com.droidcon.habitsync.data.datastore.db.DatabaseHelper
import com.droidcon.habitsync.di.initKoinIos
import com.droidcon.habitsync.domain.repository.HabitLogRepository
import com.droidcon.habitsync.presentation.screen.home.MainHabitUI
import com.droidcon.habitsync.presentation.screen.theme.ThemeManager
import com.droidcon.habitsync.presentation.screen.theme.AppTheme
import com.droidcon.habitsync.presentation.screen.home.HabitViewModel
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
