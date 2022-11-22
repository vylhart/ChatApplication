package com.example.chatapplication.presentation.screens.channel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.use_cases.channel_use_cases.ChannelUseCases
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val useCases: ChannelUseCases,
    auth: FirebaseAuth,
): ViewModel() {
    var state = MutableStateFlow(ChannelState(auth.currentUser!!.uid))
    private set

    init {
        getChannels()
    }

    private fun getChannels(){
        Log.d(TAG, "getChannels: ")
        viewModelScope.launch {
            useCases.getChannelsFromNetwork()
        }

        viewModelScope.launch {
            useCases.getChannelsFromLocalDB().collect { result ->
                when(result){
                    is Resource.Success -> {
                        Log.d(TAG, "getChannels: Success")
                        state.value = ChannelState(userID = state.value.userID, channels = result.data)
                    }
                    is Resource.Loading -> {
                        Log.d(TAG, "getChannels: Loading")
                        state.value = ChannelState(userID = state.value.userID, isLoading = true)
                    }
                    is Resource.Error -> {
                        Log.d(TAG, "getChannels: Error")
                        state.value = ChannelState(userID = state.value.userID, error = result.message)
                    }
                }
            }
        }
    }
}