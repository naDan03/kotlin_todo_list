package com.nadan.firstappjetpackcompose.data

/**
 * Le Repository est une couche qui gère la source de données.
 * Dans MVVM, le ViewModel ne sait pas si les données viennent d'Internet, d'une DB locale ou d'un cache.
 */
class TodoRepository(private val apiService: TodoApiService) {
    suspend fun getTodos(): List<Todo> = apiService.getTodos()
}
