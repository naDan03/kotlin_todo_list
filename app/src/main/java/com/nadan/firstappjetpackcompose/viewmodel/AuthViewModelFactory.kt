package com.nadan.firstappjetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nadan.firstappjetpackcompose.data.AuthManager

class AuthViewModelFactory(private val authManager: AuthManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
