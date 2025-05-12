package com.droidcon.habitsync.keys

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
 val THEME_MODE_KEY: Preferences.Key<String> = stringPreferencesKey("theme_mode")
 val REMINDER_TIME_KEY: Preferences.Key<String> = stringPreferencesKey("reminder_time")
 val FILTER_KEY: Preferences.Key<String> = stringPreferencesKey("filter")
