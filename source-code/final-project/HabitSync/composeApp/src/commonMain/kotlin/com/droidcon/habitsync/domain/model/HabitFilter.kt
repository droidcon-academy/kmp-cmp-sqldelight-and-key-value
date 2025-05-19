package com.droidcon.habitsync.domain.model

/**
 * Enum class representing the different filters
 * available to view habits in the Home screen.
 *
 * @param displayName The human-readable name for each filter option.
 */
enum class HabitFilter(val displayName: String) {
    /**
     * Show all habits regardless of their status.
     */
    ALL("All"),

    /**
     * Show only habits that have been completed today.
     */
    COMPLETED_TODAY("Completed Today"),

    /**
     * Show only habits that were missed today (not completed).
     */
    MISSED_TODAY("Missed Today")
}
