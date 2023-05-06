package com.example.chatapplication.presentation.screens.auth

data class AuthState(
    val isLoading: Boolean = false,
    val isSignedIn: Boolean = false,
    val isNewUser: Boolean = true,
    val codeSent: Boolean = false,
    val error: String = "",
)


sealed class LoginStage {
    object LoggedOut : LoginStage()
    object OTPVerifcation : LoginStage()
    object SignUp : LoginStage()
    object SignedIn : LoginStage()
}

data class LoginData(
    val isLoading: Boolean = false,
    val stage: LoginStage = LoginStage.LoggedOut,
    val error: String = ""
)