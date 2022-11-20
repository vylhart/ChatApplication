package com.example.chatapplication.domain.use_cases.channel_use_cases

import android.util.Log
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.domain.repository.ChannelRepository
import javax.inject.Inject
import javax.inject.Named

class GetChannelsFromNetwork(
    private val localRepository: ChannelRepository,
    private val remoteRepository: ChannelRepository
    ) {
    suspend operator fun invoke(){
        try{
            Log.d(TAG, "invoke: n/w")
            remoteRepository.getChannels().collect{
                    for(channel in it){
                        Log.d(TAG, "invoke: ${channel.channelID}")
                        localRepository.joinChannel(channel)
                    }
            }
        }
        catch (e: Exception){
            Log.e(TAG, "invoke: n/w", e)
        }
    }
}