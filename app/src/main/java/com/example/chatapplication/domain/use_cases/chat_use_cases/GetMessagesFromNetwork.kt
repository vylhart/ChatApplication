package com.example.chatapplication.domain.use_cases.chat_use_cases

import android.util.Log
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.domain.repository.MessageRepository
import javax.inject.Inject
import javax.inject.Named

class GetMessagesFromNetwork @Inject constructor(
    @Named("Local") private val localRepository: MessageRepository,
    @Named("Remote") private val remoteRepository: MessageRepository) {
    suspend operator fun invoke(channel: String) {
        Log.d(TAG, "invoke: fetch")
        try{
            remoteRepository.getMessages(channel).collect{
                for(msg in it){
                    localRepository.sendMessage(msg)
                }
            }
        }
        catch (e: Exception){
            Log.d(TAG, (e.localizedMessage ?: "An unexpected error"))
        }
    }
}