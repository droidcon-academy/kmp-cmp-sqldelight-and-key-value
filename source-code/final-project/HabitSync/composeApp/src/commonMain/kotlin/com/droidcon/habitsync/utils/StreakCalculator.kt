package com.droidcon.habitsync.utils

import kotlinx.datetime.*

data class StreakInfo(
    val currentStreak: Int,
    val bestStreak: Int
)

fun calculateStreaks(completedDateStrings: List<String>): StreakInfo {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val completedDates = completedDateStrings
        .mapNotNull { runCatching { LocalDate.parse(it) }.getOrNull() }
        .toSet()

    var currentStreak = 0
    var bestStreak = 0
    var streak = 0

    var day = today
    while (completedDates.contains(day)) {
        currentStreak++
        day = day.minus(1, DateTimeUnit.DAY)
    }

    val sortedDates = completedDates.sorted()
    var prev: LocalDate? = null
    for (date in sortedDates) {
        if (prev == null || prev.plus(1, DateTimeUnit.DAY) == date) {
            streak++
        } else {
            bestStreak = maxOf(bestStreak, streak)
            streak = 1
        }
        prev = date
    }
    bestStreak = maxOf(bestStreak, streak)

    return StreakInfo(currentStreak, bestStreak)
}
