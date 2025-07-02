package com.example.chatstud

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

@Composable
fun ChatScreen(onLogout: () -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Message>() }
    val displayName = user?.displayName ?: user?.email?.substringBefore("@") ?: "Anon"

    // Realtime listener
    LaunchedEffect(Unit) {
        db.collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, _ ->
                messages.clear()
                snapshot?.documents?.mapNotNull { it.toObject(Message::class.java) }
                    ?.let { messages.addAll(it) }
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 🔘 Tombol Logout
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = {
                auth.signOut()
                Toast.makeText(context, "Berhasil logout", Toast.LENGTH_SHORT).show()
                onLogout()
            }) {
                Text("Logout")
            }
        }

        // 🧾 Chat list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true
        ) {
            items(messages.reversed()) { msg ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalAlignment = if (msg.sender == user?.email)
                        Alignment.End else Alignment.Start
                ) {
                    Text(
                        text = msg.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (msg.sender == user?.email)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = msg.text,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

        // 📝 Input & Kirim
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ketik pesan...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (messageText.isNotBlank() && user != null) {
                    val msg = Message(
                        text = messageText,
                        sender = user.email ?: "Anon",
                        displayName = displayName,
                        timestamp = System.currentTimeMillis()
                    )
                    db.collection("messages").add(msg)
                    messageText = ""
                }
            }) {
                Text("Kirim")
            }
        }
    }
}
