package com.droidcon.habitsync.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createDataStore(context: Context): DataStore<Preferences> {
    // Returns a DataStore instance that uses the file path: /data/data/your.app.package/files/prefs.preferences_pb
    return createDataStore {
        context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    }
}
