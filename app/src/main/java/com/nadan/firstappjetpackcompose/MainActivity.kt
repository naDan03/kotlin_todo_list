package com.nadan.firstappjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.nadan.firstappjetpackcompose.data.AuthManager
import com.nadan.firstappjetpackcompose.navigation.TodoBottomBar
import com.nadan.firstappjetpackcompose.navigation.TodoNavHost
import com.nadan.firstappjetpackcompose.screens.LoginScreen
import com.nadan.firstappjetpackcompose.screens.SignUpScreen
import com.nadan.firstappjetpackcompose.ui.theme.AccentBlue
import com.nadan.firstappjetpackcompose.ui.theme.FirstAppJetPackComposeTheme
import com.nadan.firstappjetpackcompose.ui.theme.Slate50
import com.nadan.firstappjetpackcompose.viewmodel.AuthViewModel
import com.nadan.firstappjetpackcompose.viewmodel.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstAppJetPackComposeTheme {
                val context = LocalContext.current
                val authViewModel: AuthViewModel = viewModel(
                    factory = remember { AuthViewModelFactory(AuthManager(context)) }
                )
                
                val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
                var showSignUp by remember { mutableStateOf(false) }
                
                when (isLoggedIn) {
                    true -> TodoApp(onLogout = { authViewModel.logout() })
                    false -> {
                        if (showSignUp) {
                            SignUpScreen(
                                viewModel = authViewModel,
                                onBackToLogin = { showSignUp = false }
                            )
                        } else {
                            LoginScreen(
                                viewModel = authViewModel,
                                onNavigateToSignUp = { showSignUp = true }
                            )
                        }
                    }
                    null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Slate50),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = AccentBlue)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TodoApp(onLogout: () -> Unit) {
    val navController = rememberNavController()
    
    Scaffold(
        containerColor = Slate50,
        bottomBar = {
            TodoBottomBar(navController = navController)
        }
    ) { paddingValues ->
        TodoNavHost(
            navController = navController,
            onLogout = onLogout,
            modifier = Modifier.padding(paddingValues)
        )
    }
}
