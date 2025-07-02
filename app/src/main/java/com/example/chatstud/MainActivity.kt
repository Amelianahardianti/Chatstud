package com.example.chatstud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.chatstud.ui.theme.ChatstudTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChatstudTheme {
                val navController = rememberNavController()
                val auth = FirebaseAuth.getInstance()
                val user = auth.currentUser

                NavHost(
                    navController = navController,
                    startDestination = if (user != null) "home" else "auth"
                ) {
                    composable("auth") {
                        AuthScreen(onLoginSuccess = {
                            navController.navigate("home") {
                                popUpTo("auth") { inclusive = true }
                            }
                        })
                    }

                    composable("home") {
                        HomeScreen(
                            user = auth.currentUser,
                            onLogout = {
                                auth.signOut()
                                navController.navigate("auth") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            onGoToGlobalChat = { navController.navigate("globalChat") },
                            onGoToPrivateChat = { navController.navigate("privateChat") },
                            onGoToProfile = { navController.navigate("profile") }
                        )
                    }

                    composable("globalChat") {
                        ChatScreen(onLogout = {
                            auth.signOut()
                            navController.navigate("auth") {
                                popUpTo("globalChat") { inclusive = true }
                            }
                        })
                    }

                    composable("privateChat") {
                        // Placeholder dulu ntr buat
                        Text("Private Chat Placeholder")
                    }

                    composable("profile") {
                        ProfileScreen(user = auth.currentUser) {
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}
