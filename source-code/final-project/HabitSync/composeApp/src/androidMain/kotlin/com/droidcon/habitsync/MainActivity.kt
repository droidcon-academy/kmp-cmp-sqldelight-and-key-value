package com.droidcon.habitsync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.droidcon.habitsync.di.initKoinAndroid
import com.droidcon.habitsync.navigation.HabitNavGraph
import com.droidcon.habitsync.ui.theme.AppTheme
import com.droidcon.habitsync.ui.theme.ThemeManager
import com.droidcon.habitsync.viewmodel.HabitViewModel
import org.koin.compose.getKoin

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Koin dependency injection with Android context
        initKoinAndroid(applicationContext)

        // Set the root Composable content
        setContent {
            App()
        }
    }

    @Composable
    fun App() {
        // Retrieve ThemeManager from Koin container
        val themeManager = getKoin().get<ThemeManager>()

        // Apply app-wide theme
        AppTheme(themeManager = themeManager) {
            // Retrieve other dependencies from Koin
            val habitViewModel = getKoin().get<HabitViewModel>()
            val logRepo = getKoin().get<com.droidcon.habitsync.repository.HabitLogRepository>()
            val dbHelper = getKoin().get<com.droidcon.habitsync.db.DatabaseHelper>()

            HabitNavGraph(
                habitViewModel =habitViewModel,
                logRepo = logRepo,
                dbHelper = dbHelper,
                themeManager = themeManager
            )
        }
    }
}
