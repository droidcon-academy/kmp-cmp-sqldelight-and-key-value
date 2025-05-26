@file:OptIn(ExperimentalForeignApi::class)

package com.droidcon.habitsync.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.droidcon.habitsync.data.datastore.DATA_STORE_FILE_NAME
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun createDataStore(): DataStore<Preferences> {
    return com.droidcon.habitsync.data.datastore.createDataStore {
        // Get the path to the iOS app's Documents directory
        val directory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,       // Standard documents directory
            inDomain = NSUserDomainMask,           // Scope: user's home directory
            appropriateForURL = null,              // No specific URL context
            create = false,                        // Don't create if missing
            error = null                           // Not handling errors explicitly
        )

        // Return the full path to the preferences file
        requireNotNull(directory).path + "/$DATA_STORE_FILE_NAME"
    }
}
