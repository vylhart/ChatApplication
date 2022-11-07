package com.example.chatapplication.domain.use_cases.user_use_cases

data class UserUseCases(
    val getChannelsFromNetwork: GetChannelsFromNetwork,
    val getCurrentUsers: GetCurrentUser,
    val joinChannel: JoinChannel
)
