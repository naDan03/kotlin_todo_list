package com.nadan.firstappjetpackcompose.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.nadan.firstappjetpackcompose.screens.*
import com.nadan.firstappjetpackcompose.viewmodel.TodoViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Login : Screen("login", "Connexion")
    object Pending : Screen("pending", "À faire", Icons.Default.Assignment)
    object Completed : Screen("completed", "Terminé", Icons.Default.CheckCircle)
    object Stats : Screen("stats", "Statistiques", Icons.Default.ShowChart)
    object TodoDetail : Screen("todo_detail/{todoId}", "Détails")
    object Settings : Screen("settings", "Paramètres")
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
            PendingScreen(
                viewModel = viewModel, 
                onLogout = onLogout,
                onNavigateToDetail = { todoId -> 
                    navController.navigate("todo_detail/$todoId") 
                },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.Completed.route) {
            CompletedScreen(viewModel = viewModel, onLogout = onLogout)
        }
        composable(Screen.Stats.route) {
            StatsScreen(viewModel = viewModel, onLogout = onLogout)
        }
        composable(
            route = Screen.TodoDetail.route,
            arguments = listOf(navArgument("todoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getInt("todoId") ?: 0
            TodoDetailScreen(
                todoId = todoId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}

@Composable
fun TodoBottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val screens = listOf(Screen.Pending, Screen.Completed, Screen.Stats)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(24.dp) // Barre flottante très moderne
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            screens.forEach { screen ->
                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                
                // Animation de couleur et de taille
                val contentColor by animateColorAsState(
                    targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    label = "color"
                )
                val scale by animateFloatAsState(
                    targetValue = if (selected) 1.1f else 1f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "scale"
                )

                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.scale(scale)
                    ) {
                        screen.icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                                tint = contentColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        if (selected) {
                            Text(
                                text = screen.title,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = contentColor,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
