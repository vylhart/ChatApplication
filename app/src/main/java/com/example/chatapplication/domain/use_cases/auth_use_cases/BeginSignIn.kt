package com.example.chatapplication.domain.use_cases.auth_use_cases

import com.example.chatapplication.domain.repository.AuthRepository
import com.example.chatapplication.presentation.MainActivity
import javax.inject.Inject

class BeginSignIn(private val repository: AuthRepository) {
    suspend operator fun invoke(number: String, activity: MainActivity) = repository.beginSignIn(number, activity)
}