package com.example.chatapplication.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
@kotlinx.serialization.Serializable
data class Message(
    @PrimaryKey
    val messageId: String = "",
    val channelId: String = "",
    val senderId: String = "",
    val data: String = "",
    val date:Long = 0,
    val isSent: Boolean = false,
    val isDelivered: Boolean = false,
    val isSeen: Boolean = false
)
