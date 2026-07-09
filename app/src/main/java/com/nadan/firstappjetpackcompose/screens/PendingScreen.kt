package com.nadan.firstappjetpackcompose.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nadan.firstappjetpackcompose.data.Todo
import com.nadan.firstappjetpackcompose.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingScreen(
    viewModel: TodoViewModel,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val todos by viewModel.todos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val pendingTodos = remember(todos) { todos.filterNot { it.isCompleted } }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("À faire naDan (${pendingTodos.size})") },
                actions = {
                    IconButton(onClick = { viewModel.fetchTodos() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualiser")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Déconnexion")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter")
            }
        },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(10) { // On affiche 10 squelettes
                        TodoSkeletonItem()
                    }
                }
            } else if (error != null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(error ?: "Erreur inconnue", color = MaterialTheme.colorScheme.error)
                    Button(onClick = { viewModel.fetchTodos() }) {
                        Text("Réessayer")
                    }
                }
            } else if (pendingTodos.isEmpty()) {
                Text("Aucune tâche en cours !")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(pendingTodos, key = { it.id }) { todo ->
                        TodoItem(
                            todo = todo,
                            onToggle = { viewModel.toggleTodo(todo.id) },
                            onDelete = { viewModel.deleteTodo(todo.id) }
                        )
                    }
                }
            }
        }

        if (showAddDialog) {
            AddTodoDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { title ->
                    viewModel.addTodo(title)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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

@Composable
fun TodoSkeletonItem() {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alpha by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Placeholder pour la Checkbox
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
            // Placeholder pour le texte
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth(0.6f)
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}

@Composable
fun AddTodoDialog(
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    var title by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nouvelle tâche") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Titre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onAdd(title)
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Ajouter")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}
