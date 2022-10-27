package com.example.chatapplication.presentation.viewmodels

data class AuthState(
    val isLoading: Boolean = false,
    val isSignedIn: Boolean = false,
    val error: String = ""
)
