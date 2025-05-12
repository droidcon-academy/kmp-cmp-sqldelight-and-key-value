package com.droidcon.habitsync.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.droidcon.habitsync.db.DatabaseHelper
import com.droidcon.habitsync.db.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

class HabitRepository(private val db: DatabaseHelper) {

    fun getAllHabits(): Flow<List<Habit>> =
        db.db.habitQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)

    fun getHabitById(id: String): Flow<Habit?> =
        db.db.habitQueries.selectById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)

    suspend fun insert(habit: Habit) {
        db.db.habitQueries.insertHabit(
            id = habit.id,
            title = habit.title,
            createdAt = habit.createdAt,
            reminderTime = habit.reminderTime,
            isArchived = habit.isArchived
        )
    }

    suspend fun update(habit: Habit) {
        db.db.habitQueries.updateHabit(
            title = habit.title,
            reminderTime = habit.reminderTime,
            isArchived = habit.isArchived,
            id = habit.id
        )
    }

    suspend fun delete(id: String) {
        db.db.habitQueries.deleteHabit(id)
    }
}
