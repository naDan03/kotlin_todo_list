package com.nadan.firstappjetpackcompose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nadan.firstappjetpackcompose.ui.components.MainHeader
import com.nadan.firstappjetpackcompose.ui.theme.*
import com.nadan.firstappjetpackcompose.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: TodoViewModel,
    onLogout: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val todos by viewModel.todos.collectAsState()
    val pendingCount = remember(todos) { todos.count { !it.isCompleted } }
    val completedCount = remember(todos) { todos.count { it.isCompleted } }
    val totalCount = remember(todos) { todos.size }
    val completionRate = remember(todos) {
        if (totalCount > 0) (completedCount.toFloat() / totalCount.toFloat()) else 0f
    }

    Scaffold(
        containerColor = Slate50,
        topBar = {
            MainHeader(
                title = "Analyses de",
                onProfileClick = onNavigateToSettings,
                onLogout = onLogout
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Statistiques",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = AccentBlue,
                    letterSpacing = (-1).sp
                ),
                modifier = Modifier.align(Alignment.Start).padding(vertical = 16.dp)
            )

            // Progression Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, Slate200, RoundedCornerShape(24.dp)),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Taux de complétion",
                        style = MaterialTheme.typography.labelLarge,
                        color = Slate500,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = { completionRate },
                            modifier = Modifier.size(120.dp),
                            strokeWidth = 10.dp,
                            color = AccentBlue,
                            trackColor = Slate100,
                            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                        Text(
                            text = "${(completionRate * 100).toInt()}%",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 28.sp,
                                color = Slate900
                            )
                        )
                    }
                }
            }

            // Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "À faire",
                    value = pendingCount.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Terminées",
                    value = completedCount.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            StatCard(
                title = "Total des tâches créées",
                value = totalCount.toString(),
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Slate200, RoundedCornerShape(20.dp)),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Black,
                    color = Slate900
                )
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = Slate500
            )
        }
    }
}
