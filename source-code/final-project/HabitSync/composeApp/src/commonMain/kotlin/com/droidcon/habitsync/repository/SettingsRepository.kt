package com.droidcon.habitsync.repository

import com.droidcon.habitsync.data.KeyValueStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SettingsRepository(private val storage: KeyValueStorage) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _themeMode = MutableStateFlow("system")
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _reminderTime = MutableStateFlow("08:00")
    val reminderTime: StateFlow<String> = _reminderTime.asStateFlow()

    init {
        scope.launch {
            _themeMode.value = storage.getString("theme_mode", "system")
            _reminderTime.value = storage.getString("reminder_time", "08:00")
        }
    }

    fun setThemeMode(mode: String) {
        _themeMode.value = mode
        scope.launch { storage.setString("theme_mode", mode) }
    }

    fun setReminderTime(time: String) {
        _reminderTime.value = time
        scope.launch { storage.setString("reminder_time", time) }
    }
}
