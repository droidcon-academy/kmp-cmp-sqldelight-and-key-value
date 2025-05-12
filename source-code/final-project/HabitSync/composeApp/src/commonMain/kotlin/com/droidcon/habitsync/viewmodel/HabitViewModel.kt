package com.droidcon.habitsync.viewmodel

import com.droidcon.habitsync.db.Habit
import com.droidcon.habitsync.repository.HabitRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class HabitViewModel(private val repo: HabitRepository) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // For now, we'll just return all habits (filtering logic can be added later)
    val habits: StateFlow<List<Habit>> =
        repo.getAllHabits().stateIn(scope, SharingStarted.Eagerly, emptyList())

    val filteredHabits: StateFlow<List<Habit>> = habits
    private val _filter = MutableStateFlow(HabitFilter.ALL)
    val filter: StateFlow<HabitFilter> = _filter.asStateFlow()

    fun setFilter(value: HabitFilter) {
        _filter.value = value
    }


    fun deleteHabit(id: String) {
        scope.launch { repo.delete(id) }
    }

    fun addHabit(habit: Habit) {
        scope.launch { repo.insert(habit) }
    }

    fun getHabitById(id: String): Habit? = habits.value.find { it.id == id }

    fun addHabit(title: String, createdAt: String, reminderTime: String?) {
        val habit = Habit(
            id = generateUUID(),
            title = title,
            createdAt = createdAt,
            reminderTime = reminderTime,
            isArchived = 0L
        )
        scope.launch { repo.insert(habit) }
    }

    fun updateHabit(id: String, title: String, reminderTime: String?) {
        val updated = Habit(
            id = id,
            title = title,
            createdAt = getHabitById(id)?.createdAt ?: "", // fallback
            reminderTime = reminderTime,
            isArchived = 0L
        )
        scope.launch { repo.update(updated) }
    }

    fun generateUUID(): String {
        return List(32) {
            (('a'..'f') + ('0'..'9')).random()
        }.joinToString("")
    }
}
