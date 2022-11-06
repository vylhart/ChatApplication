package com.example.chatapplication.presentation.screens.auth

data class AuthState(
    val isLoading: Boolean = false,
    val isSignedIn: Boolean = false,
    val error: String = ""
)
