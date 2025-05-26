package com.droidcon.habitsync.domain.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.droidcon.habitsync.data.db.DatabaseHelper
import com.droidcon.habitsync.db.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing Habit data access.
 * Uses SQLDelight queries and exposes reactive Flows for UI consumption.
 */
class HabitRepository(private val db: DatabaseHelper) {

    /**
     *  Returns a reactive stream of all habits.
     * This updates automatically when the table data changes.
     */
    fun getAllHabits(): Flow<List<Habit>> =
        db.db.habitQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)

    /**
     *  Returns a reactive stream of a specific habit by its ID.
     * Returns null if no habit is found.
     */
    fun getHabitById(id: String): Flow<Habit?> =
        db.db.habitQueries.selectById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)

    /**
     *  Inserts a new habit into the database.
     */
    suspend fun insert(habit: Habit) {
        db.db.habitQueries.insertHabit(
            id = habit.id,
            title = habit.title,
            createdAt = habit.createdAt,
            reminderTime = habit.reminderTime,
            isArchived = habit.isArchived
        )
    }

    /**
     * ️ Updates an existing habit.
     * Matching is done by ID.
     */
    suspend fun update(habit: Habit) {
        db.db.habitQueries.updateHabit(
            title = habit.title,
            reminderTime = habit.reminderTime,
            isArchived = habit.isArchived,
            id = habit.id
        )
    }

    /**
     *  Deletes a habit by ID.
     */
    suspend fun delete(id: String) {
        db.db.habitQueries.deleteHabit(id)
    }
}
