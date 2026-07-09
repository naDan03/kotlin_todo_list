package com.nadan.firstappjetpackcompose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nadan.firstappjetpackcompose.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: TodoViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val todos by viewModel.todos.collectAsState()
    val pendingCount = remember(todos) { todos.filterNot { it.isCompleted }.size }
    val completedCount = remember(todos) { todos.filter { it.isCompleted }.size }
    val totalCount = remember(todos) { todos.size }
    val completionRate = remember(todos) {
        if (totalCount > 0) (completedCount * 100) / totalCount else 0
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistiques") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Déconnexion")
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatsCard(
                title = "Total",
                value = totalCount.toString(),
                description = "tâches au total",
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatsCard(
                    title = "À faire",
                    value = pendingCount.toString(),
                    description = "tâches en cours",
                    modifier = Modifier.weight(1f)
                )
                StatsCard(
                    title = "Terminé",
                    value = completedCount.toString(),
                    description = "tâches faites",
                    modifier = Modifier.weight(1f),
                    highlight = true
                )
            }

            if (totalCount > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Progression",
                            style = MaterialTheme.typography.titleMedium
                        )
                        LinearProgressIndicator(
                            progress = { completionRate / 100f },
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "$completionRate% complété",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (totalCount == 0) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Commencez à ajouter des tâches !")
                }
            }
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    value: String,
    description: String,
    modifier: Modifier = Modifier,
    highlight: Boolean = false
) {
    Card(
        modifier = modifier,
        colors = if (highlight) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        } else {
            CardDefaults.cardColors()
        },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
