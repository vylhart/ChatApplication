package com.example.chatapplication.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Contact(
    @PrimaryKey
    val uid: String = "",
    val number: String = "",
    val name:String = "",
    val photoURL: String? = null,
)