package com.example.chatapplication.domain.repository

import com.example.chatapplication.common.Resource
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signOut(): Flow<Resource<Boolean>>
    fun isUserAuthenticated(): Boolean
    suspend fun firebaseSignIn(googleCredential: AuthCredential): Flow<Resource<Boolean>>
    suspend fun oneTapSignIn(): Flow<Resource<BeginSignInResult>>
}