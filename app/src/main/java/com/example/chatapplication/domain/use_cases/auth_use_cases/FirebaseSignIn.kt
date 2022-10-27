package com.example.chatapplication.domain.use_cases.auth_use_cases

import com.example.chatapplication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import javax.inject.Inject

class FirebaseSignIn @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(googleCredential: AuthCredential) = repository.firebaseSignIn(googleCredential)
}