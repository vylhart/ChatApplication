package com.example.chatapplication.domain.model

class Channel(
    val channelID: String = "",
    val users: List<User> = emptyList(),
    val dateCreated: Long = 0L,
    val dateModified: Long = 0L
)