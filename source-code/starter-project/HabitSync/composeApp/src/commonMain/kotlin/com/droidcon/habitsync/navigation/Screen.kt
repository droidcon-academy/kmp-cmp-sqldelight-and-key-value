package com.droidcon.habitsync.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddHabit : Screen("add")
    object EditHabit : Screen("edit/{habitId}")
    object Detail : Screen("detail/{habitId}")
    object Debug : Screen("debug")
    object Theme : Screen("theme")
}