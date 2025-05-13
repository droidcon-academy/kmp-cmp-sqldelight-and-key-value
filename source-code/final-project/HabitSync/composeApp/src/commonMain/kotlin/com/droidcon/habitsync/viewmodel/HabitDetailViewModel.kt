package com.droidcon.habitsync.viewmodel

import com.droidcon.habitsync.repository.HabitLogRepository
import com.droidcon.habitsync.utils.StreakInfo
import com.droidcon.habitsync.utils.calculateStreaks
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

/**
 * ViewModel for the Habit Detail Screen.
 * It handles completed dates, today's status, and streak calculations for a given habit.
 *
 * @param habitId ID of the habit being observed
 * @param logRepo Repository to fetch and update habit log data
 */
class HabitDetailViewModel(
    private val habitId: String,
    private val logRepo: HabitLogRepository
) {
    // Coroutine scope tied to this ViewModel instance
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // Current date in YYYY-MM-DD format (based on user's timezone)
    private val today: String = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()

    /**
     * Emits a list of completed date strings (used for calendar view and streak logic).
     */
    val completedDates: StateFlow<List<String>> = logRepo
        .getCompletedDates(habitId)
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    /**
     * Emits a boolean indicating whether the habit is marked as completed today.
     * Used to update the UI checkbox state.
     */
    val isCompletedToday: StateFlow<Boolean> = logRepo
        .getLogForToday(habitId, today)
        .map { it?.completed == 1L }
        .stateIn(scope, SharingStarted.Eagerly, false)

    /**
     * Emits the calculated streak information.
     * This includes current streak and best streak.
     */
    val streakInfo: StateFlow<StreakInfo> = completedDates
        .map { calculateStreaks(it) }
        .stateIn(scope, SharingStarted.Eagerly, StreakInfo(0, 0))

    /**
     * Toggles the completion status for today:
     * - If already completed, uncheck (delete or update to 0).
     * - If not completed, mark it as completed.
     */
    fun toggleTodayCompletion() {
        scope.launch {
            val currentlyCompleted = isCompletedToday.value
            logRepo.upsertLog(habitId, today, !currentlyCompleted)
        }
    }
}
