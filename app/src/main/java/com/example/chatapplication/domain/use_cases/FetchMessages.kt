package com.example.chatapplication.domain.use_cases

import android.util.Log
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Resource
import com.example.chatapplication.data.remote.repository.MessageRemoteRepositoryImpl
import com.example.chatapplication.data.worker.WorkerUtils
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchMessages @Inject constructor(
    private val localRepository: MessageRepository,
    private val remoteRepository: MessageRemoteRepositoryImpl,
    private val workerUtils: WorkerUtils) {
    suspend operator fun invoke(channel: String) {
        Log.d(TAG, "invoke: fetch")
        try{
            remoteRepository.fetchMessages(channel).collect{
                localRepository.sendMessage(it)
            }
        }
        catch (e: Exception){
            Log.d(TAG, (e.localizedMessage ?: "An unexpected error"))
        }
    }
}