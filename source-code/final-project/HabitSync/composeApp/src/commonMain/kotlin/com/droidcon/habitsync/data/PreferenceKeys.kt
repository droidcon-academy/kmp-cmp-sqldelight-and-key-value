package com.droidcon.habitsync.data

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val THEME_MODE = stringPreferencesKey("theme_mode") // "light", "dark", "system"
    val REMINDER_TIME = stringPreferencesKey("reminder_time") // "08:00"
}
