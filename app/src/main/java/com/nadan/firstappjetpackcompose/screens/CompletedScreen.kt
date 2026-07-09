package com.nadan.firstappjetpackcompose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nadan.firstappjetpackcompose.data.Todo
import com.nadan.firstappjetpackcompose.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletedScreen(
    viewModel: TodoViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val todos by viewModel.todos.collectAsState()
    val completedTodos = remember(todos) { todos.filter { it.isCompleted } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terminé") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Déconnexion")
                    }
                }
            )
        },
        modifier = modifier
    ) { paddingValues ->
        if (completedTodos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Aucune tâche terminée !")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(completedTodos, key = { it.id }) { todo ->
                    TodoItemCompleted(
                        todo = todo,
                        onToggle = { viewModel.toggleTodo(todo.id) },
                        onDelete = { viewModel.deleteTodo(todo.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun TodoItemCompleted(
    todo: Todo,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Checkbox(
                    checked = todo.isCompleted,
                    onCheckedChange = { onToggle() }
                )
                Column {
                    Text(
                        text = todo.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
