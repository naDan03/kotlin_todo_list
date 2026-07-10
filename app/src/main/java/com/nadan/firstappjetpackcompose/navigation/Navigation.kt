package com.nadan.firstappjetpackcompose.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
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
import com.nadan.firstappjetpackcompose.ui.theme.*
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
            CompletedScreen(
                viewModel = viewModel, 
                onLogout = onLogout,
                onNavigateToDetail = { todoId -> 
                    navController.navigate("todo_detail/$todoId") 
                },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.Stats.route) {
            StatsScreen(
                viewModel = viewModel, 
                onLogout = onLogout,
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
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
        modifier = modifier.fillMaxWidth(),
        color = AccentBlue,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            screens.forEach { screen ->
                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                val bgColor by animateColorAsState(
                    targetValue = if (selected) Color.White.copy(alpha = 0.15f) else Color.Transparent,
                    label = "bgColor"
                )
                val iconColor by animateColorAsState(
                    targetValue = if (selected) Color.White else Color.White.copy(alpha = 0.6f),
                    label = "iconColor"
                )

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(bgColor)
                        .clickable {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = screen.icon ?: Icons.Default.Assignment,
                        contentDescription = screen.title,
                        tint = iconColor,
                        modifier = Modifier.size(22.dp)
                    )
                    if (selected) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = screen.title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
