package com.droidcon.habitsync.utils

import kotlinx.datetime.*

/**
 * Converts an ISO 8601 timestamp string into a human-readable formatted date-time string.
 *
 * Example:
 * Input: "2025-05-13T06:30:00Z"
 * Output: "May 13, 2025 – 12:00 PM"
 *
 * @param isoString The ISO-8601 date-time string to format.
 * @return A formatted string like "May 13, 2025 – 12:00 PM", or the raw string if parsing fails.
 */
fun formatDateTimeKMP(isoString: String): String {
    return try {
        // Parse the ISO string into an Instant
        val instant = Instant.parse(isoString)

        // Convert to local date-time based on system's time zone
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        // Format date parts
        val month = dateTime.month.name.lowercase()
            .replaceFirstChar { it.uppercase() } // Capitalize
            .take(3) // Short form (e.g., "May")
        val day = dateTime.dayOfMonth
        val year = dateTime.year

        // Format time parts
        val hour24 = dateTime.hour
        val minute = dateTime.minute.toString().padStart(2, '0')
        val amPm = if (hour24 < 12) "AM" else "PM"
        val hour12 = if (hour24 % 12 == 0) 12 else hour24 % 12

        // Final formatted output
        "$month $day, $year – $hour12:$minute $amPm"
    } catch (e: Exception) {
        // In case parsing fails, return original input
        isoString
    }
}
