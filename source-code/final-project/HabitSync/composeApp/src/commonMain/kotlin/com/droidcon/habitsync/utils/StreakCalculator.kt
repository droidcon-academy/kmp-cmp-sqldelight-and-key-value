package com.droidcon.habitsync.utils

import kotlinx.datetime.*

/**
 * Represents a habit streak summary.
 *
 * @property currentStreak Number of consecutive days (up to today) with completion.
 * @property bestStreak Highest number of consecutive days completed in history.
 */
data class StreakInfo(
    val currentStreak: Int,
    val bestStreak: Int
)

/**
 * Calculates the current and best streaks based on a list of completed date strings.
 *
 * @param completedDateStrings A list of ISO date strings (e.g., "2024-05-13").
 * @return StreakInfo containing current and best streak counts.
 */
fun calculateStreaks(completedDateStrings: List<String>): StreakInfo {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    // Convert strings to LocalDate objects, ignoring parsing failures
    val completedDates = completedDateStrings
        .mapNotNull { runCatching { LocalDate.parse(it) }.getOrNull() }
        .toSet() // use Set for fast lookup

    var currentStreak = 0
    var bestStreak = 0
    var streak = 0

    // ✅ Calculate current streak by counting how many days back from today are in the set
    var day = today
    while (completedDates.contains(day)) {
        currentStreak++
        day = day.minus(1, DateTimeUnit.DAY)
    }

    // ✅ Calculate best streak by iterating through sorted dates
    val sortedDates = completedDates.sorted()
    var prev: LocalDate? = null
    for (date in sortedDates) {
        if (prev == null || prev.plus(1, DateTimeUnit.DAY) == date) {
            // Continue streak
            streak++
        } else {
            // Streak broken → update best and restart
            bestStreak = maxOf(bestStreak, streak)
            streak = 1
        }
        prev = date
    }

    // Final check in case the best streak is at the end
    bestStreak = maxOf(bestStreak, streak)

    return StreakInfo(currentStreak, bestStreak)
}
