package com.droidcon.habitsync.presentation.screen.home

import com.droidcon.habitsync.db.Habit
import com.droidcon.habitsync.domain.model.HabitFilter
import com.droidcon.habitsync.domain.repository.HabitLogRepository
import com.droidcon.habitsync.domain.repository.HabitRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

/**
 * ViewModel responsible for managing all Habit-related data and logic,
 * including filtering, adding, editing, and deleting habits.
 */
class HabitViewModel(
    private val repo: HabitRepository,
    private val logRepo: HabitLogRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // Currently selected filter type (All / Completed Today / Missed Today)
    private val _filter = MutableStateFlow(HabitFilter.ALL)
    val filter: StateFlow<HabitFilter> = _filter.asStateFlow()

    // Fetch all habits from repository as a reactive stream
    private val allHabits = repo.getAllHabits()
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    // Get today's date in YYYY-MM-DD format
    private val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()

    /**
     * Exposed state flow of habits filtered according to current filter selection.
     * - ALL: Returns all habits
     * - COMPLETED_TODAY: Only habits marked done today
     * - MISSED_TODAY: Habits not completed today
     */
    val filteredHabits: StateFlow<List<Habit>> = combine(allHabits, filter) { habits, filter ->
        when (filter) {
            HabitFilter.ALL -> habits
            HabitFilter.COMPLETED_TODAY -> {
                habits.filter { habit ->
                    val todayLog = logRepo.getLogForToday(habit.id, today).firstOrNull()
                    todayLog?.completed == 1L
                }
            }
            HabitFilter.MISSED_TODAY -> {
                habits.filter { habit ->
                    val todayLog = logRepo.getLogForToday(habit.id, today).firstOrNull()
                    todayLog == null || todayLog.completed == 0L
                }
            }
        }
    }.stateIn(scope, SharingStarted.Eagerly, emptyList())

    // Update the selected filter
    fun setFilter(newFilter: HabitFilter) {
        _filter.value = newFilter
    }

    // Delete a habit by ID
    fun deleteHabit(id: String) {
        scope.launch { repo.delete(id) }
    }

    // Return a habit by ID if it exists (used in edit mode)
    fun getHabitById(id: String): Habit? = allHabits.value.find { it.id == id }

    // Add a new habit
    fun addHabit(title: String, createdAt: String, reminderTime: String?) {
        val habit = Habit(
            id = generateUUID(),
            title = title,
            createdAt = createdAt,
            reminderTime = reminderTime,
            isArchived = 0L,
            // notes = "" // Optional: Uncomment if you use notes
        )
        scope.launch { repo.insert(habit) }
    }

    // Update an existing habit
    fun updateHabit(id: String, title: String, reminderTime: String?) {
        val updated = Habit(
            id = id,
            title = title,
            createdAt = getHabitById(id)?.createdAt ?: "",
            reminderTime = reminderTime,
            isArchived = 0L,
            // notes = "" // Optional: Uncomment if you use notes
        )
        scope.launch { repo.update(updated) }
    }

    // Utility to generate a random 32-character hex-based UUID
    private fun generateUUID(): String {
        return List(32) {
            (('a'..'f') + ('0'..'9')).random()
        }.joinToString("")
    }
}
