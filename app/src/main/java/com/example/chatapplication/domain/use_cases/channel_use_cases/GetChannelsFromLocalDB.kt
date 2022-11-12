package com.example.chatapplication.domain.use_cases.channel_use_cases

import android.util.Log
import com.example.chatapplication.common.Constants
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.Channel
import com.example.chatapplication.domain.repository.ChannelRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class GetChannelsFromLocalDB @Inject constructor(@Named("local") private val repository: ChannelRepository) {
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