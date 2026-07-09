package com.nadan.firstappjetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nadan.firstappjetpackcompose.data.AuthManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel pour gérer l'état d'authentification.
 */
class AuthViewModel(private val authManager: AuthManager) : ViewModel() {

    // On utilise Boolean? (nullable). null = On est en train de vérifier.
    val isLoggedIn: StateFlow<Boolean?> = authManager.authToken
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            // Simulation API
            if (email.isNotBlank() && password.length >= 6) {
                authManager.saveToken("fake-jwt-token-for-$email")
            }
        }
    }

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            // Simulation API Inscription
            if (name.isNotBlank() && email.isNotBlank() && password.length >= 6) {
                authManager.saveToken("fake-jwt-token-for-$email")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authManager.clearToken()
        }
    }
}
