package com.example.chatapplication.domain.use_cases.channel_use_cases

import com.example.chatapplication.domain.repository.ChannelRepository

class GetChannelsFromNetwork(private val repository: ChannelRepository) {
    suspend operator fun invoke(userID: String) = repository.getChannels(userID)
}