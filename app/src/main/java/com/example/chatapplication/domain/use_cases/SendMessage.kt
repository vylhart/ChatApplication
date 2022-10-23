package com.example.chatapplication.domain.use_cases

import com.example.chatapplication.common.Resource
import com.example.chatapplication.data.worker.WorkerUtils
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SendMessage @Inject constructor(private val repository: MessageRepository, private val workerUtils: WorkerUtils) {
    operator fun invoke(message: Message): Flow<Resource<Boolean>> = flow {
        try {
            repository.sendMessage(message)
            workerUtils.sendMessage(message)
            emit(Resource.Success(true))
        }
        catch (e: Exception){
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error"))
        }
    }
}