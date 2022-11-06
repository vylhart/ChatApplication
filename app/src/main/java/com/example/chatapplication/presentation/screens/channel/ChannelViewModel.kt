package com.example.chatapplication.presentation.screens.channel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.common.Constants
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.Channel
import com.example.chatapplication.domain.use_cases.channel_use_cases.ChannelUseCases
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val useCases: ChannelUseCases,
    private val auth: FirebaseAuth,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    var state = MutableStateFlow(ChannelState(auth.currentUser!!.uid))
    private set

    init {
        getChannels()
    }

    private fun getChannels(){
        viewModelScope.launch {
            useCases.getChannelsFromNetwork(state.value.userID).onEach {    result ->
                when(result){
                    is Resource.Success -> {
                        Log.d(Constants.TAG, "getChannels: Success")
                        state.value = ChannelState(userID = state.value.userID, channels = result.data)
                    }
                    is Resource.Loading -> {
                        Log.d(Constants.TAG, "getChannels: Loading")
                        state.value = ChannelState(userID = state.value.userID, isLoading = true)
                    }
                    is Resource.Error -> {
                        Log.d(Constants.TAG, "getChannels: Error")
                        state.value = ChannelState(userID = state.value.userID, error = result.message)
                    }
                }
            }
        }
    }

    fun onClick(channelID: String){
        savedStateHandle["channelID"] = channelID
    }

    fun joinChannel(channelName: String){
        val channel = Channel(channelID = channelName)
    }



}