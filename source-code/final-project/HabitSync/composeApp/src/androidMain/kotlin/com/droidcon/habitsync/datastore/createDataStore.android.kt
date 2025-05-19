package com.droidcon.habitsync.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.droidcon.habitsync.data.datastore.DATA_STORE_FILE_NAME

fun createDataStore(context: Context): DataStore<Preferences> {
    // Returns a DataStore instance that uses the file path: /data/data/your.app.package/files/prefs.preferences_pb
    return com.droidcon.habitsync.data.datastore.createDataStore {
        context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    }
}
