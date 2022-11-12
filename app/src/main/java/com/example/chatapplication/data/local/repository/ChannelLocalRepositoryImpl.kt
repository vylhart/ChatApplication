package com.example.chatapplication.data.local.repository

import com.example.chatapplication.data.local.ChannelDao
import com.example.chatapplication.domain.model.Channel
import com.example.chatapplication.domain.repository.ChannelRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChannelLocalRepositoryImpl @Inject constructor(private val dao: ChannelDao): ChannelRepository {
    override suspend fun getChannels(): Flow<List<Channel>> {
        return dao.getChannels()
    }

    override suspend fun leaveChannel(channel: Channel) {
        dao.leaveChannel(channel)
    }

    override suspend fun joinChannel(channel: Channel) {
        dao.joinChannel(channel)
    }

    override suspend fun getChannel(channelID: String): Channel {
        return dao.getChannel(channelID)
    }
}