package com.droidcon.habitsync.di

import android.content.Context
import com.droidcon.habitsync.datastore.createDataStore
import com.droidcon.habitsync.db.createDatabaseHelper
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

    }

    // Start Koin with both iOS-specific and shared modules
    startKoin {
        modules(
            androidModule,
            sharedModule()
        )
    }
}
