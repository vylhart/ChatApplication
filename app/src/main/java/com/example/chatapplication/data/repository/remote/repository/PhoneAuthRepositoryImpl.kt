package com.example.chatapplication.data.repository.remote.repository

import android.content.Context
import android.util.Log
import com.example.chatapplication.common.Constants
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.repository.AuthRepository
import com.example.chatapplication.presentation.activities.LoginActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.TimeUnit

class PhoneAuthRepositoryImpl(private val auth: FirebaseAuth) : AuthRepository {
    private lateinit var mPhoneNumber: String
    private lateinit var mStoredVerificationId: String
    private lateinit var mResendToken: PhoneAuthProvider.ForceResendingToken

    override fun signOut(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading)
            auth.signOut()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Sign out failed"))
        }
    }

    override fun isUserAuthenticated(): Boolean = auth.currentUser != null

    override suspend fun beginSignIn(
        phoneNumber: String,
        activity: Context,
    ): Flow<Resource<Boolean>> = callbackFlow {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d(Constants.TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential).onEach {
                    trySend(it)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w(Constants.TAG, "onVerificationFailed", e)
                trySend(Resource.Error(e.localizedMessage ?: "Verification failed"))
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                Log.d(Constants.TAG, "onCodeSent:$verificationId")
                trySend(Resource.Success(false))
                mStoredVerificationId = verificationId
                mResendToken = token
            }
        }

        mPhoneNumber = phoneNumber
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity as LoginActivity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        awaitClose {
            close()
        }
    }

    override suspend fun firebaseSignIn(code: String): Flow<Resource<Boolean>> = flow {
        val credential = PhoneAuthProvider.getCredential(mStoredVerificationId, code)
        signInWithPhoneAuthCredential(credential).collect { emit(it) }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): Flow<Resource<Boolean>> =
        callbackFlow {
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(Constants.TAG, "signInWithCredential:success")
                        trySend(Resource.Success(true))
                    } else {
                        Log.w(Constants.TAG, "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            trySend(Resource.Error("Invalid code"))
                        }
                        trySend(Resource.Error("Verification failed"))
                    }
                }
            awaitClose { close() }
        }

}