package com.example.chatapplication.presentation.screens.channel

import com.example.chatapplication.domain.model.Channel

data class ChannelState(
    val userID: String,
    val isLoading: Boolean = false,
    val error: String = "",
    val channels: List<Channel> = emptyList()
)
