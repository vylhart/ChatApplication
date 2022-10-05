package com.example.chatapplication.domain.repository

import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getMessages(channel: String): Flow<Resource<List<Message>>>
    fun deleteMessage(messageId: String): Flow<Resource<Boolean>>
    fun sendMessage(message: Message): Flow<Resource<Boolean>>
}