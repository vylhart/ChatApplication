package com.example.chatapplication.domain.use_cases

import android.util.Log
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMessages @Inject constructor(private val repository: MessageRepository) {
    operator fun invoke(channel: String):Flow<Resource<List<Message>>> = flow {
        try{
            emit(Resource.Loading)
            repository.getMessages(channel).collect{ list->
                emit(Resource.Success(data = list))
            }
        }
        catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error"))
        }
    }
}