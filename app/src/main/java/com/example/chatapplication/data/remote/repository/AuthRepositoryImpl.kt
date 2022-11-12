package com.example.chatapplication.data.remote.repository

import android.util.Log
import com.example.chatapplication.common.Constants.COLLECTION_USER
import com.example.chatapplication.common.Constants.SIGN_IN_REQUEST
import com.example.chatapplication.common.Constants.SIGN_UP_REQUEST
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.User
import com.example.chatapplication.domain.repository.AuthRepository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val oneTapClient: SignInClient,
    private val firestore: FirebaseFirestore,
    @Named(SIGN_IN_REQUEST) private val signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST) private val signUpRequest: BeginSignInRequest

): AuthRepository {

    override fun isUserAuthenticated(): Boolean = auth.currentUser != null

    override suspend fun oneTapSignIn(): Flow<Resource<BeginSignInResult>> = flow {
        try {
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
             emit(Resource.Success(signInResult))
        } catch (e: Exception) {
            try {
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                emit(Resource.Success(signUpResult))
            } catch (e: Exception) {
                Log.e(TAG, "oneTapSignIn: ", e)
                emit(Resource.Error(e.localizedMessage?: "Sign in failed"))
            }
        }
    }

    override suspend fun firebaseSignIn(googleCredential: AuthCredential)= flow {
        try {
            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            if (isNewUser) {
                addUserToFirestore()
            }
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage?: "Sign in failed"))
        }
    }

    private suspend fun addUserToFirestore() {
        auth.currentUser?.apply {
            val user = User(
                uid = uid,
                createdAt = System.currentTimeMillis(),
                name = displayName ?: "user",
                email = email,
                photoURL = photoUrl.toString()
            )
            firestore.collection(COLLECTION_USER).document(user.uid).set(user).await()
        }
    }

    override fun signOut(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading)
            oneTapClient.signOut().await()
            emit(Resource.Success(true))
        }catch (e: Exception){
            emit(Resource.Error(e.localizedMessage?: "Sign out failed"))
        }
    }

}
