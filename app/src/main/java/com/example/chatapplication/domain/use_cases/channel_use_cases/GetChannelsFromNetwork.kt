package com.example.chatapplication.domain.use_cases.channel_use_cases

import android.util.Log
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.repository.ChannelRepository
import javax.inject.Inject
import javax.inject.Named

class GetChannelsFromNetwork @Inject constructor(
    @Named("local")  private val localRepository: ChannelRepository,
    @Named("remote") private val remoteRepository: ChannelRepository
    ) {
    suspend operator fun invoke(){
        try{
            remoteRepository.getChannels().collect{
                    for(channel in it){
                        localRepository.joinChannel(channel)
                    }
            }
        }
        catch (e: Exception){
            Log.e(TAG, "invoke: n/w", e)
        }
    }
}