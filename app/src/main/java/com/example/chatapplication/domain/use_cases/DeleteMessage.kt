package com.example.chatapplication.domain.use_cases

import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteMessage @Inject constructor(private val repository: MessageRepository) {
    operator fun invoke(channel: String, messageId: String): Flow<Resource<Boolean>> = flow {
        try{
            emit(Resource.Loading)
            repository.deleteMessage(channel = channel, messageId= messageId)
            emit(Resource.Success(true))
        }
        catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error"))
        }
    }
}