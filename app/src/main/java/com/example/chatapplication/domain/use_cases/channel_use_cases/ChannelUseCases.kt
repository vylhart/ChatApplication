package com.example.chatapplication.domain.use_cases.channel_use_cases

class ChannelUseCases(
    val getChannelsFromNetwork : GetChannelsFromNetwork,
    val getChannelsFromLocalDB: GetChannelsFromLocalDB,
    val joinChannel : JoinChannel,
    val leaveChannel : LeaveChannel
)