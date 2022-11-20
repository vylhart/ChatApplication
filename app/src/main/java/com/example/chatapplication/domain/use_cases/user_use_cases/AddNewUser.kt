package com.example.chatapplication.domain.use_cases.user_use_cases

import com.example.chatapplication.domain.repository.UserRepository

class AddNewUser(private val repository: UserRepository) {
    suspend operator fun invoke(name: String) = repository.addNewUser(name)
}