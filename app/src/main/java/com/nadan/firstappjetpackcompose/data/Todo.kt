package com.nadan.firstappjetpackcompose.data

import com.google.gson.annotations.SerializedName

/**
 * Représente une tâche (Todo) récupérée de JSONPlaceholder.
 *
 * @param userId L'identifiant de l'utilisateur qui possède la tâche.
 * @param id L'identifiant unique de la tâche.
 * @param title Le titre de la tâche.
 * @param completed Si la tâche est terminée ou non.
 */
data class Todo(
    val userId: Int,
    val id: Int,
    val title: String,
    @SerializedName("completed")
    val isCompleted: Boolean
)
