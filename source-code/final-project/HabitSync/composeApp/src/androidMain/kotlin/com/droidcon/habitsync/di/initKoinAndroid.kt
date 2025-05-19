package com.droidcon.habitsync.di

import android.content.Context
import com.droidcon.habitsync.data.datastore.createDataStore
import com.droidcon.habitsync.datastore.createDataStore
import com.droidcon.habitsync.db.createDatabaseHelper
import com.droidcon.habitsync.domain.repository.HabitLogRepository
import com.droidcon.habitsync.domain.repository.HabitRepository
import com.droidcon.habitsync.presentation.screen.theme.ThemeManager
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Initializes Koin dependency injection for the Android platform.
 * This module provides all platform-specific and shared dependencies
 * needed throughout the app.
 */
fun initKoinAndroid(appContext: Context) {
    // Android-specific dependency module
    val androidModule = module {
        // Provides a SQLDelight database helper using Android driver
        single { createDatabaseHelper(appContext) }

        // Provides a DataStore instance for saving preferences
        single { createDataStore(appContext) }

        // Provides a ThemeManager that reads/writes theme preference from DataStore
        single { ThemeManager(get()) }

        // Repository to access habit records
        single { HabitRepository(get()) }

        // Repository to access habit logs (completion status)
        single { HabitLogRepository(get()) }

    }

    // Start Koin with the declared modules
    startKoin {
        modules(androidModule)
    }
}
