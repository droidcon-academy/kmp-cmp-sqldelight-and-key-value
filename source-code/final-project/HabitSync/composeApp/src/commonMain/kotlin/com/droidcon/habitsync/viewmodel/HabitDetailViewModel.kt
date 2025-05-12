package com.droidcon.habitsync.viewmodel

import com.droidcon.habitsync.repository.HabitLogRepository
import com.droidcon.habitsync.utils.StreakInfo
import com.droidcon.habitsync.utils.calculateStreaks
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class HabitDetailViewModel(
    private val habitId: String,
    private val logRepo: HabitLogRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val today: String = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()

    // Emits all completed date strings for this habit
    val completedDates: StateFlow<List<String>> = logRepo
        .getCompletedDates(habitId)
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    // Emits whether today is completed or not
    val isCompletedToday: StateFlow<Boolean> = logRepo
        .getLogForToday(habitId, today)
        .map { it?.completed == 1L }
        .stateIn(scope, SharingStarted.Eagerly, false)

    // Emits streak info based on completed dates
    val streakInfo: StateFlow<StreakInfo> = completedDates
        .map { calculateStreaks(it) }
        .stateIn(scope, SharingStarted.Eagerly, StreakInfo(0, 0))

    // Toggle today's log (insert or update)
    fun toggleTodayCompletion() {
        scope.launch {
            val currentlyCompleted = isCompletedToday.value
            logRepo.upsertLog(habitId, today, !currentlyCompleted)
        }
    }
}
