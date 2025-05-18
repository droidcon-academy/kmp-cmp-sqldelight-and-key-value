package com.droidcon.habitsync.application

import android.app.Application
import com.droidcon.habitsync.db.DatabaseHelper
import com.droidcon.habitsync.db.DriverFactory
import com.droidcon.habitsync.di.sharedModule
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val dbHelper = DatabaseHelper(DriverFactory(applicationContext).createDriver())
        val prefs = com.droidcon.habitsync.datastore.createDataStore(applicationContext)
        startKoin {
            modules(sharedModule(dbHelper, prefs))
        }
    }
}
