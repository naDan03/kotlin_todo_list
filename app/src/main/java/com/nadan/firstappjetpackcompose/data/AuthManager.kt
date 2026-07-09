package com.nadan.firstappjetpackcompose.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// On crée une instance de DataStore (C'est notre AsyncStorage)
private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class AuthManager(private val context: Context) {

    companion object {
        private val AUTH_TOKEN = stringPreferencesKey("auth_token")
    }

    // Récupérer le token sous forme de Flow (réactif)
    val authToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[AUTH_TOKEN]
    }

    // Sauvegarder le token
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token
        }
    }

    // Supprimer le token (Déconnexion)
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(AUTH_TOKEN)
        }
    }
}
