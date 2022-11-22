package com.example.chatapplication.domain.use_cases.auth_use_cases

import com.example.chatapplication.domain.repository.AuthRepository

class FirebaseSignIn(private val repository: AuthRepository) {
    suspend operator fun invoke(code: String) = repository.firebaseSignIn(code)
}