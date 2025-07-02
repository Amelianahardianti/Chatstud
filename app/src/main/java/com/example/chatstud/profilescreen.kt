package com.example.chatstud

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser

@Composable
fun ProfileScreen(user: FirebaseUser?, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Profil", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Text("Email: ${user?.email}")
        Text("Nama: ${user?.displayName ?: "Anon"}")

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBack) {
            Text("Kembali")
        }
    }
}
