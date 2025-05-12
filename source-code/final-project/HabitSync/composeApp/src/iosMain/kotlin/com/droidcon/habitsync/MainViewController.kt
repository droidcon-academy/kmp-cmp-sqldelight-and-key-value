package com.droidcon.habitsync

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.droidcon.habitsync.db.createDatabaseHelper
import com.droidcon.habitsync.db.createIosSqlDriver
import com.droidcon.habitsync.repository.HabitLogRepository
import com.droidcon.habitsync.repository.HabitRepository
import com.droidcon.habitsync.ui.home.MainHabitUI
import com.droidcon.habitsync.viewmodel.HabitViewModel

//fun MainViewController() = ComposeUIViewController {
//  //  App(createDataStore())
//}

fun MainViewController() = ComposeUIViewController {
    val dbHelper = createDatabaseHelper()
    val logRepo = HabitLogRepository(dbHelper)

    val habitRepository = HabitRepository(dbHelper)
    val habitLogRepository = HabitLogRepository(dbHelper)
    val habitViewModel = HabitViewModel(habitRepository, habitLogRepository)

    MainHabitUI(
        habitViewModel = habitViewModel,
        logRepo = logRepo,
        dbHelper = dbHelper
    )
}