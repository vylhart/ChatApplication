package com.example.chatapplication.domain.use_cases.channel_use_cases

import android.util.Log
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Constants.UNKNOWN_ERROR
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.Channel
import com.example.chatapplication.domain.repository.ChannelRepository
import kotlinx.coroutines.flow.flow

class LeaveChannel(
    private val localRepository: ChannelRepository,
    private val remoteRepository: ChannelRepository,
) {
    suspend operator fun invoke(channel: Channel) = flow {
        try {
            localRepository.leaveChannel(channel)
            remoteRepository.leaveChannel(channel)
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: UNKNOWN_ERROR))
            Log.e(TAG, "invoke: ", e)
        }
    }
}