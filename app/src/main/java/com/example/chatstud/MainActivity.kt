package com.example.chatstud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.*
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
                        },
                            onBack = {
                            navController.popBackStack()
                        })
                    }

                    composable("privateChat") {
                        val auth = FirebaseAuth.getInstance()
                        val currentUser = auth.currentUser
                        val currentUserEmail = currentUser?.email ?: ""

                        Userfriend(
                            currentUserEmail = currentUserEmail,
                            onUserSelected = { selectedUserEmail ->
                                navController.navigate("chatWith/${selectedUserEmail}")
                            },
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }


                    composable("profile") {
                        ProfileScreen(user = auth.currentUser) {
                            navController.popBackStack()
                        }
                    }

                    composable("chatWith/{email}") { backStackEntry ->
                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val currentUserEmail = currentUser?.email ?: ""
                        val recipientEmail = backStackEntry.arguments?.getString("email") ?: ""

                        PrivateChatScreen(
                            currentUserEmail = currentUserEmail,
                            recipientEmail = recipientEmail,
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                }
            }
        }
    }

}
