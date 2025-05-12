package com.droidcon.habitsync

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.droidcon.habitsync.db.createDatabaseHelper
import com.droidcon.habitsync.repository.HabitRepository
import com.droidcon.habitsync.ui.home.MainHabitUI
import com.droidcon.habitsync.viewmodel.HabitViewModel

//fun MainViewController() = ComposeUIViewController {
//  //  App(createDataStore())
//}

fun MainViewController() = ComposeUIViewController {
    val dbHelper = createDatabaseHelper()
//    val repository = HabitRepository(dbHelper)
//    val viewModel = HabitViewModel(repository)
//
//    MainHabitUI(viewModel = viewModel)
}