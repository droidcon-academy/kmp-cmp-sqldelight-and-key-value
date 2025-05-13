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

fun MainViewController() = ComposeUIViewController {
    val prefs = remember { createDataStore() }
    val themeManager = remember { ThemeManager(prefs) }
    val dbHelper = remember { createDatabaseHelper() }

    val habitRepository = remember { HabitRepository(dbHelper) }
    val habitLogRepository = remember { HabitLogRepository(dbHelper) }
    val habitViewModel = remember { HabitViewModel(habitRepository, habitLogRepository) }
    AppTheme(themeManager) {
        MainHabitUI(
            habitViewModel = habitViewModel,
            logRepo = habitLogRepository,
            dbHelper = dbHelper,
            themeManager = themeManager
        )
    }
}

