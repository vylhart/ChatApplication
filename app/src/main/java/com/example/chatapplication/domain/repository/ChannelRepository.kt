package com.example.chatapplication.domain.repository

import com.example.chatapplication.domain.model.Channel
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {
    suspend fun getChannels(): Flow<List<Channel>>
    suspend fun leaveChannel(channel: Channel)
    suspend fun joinChannel(channel: Channel)
    suspend fun getChannel(channelID: String): Channel
}