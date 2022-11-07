package com.example.chatapplication.domain.model

class Channel(
    val channelID: String = "",
    val users: MutableList<User> = mutableListOf(),
    val dateCreated: Long = 0L,
    val dateModified: Long = 0L
)