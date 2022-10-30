package com.example.chatapplication.domain.model

data class User(
    private val uid: String,
    private val createdAt: Long,
    private val name: String,
    private val photoURL: String
)
