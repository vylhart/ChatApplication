package com.example.chatapplication.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(
    @PrimaryKey
    val messageId: String = "",
    val channelId: String = "",
    val senderId: String = "",
    val data: String = "",
    val date:Long = 0
)
