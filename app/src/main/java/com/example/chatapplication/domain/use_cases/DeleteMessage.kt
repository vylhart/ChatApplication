package com.example.chatapplication.domain.use_cases

import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteMessage @Inject constructor(private val repository: MessageRepository) {
    operator fun invoke(message: Message): Flow<Resource<Boolean>> = flow {
        try{
            emit(Resource.Loading)
            repository.deleteMessage(message)
            emit(Resource.Success(true))
        }
        catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error"))
        }
    }
}