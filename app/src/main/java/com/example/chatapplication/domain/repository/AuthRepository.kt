package com.example.chatapplication.domain.repository

import android.content.Context
import com.example.chatapplication.common.Resource
import com.example.chatapplication.presentation.MainActivity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signOut(): Flow<Resource<Boolean>>
    fun isUserAuthenticated(): Boolean
    suspend fun beginSignIn(phoneNumber: String, activity: Context): Flow<Resource<Boolean>>
    suspend fun firebaseSignIn(code: String): Flow<Resource<Boolean>>
}