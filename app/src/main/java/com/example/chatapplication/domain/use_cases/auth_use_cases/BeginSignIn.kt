package com.example.chatapplication.domain.use_cases.auth_use_cases

import android.content.Context
import com.example.chatapplication.domain.repository.AuthRepository
import com.example.chatapplication.presentation.MainActivity

class BeginSignIn(private val repository: AuthRepository) {
    suspend operator fun invoke(number: String, activity: Context) =
        repository.beginSignIn(number, activity)
}