package com.example.chatapplication.domain.use_cases.channel_use_cases

import android.util.Log
import com.example.chatapplication.common.Constants
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.repository.ChannelRepository
import kotlinx.coroutines.flow.flow

class GetChannelsFromLocalDB(private val repository: ChannelRepository) {
    operator fun invoke() = flow {
        try{
            Log.d(TAG, "invoke: local")
            emit(Resource.Loading)
            repository.getChannels().collect{
                emit(Resource.Success(data = it))
            }
        }
        catch (e: Exception){
            Log.e(TAG, "invoke: ", e)
            emit(Resource.Error(e.localizedMessage ?: Constants.UNKNOWN_ERROR))
        }
    }
}