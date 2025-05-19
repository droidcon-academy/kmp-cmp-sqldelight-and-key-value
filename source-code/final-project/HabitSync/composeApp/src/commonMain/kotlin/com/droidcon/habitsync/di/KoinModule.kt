package com.droidcon.habitsync.di

import com.droidcon.habitsync.data.db.DatabaseHelper
import com.droidcon.habitsync.domain.repository.HabitLogRepository
import com.droidcon.habitsync.domain.repository.HabitRepository
import com.droidcon.habitsync.presentation.screen.theme.ThemeManager
import org.koin.dsl.module

/**
 * Shared Koin module used by both Android and iOS platforms.
 * It registers common singletons such as the database helper,
 * repositories, and the shared HabitViewModel.
 */
fun sharedModule() = module {
    // Provide the habit data repository
    single { HabitRepository(get()) }

    // Provide the habit log (history) repository
    single { HabitLogRepository(get()) }
    single { ThemeManager(get()) }
}

