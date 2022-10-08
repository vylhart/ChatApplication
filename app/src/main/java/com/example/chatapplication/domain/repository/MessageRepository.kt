package com.example.chatapplication.domain.repository

import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getMessages(channel: String): List<Message>
    suspend fun deleteMessage(channel: String, messageId: String)
    suspend fun sendMessage(channel: String, message: Message)
}