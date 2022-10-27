package com.example.chatapplication.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.use_cases.auth_use_cases.AuthUseCases
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val  authUseCases: AuthUseCases,
    val oneTapClient: SignInClient
): ViewModel()  {


    val isUserAuthenticated get() = authUseCases.isUserAuthenticated()
    private val _state = MutableStateFlow(AuthState(isSignedIn = isUserAuthenticated))
    val state : StateFlow<AuthState> = _state

    fun oneTapSignIn(callback: (BeginSignInResult) -> Unit){
        viewModelScope.launch {
            authUseCases.oneTapSignIn().collect{
                when(it){
                    is Resource.Loading -> {
                        _state.value = AuthState(isLoading = true)
                        Log.d(TAG, "oneTapSignIn: loading")
                    }
                    is Resource.Success -> {
                        callback(it.data)
                        Log.d(TAG, "oneTapSignIn: success")
                    }
                    is Resource.Error -> {
                        _state.value = AuthState(error = it.message)
                        Log.d(TAG, "oneTapSignIn: ${it.message}")
                    }
                }
            }
        }
    }

    fun firebaseSignIn(googleCredential: AuthCredential){
        viewModelScope.launch{
            authUseCases.firebaseSignIn(googleCredential).collect{
                when(it){
                    is Resource.Loading -> {
                        _state.value = AuthState(isLoading = true)
                        Log.d(TAG, "firebaseSignIn: loading")
                    }
                    is Resource.Success -> {
                        _state.value = AuthState(isSignedIn = it.data)
                        Log.d(TAG, "firebaseSignIn: success")
                    }
                    is Resource.Error -> {
                        _state.value = AuthState(error = it.message)
                        Log.d(TAG, "firebaseSignIn: error")
                    }
                }
            }
        }
    }


    fun signOut(){
        authUseCases.signOut().launchIn(viewModelScope)
    }



}