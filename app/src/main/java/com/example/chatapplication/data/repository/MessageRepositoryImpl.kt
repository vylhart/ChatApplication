package com.example.chatapplication.data.repository

import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.domain.repository.MessageRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MessageRepositoryImpl (private val firestore: FirebaseFirestore): MessageRepository {
    override fun getMessages(channel: String): Flow<Resource<List<Message>>> = flow{
    }

    override fun deleteMessage(messageId: String): Flow<Resource<Boolean>> = flow{
    }

    override fun sendMessage(message: Message): Flow<Resource<Boolean>> = flow {

    }
}