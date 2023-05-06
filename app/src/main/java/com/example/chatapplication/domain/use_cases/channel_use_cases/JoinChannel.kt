package com.example.chatapplication.domain.use_cases.channel_use_cases

import android.util.Log
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Constants.UNKNOWN_ERROR
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.Channel
import com.example.chatapplication.domain.repository.ChannelRepository
import kotlinx.coroutines.flow.flow

class JoinChannel(private val repository: ChannelRepository) {

    suspend operator fun invoke(channelID: String) = flow {
        try {
            emit(Resource.Loading)
            val channel: Channel = repository.getChannel(channelID)
            repository.joinChannel(channel)
            emit(Resource.Success(true))
        } catch (e: Exception) {
            Log.e(TAG, "invoke: ", e)
            emit(Resource.Error(e.localizedMessage ?: UNKNOWN_ERROR))
        }
    }
}