package com.example.chatapplication.domain.use_cases.chat_use_cases

import android.util.Log
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.data.remote.repository.MessageRemoteRepositoryImpl
import com.example.chatapplication.domain.repository.MessageRepository
import javax.inject.Inject

class FetchMessages @Inject constructor(
    private val localRepository: MessageRepository,
    private val remoteRepository: MessageRemoteRepositoryImpl) {
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