package com.example.chatstud

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser

@Composable
fun HomeScreen(
    user: FirebaseUser?,
    onLogout: () -> Unit,
    onGoToGlobalChat: () -> Unit,
    onGoToPrivateChat: () -> Unit,
    onGoToProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selamat datang, ${user?.displayName ?: user?.email ?: "Anon"}!",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onGoToGlobalChat, modifier = Modifier.fillMaxWidth()) {
            Text("Global Chat")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onGoToPrivateChat, modifier = Modifier.fillMaxWidth()) {
            Text("Private Chat")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onGoToProfile, modifier = Modifier.fillMaxWidth()) {
            Text("Profile")
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Logout")
        }
    }
}
