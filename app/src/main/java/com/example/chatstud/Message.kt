package com.example.chatstud

data class Message(
    val text: String = "",
    val sender: String = "",
    val displayName: String = "",
    val timestamp: Long = 0L
)
