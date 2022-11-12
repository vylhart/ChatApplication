package com.example.chatapplication.domain.repository

import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getCurrentUser(): Flow<Resource<User>>
    suspend fun getUser(): User
}