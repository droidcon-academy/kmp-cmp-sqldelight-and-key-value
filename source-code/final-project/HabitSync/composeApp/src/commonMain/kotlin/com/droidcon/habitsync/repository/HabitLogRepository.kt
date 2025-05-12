package com.droidcon.habitsync.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.droidcon.habitsync.db.DatabaseHelper
import com.droidcon.habitsync.db.HabitLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class HabitLogRepository(private val db: DatabaseHelper) {

    // ✅ Insert or update a log for the given day
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

    // ✅ Remove a log (used for undo or debugging)
    suspend fun deleteLog(habitId: String, date: String) {
        withContext(Dispatchers.IO) {
            db.db.habitLogQueries.deleteHabitLog(habitId, date)
        }
    }

    // ✅ Reactive stream of all logs for this habit
    fun getLogForHabit(habitId: String): Flow<List<HabitLog>> {
        return db.db.habitLogQueries
            .selectLogForHabit(habitId)
            .asFlow()
            .mapToList(Dispatchers.IO)
    }

    // ✅ Reactive stream of today's log (used for toggle checkbox)
    fun getLogForToday(habitId: String, date: String): Flow<HabitLog?> {
        return db.db.habitLogQueries
            .selectLogForToday(habitId, date)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }

    // ✅ Reactive stream of just the completed date strings (used for streaks + calendar)
    fun getCompletedDates(habitId: String): Flow<List<String>> {
        return db.db.habitLogQueries
            .selectCompletedDates(habitId)
            .asFlow()
            .mapToList(Dispatchers.IO)
    }
}
