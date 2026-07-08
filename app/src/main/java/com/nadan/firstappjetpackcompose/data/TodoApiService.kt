package com.nadan.firstappjetpackcompose.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Interface Retrofit pour définir les appels API.
 * JSONPlaceholder est un service gratuit pour tester les appels réseau.
 */
interface TodoApiService {
    @GET("todos")
    suspend fun getTodos(): List<Todo>

    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

        fun create(): TodoApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TodoApiService::class.java)
        }
    }
}
