package com.droidcon.habitsync.di

import com.droidcon.habitsync.db.createDatabaseHelper
import com.droidcon.habitsync.datastore.createDataStore
import com.droidcon.habitsync.ui.theme.ThemeManager
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Initializes Koin dependency injection for iOS.
 * This sets up platform-specific dependencies and combines them
 * with shared business logic modules (repositories, viewmodels, etc).
 */
fun initKoinIos() {
    // Create SQLDelight database instance using native driver
    val dbHelper = createDatabaseHelper()

    // iOS-specific dependency module
    val iosModule = module {
        // Provide the shared DB instance to be used by repositories
        single { dbHelper }

        // Provide platform-specific DataStore for preferences
        single { createDataStore() }

        // Provide ThemeManager using DataStore
        single { ThemeManager(get()) }
    }

    // Start Koin with both iOS-specific and shared modules
    startKoin {
        modules(
            iosModule,
            sharedModule(dbHelper) // Inject shared logic (repos, viewmodels) using the DB
        )
    }
}
