package com.droidcon.habitsync.viewmodel

import com.droidcon.habitsync.db.Habit
import com.droidcon.habitsync.repository.HabitLogRepository
import com.droidcon.habitsync.repository.HabitRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class HabitViewModel(
    private val repo: HabitRepository,
    private val logRepo: HabitLogRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _filter = MutableStateFlow(HabitFilter.ALL)
    val filter: StateFlow<HabitFilter> = _filter.asStateFlow()

    private val allHabits = repo.getAllHabits()
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    private val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()

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

    fun setFilter(newFilter: HabitFilter) {
        _filter.value = newFilter
    }

    fun deleteHabit(id: String) {
        scope.launch { repo.delete(id) }
    }

    fun getHabitById(id: String): Habit? = allHabits.value.find { it.id == id }

    fun addHabit(title: String, createdAt: String, reminderTime: String?) {
        val habit = Habit(
            id = generateUUID(),
            title = title,
            createdAt = createdAt,
            reminderTime = reminderTime,
            isArchived = 0L,
         //   notes = ""
        )
        scope.launch { repo.insert(habit) }
    }

    fun updateHabit(id: String, title: String, reminderTime: String?) {
        val updated = Habit(
            id = id,
            title = title,
            createdAt = getHabitById(id)?.createdAt ?: "",
            reminderTime = reminderTime,
            isArchived = 0L,
            //notes = ""
        )
        scope.launch { repo.update(updated) }
    }

    private fun generateUUID(): String {
        return List(32) {
            (('a'..'f') + ('0'..'9')).random()
        }.joinToString("")
    }
}
