package com.droidcon.habitsync.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

// This function creates a cross-platform (Android + iOS) DataStore instance
// by using a file path provided by the caller.
// On Android, the path typically comes from context.filesDir
// On iOS, it comes from NSFileManager.documentDirectory
fun createDataStore(producePath: () -> String): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            // Convert the path string to an Okio Path, required by DataStore
            producePath().toPath()
        }
    )
}

// The file name used to store the preferences in the file system.
// This file will be stored at the path given by `producePath()`.
internal const val DATA_STORE_FILE_NAME = "prefs.preferences_pb"
