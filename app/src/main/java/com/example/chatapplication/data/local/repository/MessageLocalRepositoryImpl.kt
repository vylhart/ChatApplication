package com.example.chatapplication.data.local.repository

import com.example.chatapplication.data.local.MessageDao
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageLocalRepositoryImpl @Inject constructor(private val dao: MessageDao): MessageRepository {
    override suspend fun getMessages(channel: String): Flow<List<Message>> {
        return dao.getMessages(channel)
    }

    override suspend fun deleteMessage(message: Message) {
        return dao.deleteMessage(message)
    }

    override suspend fun sendMessage(message: Message) {
        return dao.insertMessage(message)
    }

}