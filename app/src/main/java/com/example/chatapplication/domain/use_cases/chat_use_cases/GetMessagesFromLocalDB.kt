package com.example.chatapplication.domain.use_cases.chat_use_cases

import com.example.chatapplication.common.Constants.UNKNOWN_ERROR
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMessagesFromLocalDB(private val repository: MessageRepository) {
    operator fun invoke(channel: String): Flow<Resource<List<Message>>> = flow {
        try {
            emit(Resource.Loading)
            repository.getMessages(channel).collect { list ->
                emit(Resource.Success(data = list))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: UNKNOWN_ERROR))
        }
    }
}