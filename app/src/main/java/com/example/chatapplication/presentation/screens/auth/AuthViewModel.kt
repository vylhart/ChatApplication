package com.example.chatapplication.presentation.screens.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.use_cases.auth_use_cases.AuthUseCases
import com.example.chatapplication.domain.use_cases.user_use_cases.UserUseCases
import com.example.chatapplication.presentation.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val userUseCases: UserUseCases,
) : ViewModel() {

    val isUserAuthenticated get() = authUseCases.isUserAuthenticated()
    var state = MutableStateFlow(AuthState(isSignedIn = isUserAuthenticated))
        private set

    fun beginSignIn(number: String, activity: MainActivity) {
        Log.d(TAG, "beginSignIn: ")
        viewModelScope.launch {
            authUseCases.beginSignIn(number, activity).collect {
                when (it) {
                    is Resource.Loading -> state.value = AuthState(isLoading = true)
                    is Resource.Error -> state.value = AuthState(error = it.message)
                    is Resource.Success -> {
                        if (it.data) {
                            checkNewUser()
                        }
                        state.value = AuthState(codeSent = true, isSignedIn = it.data)
                    }
                }
            }
        }
    }

    fun firebaseSignIn(code: String) {
        Log.d(TAG, "firebaseSignIn: ")
        viewModelScope.launch {
            authUseCases.firebaseSignIn(code).collect {
                when (it) {
                    is Resource.Loading -> state.value = AuthState(isLoading = true)
                    is Resource.Error -> state.value = AuthState(error = it.message)
                    is Resource.Success -> {
                        state.value = AuthState(isSignedIn = it.data)
                        checkNewUser()
                    }
                }
            }
        }
    }

    private fun checkNewUser() {
        Log.d(TAG, "checkNewUser: ")
        viewModelScope.launch {
            userUseCases.getCurrentUser().collect {
                when (it) {
                    is Resource.Loading -> state.value = AuthState(isLoading = true)
                    is Resource.Error -> state.value = AuthState(error = it.message)
                    is Resource.Success -> {
                        Log.d(TAG, "checkNewUser: success ${it.data == null}")
                        state.value = AuthState(
                            isNewUser = it.data == null,
                            isSignedIn = state.value.isSignedIn
                        )
                    }

                }
            }
        }
    }

    fun addNewUser(name: String, navigateToChannelScreen: () -> Unit) {
        Log.d(TAG, "addNewUser: ")
        viewModelScope.launch {
            userUseCases.addNewUser(name).collect {
                when (it) {
                    is Resource.Loading -> state.value = AuthState(isLoading = true)
                    is Resource.Error -> state.value = AuthState(error = it.message)
                    is Resource.Success -> navigateToChannelScreen()
                }
            }
        }
    }

    fun signOut() {
        authUseCases.signOut().launchIn(viewModelScope)
    }
}