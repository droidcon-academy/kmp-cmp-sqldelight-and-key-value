package com.droidcon.habitsync.utils

import kotlinx.datetime.*

import kotlinx.datetime.*

fun formatDateTimeKMP(isoString: String): String {
    return try {
        val instant = Instant.parse(isoString)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val month = dateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
        val day = dateTime.dayOfMonth
        val year = dateTime.year

        val hour24 = dateTime.hour
        val minute = dateTime.minute.toString().padStart(2, '0')
        val amPm = if (hour24 < 12) "AM" else "PM"
        val hour12 = if (hour24 % 12 == 0) 12 else hour24 % 12

        "$month $day, $year – $hour12:$minute $amPm"
    } catch (e: Exception) {
        isoString // fallback
    }
}

