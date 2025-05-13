package com.droidcon.habitsync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.droidcon.habitsync.datastore.createDataStore
import com.droidcon.habitsync.db.DatabaseHelper
import com.droidcon.habitsync.db.createDatabaseHelper
import com.droidcon.habitsync.repository.HabitLogRepository
import com.droidcon.habitsync.repository.HabitRepository
import com.droidcon.habitsync.ui.home.MainHabitUI
import com.droidcon.habitsync.ui.theme.AppTheme
import com.droidcon.habitsync.ui.theme.ThemeManager
import com.droidcon.habitsync.viewmodel.HabitViewModel

class MainActivity : ComponentActivity() {

    // Create DB once, early
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SQLDelight database
        dbHelper = createDatabaseHelper(this)

        // Create DataStore instance
        val prefs = createDataStore(this)

        // Set Compose content
        setContent {
            App(prefs)
        }
    }

    @Composable
    fun App(prefs: DataStore<Preferences>) {
        val themeManager = remember { ThemeManager(prefs) }

        AppTheme(themeManager = themeManager) {
            // Inject dependencies manually
            val habitRepository = remember { HabitRepository(dbHelper) }
            val habitLogRepository = remember { HabitLogRepository(dbHelper) }
            val habitViewModel = remember { HabitViewModel(habitRepository, habitLogRepository) }

            // Render main UI
            MainHabitUI(
                habitViewModel = habitViewModel,
                logRepo = habitLogRepository,
                dbHelper = dbHelper,
                themeManager = themeManager
            )
        }
    }
}
