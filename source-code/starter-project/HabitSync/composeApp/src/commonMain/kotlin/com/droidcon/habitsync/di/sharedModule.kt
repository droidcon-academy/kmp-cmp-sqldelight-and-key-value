package com.droidcon.habitsync.di


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.droidcon.habitsync.DatabaseHelper
import org.koin.dsl.module



fun sharedModule(
    dbHelper: DatabaseHelper,
    prefs: DataStore<Preferences>
) = module {
    single { dbHelper }
    single { prefs }
}
