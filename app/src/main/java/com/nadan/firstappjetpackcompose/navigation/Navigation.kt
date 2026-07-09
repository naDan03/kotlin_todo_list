package com.nadan.firstappjetpackcompose.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nadan.firstappjetpackcompose.screens.CompletedScreen
import com.nadan.firstappjetpackcompose.screens.PendingScreen
import com.nadan.firstappjetpackcompose.screens.StatsScreen
import com.nadan.firstappjetpackcompose.viewmodel.TodoViewModel

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector? = null) {
    object Login : Screen("login", "Connexion")
    object Pending : Screen("pending", "À faire", Icons.Default.Assignment)
    object Completed : Screen("completed", "Terminé", Icons.Default.CheckCircle)
    object Stats : Screen("stats", "Statistiques", Icons.Default.ShowChart)
}

@Composable
fun TodoNavHost(
    navController: NavHostController,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TodoViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Pending.route,
        modifier = modifier
    ) {
        composable(Screen.Pending.route) {
            PendingScreen(viewModel = viewModel, onLogout = onLogout)
        }
        composable(Screen.Completed.route) {
            CompletedScreen(viewModel = viewModel, onLogout = onLogout)
        }
        composable(Screen.Stats.route) {
            StatsScreen(viewModel = viewModel, onLogout = onLogout)
        }
    }
}

@Composable
fun TodoBottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // On n'affiche que les écrans qui ont une icône (donc pas le Login)
    val screens = listOf(Screen.Pending, Screen.Completed, Screen.Stats)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = modifier
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = { 
                    screen.icon?.let { 
                        Icon(it, contentDescription = screen.title) 
                    }
                },
                label = { Text(screen.title) },
                selected = currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
