package com.example.chatapplication.domain.use_cases.auth_use_cases

data class AuthUseCases(
    val isUserAuthenticated: IsUserAuthenticated,
    val firebaseSignIn: FirebaseSignIn,
    val signOut: SignOut,
    val beginSignIn: BeginSignIn,
)
