package com.droidcon.habitsync.domain.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.droidcon.habitsync.data.db.DatabaseHelper
import com.droidcon.habitsync.db.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Repository for managing Habit data access.
 * Uses SQLDelight queries and exposes reactive Flows for UI consumption.
 */
class HabitRepository(private val db: DatabaseHelper) {

    /**
     *  Returns a reactive stream of all habits.
     * This updates automatically when the table data changes.
     */
    fun getAllHabits(): Flow<List<Habit>> {
        val dummyHabits = listOf(
            Habit(id = "1", title = "Drink Water", createdAt = "2023-01-01", reminderTime = "08:00",0),
            Habit(id = "2", title = "Meditate", createdAt = "2023-01-02", reminderTime = "07:00",0),
            Habit(id = "3", title = "Read Book", createdAt = "2023-01-03", reminderTime = "21:00",0)
        )
        return flowOf(dummyHabits)
    }

    /**
     *  Returns a reactive stream of a specific habit by its ID.
     * Returns null if no habit is found.
     */
    fun getHabitById(id: String): Flow<Habit?> {
        val dummyHabit = Habit(
            id = id,
            title = "Dummy Habit",
            createdAt = "2023-01-01",
            reminderTime = "09:00",
            0
        )
        return flowOf(dummyHabit)
    }

    /**
     *  Inserts a new habit into the database.
     */
    suspend fun insert(habit: Habit) {

    }

    /**
     * ️ Updates an existing habit.
     * Matching is done by ID.
     */
    suspend fun update(habit: Habit) {

    }

    /**
     *  Deletes a habit by ID.
     */
    suspend fun delete(id: String) {
    }
}
