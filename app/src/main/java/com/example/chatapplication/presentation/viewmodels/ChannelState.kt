package com.example.chatapplication.presentation.viewmodels

import com.example.chatapplication.domain.model.Message
import java.util.UUID

data class ChannelState(
    val isLoading: Boolean = false,
    val messages: List<Message> = emptyList(),
    val error: String = "",
    var userId: String = "random",
    val channelId: String = "random"
)
