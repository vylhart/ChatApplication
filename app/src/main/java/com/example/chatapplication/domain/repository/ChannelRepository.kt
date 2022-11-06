package com.example.chatapplication.domain.repository

import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.Channel
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {
    suspend fun getChannels(userID: String): Flow<Resource<MutableList<Channel>>>
}