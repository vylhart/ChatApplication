package com.example.chatapplication.domain.model

import android.net.Uri

data class User(
    val uid: String = "",
    val createdAt: Long = 0,
    val name: String?,
    val email: String?,
    val photoURL: Uri?,
    val channelIDs: List<String> = emptyList()
)
