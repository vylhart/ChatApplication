package com.example.chatapplication.presentation.screens.message

import com.example.chatapplication.domain.model.Message

data class MessageState(
    val isLoading: Boolean = false,
    val messages: List<Message> = emptyList(),
    val error: String = "",
    val userId: String,
    val channelId: String
)
