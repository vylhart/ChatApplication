package com.example.chatapplication.domain.model

import androidx.room.Entity

@Entity
@kotlinx.serialization.Serializable
data class User(
    val uid: String = "",
    val createdAt: Long = 0,
    val name: String? = null,
    val email: String? = null,
    val photoURL: String? = null,
    val channelIDs: HashMap<String, String> = HashMap()
)
