package com.droidcon.habitsync.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.droidcon.habitsync.presentation.screen.addedit.AddEditHabitScreen
import com.droidcon.habitsync.presentation.screen.habitdetail.HabitDetailScreen
import com.droidcon.habitsync.presentation.screen.home.HomeScreen
import com.droidcon.habitsync.domain.model.AddEditMode
import com.droidcon.habitsync.presentation.components.ThemeSelectorSheet
import com.droidcon.habitsync.presentation.screen.debug.DebugScreen


@Composable
fun HabitNavGraph(
    navController: NavHostController = rememberNavController()
) {

    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                onAdd = { navController.navigate(Screen.AddHabit.route) },
                onEdit = { navController.navigate("edit/$it") },
                onDetail = { navController.navigate("detail/$it") },
                onDebugClick = { navController.navigate(Screen.Debug.route) },
                onShowTheme = { navController.navigate(Screen.Theme.route) },
            )
        }

        composable(Screen.AddHabit.route) {
            AddEditHabitScreen(
                mode = AddEditMode.Add,
                onSaved = { navController.popBackStack() }
            )
        }

        composable(Screen.EditHabit.route) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId") ?: return@composable
            AddEditHabitScreen(
                mode = AddEditMode.Edit(habitId),
                onSaved = { navController.popBackStack() }
            )
        }

        composable(Screen.Detail.route) { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId") ?: return@composable
            HabitDetailScreen(habitId, onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Debug.route) {
            DebugScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Theme.route) {
            ThemeSelectorSheet(
                onDismiss = { navController.popBackStack() }
            )
        }
    }
}
