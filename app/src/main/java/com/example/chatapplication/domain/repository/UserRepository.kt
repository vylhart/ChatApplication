package com.example.chatapplication.domain.repository

import com.example.chatapplication.common.Resource
import com.example.chatapplication.data.model.User
import com.example.chatapplication.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getCurrentUser(): Flow<Resource<User?>>
    suspend fun addNewUser(name: String): Flow<Resource<Boolean>>
    suspend fun getUser(uid: String): User?
    suspend fun getContacts(): Flow<Resource<List<Contact>>>
}