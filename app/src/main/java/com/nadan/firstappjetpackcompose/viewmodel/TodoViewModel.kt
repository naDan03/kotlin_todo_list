package com.nadan.firstappjetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadan.firstappjetpackcompose.data.Todo
import com.nadan.firstappjetpackcompose.data.TodoApiService
import com.nadan.firstappjetpackcompose.data.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Le ViewModel prépare les données pour l'UI et gère la logique métier.
 * Il survit aux changements de configuration (comme la rotation de l'écran).
 */
class TodoViewModel : ViewModel() {

    // On initialise le repository (Idéalement, on utiliserait l'injection de dépendances)
    private val repository = TodoRepository(TodoApiService.create())

    // État pour la liste des todos
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    // État pour le chargement
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // État pour les erreurs
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        // On charge les données dès le démarrage du ViewModel
        fetchTodos()
    }

    fun fetchTodos() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val fetchedTodos = repository.getTodos()
                _todos.value = fetchedTodos
            } catch (e: Exception) {
                _error.value = "Erreur lors de la récupération : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addTodo(title: String, description: String = "") {
        // Note: JSONPlaceholder ne sauvegarde pas réellement sur leur serveur
        val newTodo = Todo(
            userId = 1,
            id = (_todos.value.maxOfOrNull { it.id } ?: 0) + 1,
            title = title,
            isCompleted = false
        )
        _todos.value = listOf(newTodo) + _todos.value
    }

    fun toggleTodo(todoId: Int) {
        _todos.value = _todos.value.map { todo ->
            if (todo.id == todoId) {
                todo.copy(isCompleted = !todo.isCompleted)
            } else {
                todo
            }
        }
    }

    fun deleteTodo(todoId: Int) {
        _todos.value = _todos.value.filterNot { it.id == todoId }
    }

    fun getTodoById(id: Int): Todo? {
        return _todos.value.find { it.id == id }
    }
}
