package com.example.chatapplication.domain.use_cases.auth_use_cases

import com.example.chatapplication.domain.repository.AuthRepository

class IsUserAuthenticated(private val repository: AuthRepository) {
    operator fun invoke() = repository.isUserAuthenticated()
}