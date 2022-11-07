package com.example.chatapplication.domain.model

import android.net.Uri

data class User(
    val uid: String = "",
    val createdAt: Long = 0,
    val name: String? = null,
    val email: String? = null,
    val photoURL: String? = null,
    val channelIDs: MutableList<String> = mutableListOf()
)
