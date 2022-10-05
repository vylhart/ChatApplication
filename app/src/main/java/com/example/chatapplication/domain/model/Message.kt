package com.example.chatapplication.domain.model

data class Message(
    val messageId: String,
    val senderId: String,
    val data: String,
    val dateTime: String
)
