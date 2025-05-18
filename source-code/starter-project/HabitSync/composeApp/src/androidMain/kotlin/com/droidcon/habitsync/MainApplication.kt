package com.droidcon.habitsync

import android.app.Application
import com.droidcon.habitsync.di.sharedModule
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val dbHelper = DatabaseHelper(DriverFactory(applicationContext).createDriver())
        val prefs = createDataStore(applicationContext)
        startKoin {
            modules(sharedModule(dbHelper, prefs))
        }
    }
}
