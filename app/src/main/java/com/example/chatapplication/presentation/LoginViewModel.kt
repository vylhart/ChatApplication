package com.example.chatapplication.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.common.Constants
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.use_cases.auth_use_cases.AuthUseCases
import com.example.chatapplication.domain.use_cases.user_use_cases.UserUseCases
import com.example.chatapplication.presentation.screens.auth.LoginData
import com.example.chatapplication.presentation.screens.auth.LoginStage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val userUseCases: UserUseCases,
) : ViewModel() {

    private val TAG = Constants.TAG + "LoginViewModel"
    var state = MutableLiveData(LoginData())
        private set

    init {
        Log.i(TAG, "init: ")
        if (authUseCases.isUserAuthenticated()) {
            Log.i(TAG, "init: already logged in")
            state.value = LoginData(stage = LoginStage.SignedIn)
        }
    }

    fun beginSignIn(number: String, activity: Context) {
        Log.d(TAG, "beginSignIn")
        viewModelScope.launch {
            authUseCases.beginSignIn(number, activity).collect {
                when (it) {
                    is Resource.Loading -> state.value = LoginData(isLoading = true)
                    is Resource.Error -> state.value = LoginData(error = it.message)
                    is Resource.Success -> {
                        if (it.data) {
                            checkNewUser()             // auto otp verified
                        } else {
                            state.value =
                                LoginData(stage = LoginStage.OTPVerifcation)  // manual otp verification
                        }
                    }
                }
            }
        }
    }


    fun verifyOTP(code: String) {
        Log.d(TAG, "firebaseSignIn: $code")
        viewModelScope.launch {
            authUseCases.firebaseSignIn(code).collect {
                when (it) {
                    is Resource.Loading -> state.value = LoginData(isLoading = true)
                    is Resource.Error -> state.value = LoginData(error = it.message)
                    is Resource.Success -> {
                        checkNewUser()
                    }
                }
            }
        }
    }

    private fun checkNewUser() {
        Log.d(Constants.TAG, "checkNewUser: ")
        viewModelScope.launch {
            userUseCases.getCurrentUser().collect {
                when (it) {
                    is Resource.Loading -> state.value = LoginData(isLoading = true)
                    is Resource.Error -> state.value = LoginData(error = it.message)
                    is Resource.Success -> {
                        Log.d(Constants.TAG, "checkNewUser:  ${it.data == null}")
                        if (it.data != null) {
                            state.value = LoginData(stage = LoginStage.SignedIn)
                        } else {
                            state.value = LoginData(stage = LoginStage.SignUp)
                        }
                    }
                }
            }
        }
    }

    fun addNewUser(name: String) {
        Log.d(Constants.TAG, "addNewUser: ")
        viewModelScope.launch {
            userUseCases.addNewUser(name).collect {
                when (it) {
                    is Resource.Loading -> state.value = LoginData(isLoading = true)
                    is Resource.Error -> state.value = LoginData(error = it.message)
                    is Resource.Success -> LoginData(stage = LoginStage.SignedIn)
                }
            }
        }
    }

    fun signOut() {
        authUseCases.signOut().launchIn(viewModelScope)
    }
}