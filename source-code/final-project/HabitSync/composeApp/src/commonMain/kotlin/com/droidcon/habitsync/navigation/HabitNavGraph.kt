package com.droidcon.habitsync.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.droidcon.habitsync.repository.HabitLogRepository
import com.droidcon.habitsync.ui.add_edit.AddEditHabitScreen
import com.droidcon.habitsync.ui.debug.DebugScreen
import com.droidcon.habitsync.ui.habit_detail.HabitDetailScreen
import com.droidcon.habitsync.ui.home.HomeScreen
import com.droidcon.habitsync.ui.home.ThemeSelectorSheet
import com.droidcon.habitsync.viewmodel.AddEditMode
import com.droidcon.habitsync.viewmodel.HabitDetailViewModel
import com.droidcon.habitsync.viewmodel.HabitViewModel
import com.droidcon.habitsync.ui.theme.ThemeManager

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddHabit : Screen("add")
    object EditHabit : Screen("edit/{habitId}")
    object Detail : Screen("detail/{habitId}")
    object Debug : Screen("debug")
    object Theme : Screen("theme")
}

@Composable
fun HabitNavGraph(
    habitViewModel: HabitViewModel,
    logRepo: HabitLogRepository,
    dbHelper: com.droidcon.habitsync.db.DatabaseHelper,
    themeManager: ThemeManager,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = habitViewModel,
                onAdd = { navController.navigate(Screen.AddHabit.route) },
                onEdit = { navController.navigate("edit/$it") },
                onDetail = { navController.navigate("detail/$it") },
                onDebugClick = { navController.navigate(Screen.Debug.route) },
                onShowTheme = { navController.navigate(Screen.Theme.route) },
                themeManager = themeManager
            )
        }

        composable(Screen.AddHabit.route) {
            AddEditHabitScreen(
                viewModel = habitViewModel,
                mode = AddEditMode.Add,
                onSaved = { navController.popBackStack() }
            )
        }

        composable("edit/{habitId}") { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId") ?: return@composable
            AddEditHabitScreen(
                viewModel = habitViewModel,
                mode = AddEditMode.Edit(habitId),
                onSaved = { navController.popBackStack() }
            )
        }

        composable("detail/{habitId}") { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId") ?: return@composable
            val detailViewModel = HabitDetailViewModel(habitId, logRepo)
            HabitDetailScreen(
                viewModel = detailViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Debug.route) {
            DebugScreen(onBack = { navController.popBackStack() }, db = dbHelper)
        }

        composable(Screen.Theme.route) {
            ThemeSelectorSheet(
                themeManager = themeManager,
                onDismiss = { navController.popBackStack() }
            )
        }
    }
}
