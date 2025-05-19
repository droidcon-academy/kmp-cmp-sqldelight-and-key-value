package com.droidcon.habitsync

import androidx.compose.runtime.Composable
import com.droidcon.habitsync.navigation.HabitNavGraph
import com.droidcon.habitsync.presentation.screen.theme.AppTheme

@Composable
fun App() {
    AppTheme() {
        HabitNavGraph()
    }
}