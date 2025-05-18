package com.droidcon.habitsync.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.droidcon.habitsync.db.DatabaseHelper
import org.koin.compose.getKoin

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { navController.navigate(Routes.DataStore) }) {
                    Text("DataStore Demo")
                }
                Button(onClick = { navController.navigate(Routes.SqlDelight) }) {
                    Text("User CRUD (SQLDelight)")
                }
            }
        }
    ) { padding ->
        NavGraph(navController, Modifier.padding(padding))
    }
}

object Routes {
    const val DataStore = "datastore"
    const val SqlDelight = "sqldelight"
}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.DataStore,
        modifier = modifier
    ) {
        composable(Routes.DataStore) {
            DataStoreScreen()
        }
        composable(Routes.SqlDelight) {
            SqlDelightUserScreen()
        }
    }
}
