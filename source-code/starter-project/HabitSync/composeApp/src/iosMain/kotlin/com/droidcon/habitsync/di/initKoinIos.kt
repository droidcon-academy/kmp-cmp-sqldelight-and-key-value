package com.droidcon.habitsync.di

import com.droidcon.habitsync.db.createDatabaseHelper
import com.droidcon.habitsync.datastore.createDataStore
import com.droidcon.habitsync.presentation.screen.theme.ThemeManager
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Initializes Koin dependency injection for iOS.
 * This sets up platform-specific dependencies and combines them
 * with shared business logic modules (repositories, viewmodels, etc).
 */
fun initKoinIos() {

    // iOS-specific dependency module
    val iosModule = module {
        // Provide the shared DB instance to be used by repositories
        single { createDatabaseHelper() }

        // Provide platform-specific DataStore for preferences
        single { createDataStore() }

        // Provide ThemeManager using DataStore

    }

    // Start Koin with both iOS-specific and shared modules
    startKoin {
        modules(
            iosModule,
            sharedModule() // Inject shared logic (repos, viewmodels) using the DB
        )
    }
}
