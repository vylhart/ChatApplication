package com.example.chatapplication.domain.use_cases.chat_use_cases

import com.example.chatapplication.common.Resource
import com.example.chatapplication.data.worker.WorkerUtils
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMessages @Inject constructor(private val repository: MessageRepository, private val workerUtils: WorkerUtils) {
    operator fun invoke(channel: String):Flow<Resource<List<Message>>> = flow {
        try{
            emit(Resource.Loading)
            workerUtils.getMessages(channel)
            repository.getMessages(channel).collect{ list->
                emit(Resource.Success(data = list))
            }
        }
        catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error"))
        }
    }
}