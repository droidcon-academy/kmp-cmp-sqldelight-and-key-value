package com.droidcon.habitsync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.droidcon.habitsync.db.createDatabaseHelper
import com.droidcon.habitsync.repository.HabitRepository
import com.droidcon.habitsync.ui.home.HomeScreen
import com.droidcon.habitsync.ui.home.MainHabitUI
import com.droidcon.habitsync.viewmodel.HabitViewModel



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = createDataStore(this)
        val dbHelper = createDatabaseHelper(this)
        val habitRepository = HabitRepository(dbHelper)
        val habitViewModel = HabitViewModel(habitRepository)

        setContent {
            MainHabitUI(viewModel = habitViewModel)
        }
//        setContent {
//            SettingsScreen(prefs = prefs)
//        }
    }
}
