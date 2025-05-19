package com.droidcon.habitsync.domain.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.droidcon.habitsync.data.db.DatabaseHelper
import com.droidcon.habitsync.db.HabitLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Repository to manage habit log data access and manipulation.
 * Handles all read/write operations for HabitLog table.
 */
class HabitLogRepository(private val db: DatabaseHelper) {

    /**
     * Inserts a new log or updates the existing log for the given habit on the given date.
     * - If no record exists, inserts a new one.
     * - If record exists, updates its completed status.
     */
    suspend fun upsertLog(habitId: String, date: String, completed: Boolean) {
        withContext(Dispatchers.IO) {
            val existing = db.db.habitLogQueries
                .selectLogForToday(habitId, date)
                .executeAsOneOrNull()

            if (existing == null) {
                db.db.habitLogQueries.insertHabitLog(
                    habitId = habitId,
                    date = date,
                    completed = if (completed) 1L else 0L
                )
            } else {
                db.db.habitLogQueries.updateHabitLog(
                    completed = if (completed) 1L else 0L,
                    habitId = habitId,
                    date = date
                )
            }
        }
    }

    /**
     * Deletes a log entry for the given habit and date.
     * Useful for debug tools or undo feature.
     */
    suspend fun deleteLog(habitId: String, date: String) {
        withContext(Dispatchers.IO) {
            db.db.habitLogQueries.deleteHabitLog(habitId, date)
        }
    }

    /**
     * Emits a stream of all logs for the given habit.
     * Used to show full history or debugging logs.
     */
    fun getLogForHabit(habitId: String): Flow<List<HabitLog>> {
        return db.db.habitLogQueries
            .selectLogForHabit(habitId)
            .asFlow()
            .mapToList(Dispatchers.IO)
    }

    /**
     * Emits the log entry for today (if any) for the given habit.
     * Used to check and toggle today’s completion status.
     */
    fun getLogForToday(habitId: String, date: String): Flow<HabitLog?> {
        return db.db.habitLogQueries
            .selectLogForToday(habitId, date)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }

    /**
     * Emits a list of completed date strings for the given habit.
     * Used to calculate streaks and fill calendar UI.
     */
    fun getCompletedDates(habitId: String): Flow<List<String>> {
        return db.db.habitLogQueries
            .selectCompletedDates(habitId)
            .asFlow()
            .mapToList(Dispatchers.IO)
    }
}
