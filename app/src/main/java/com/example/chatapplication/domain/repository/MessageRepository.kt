package com.example.chatapplication.domain.repository

import com.example.chatapplication.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getMessages(channel: String): Flow<List<Message>>
    suspend fun deleteMessage(message: Message)
    suspend fun sendMessage(message: Message)
}