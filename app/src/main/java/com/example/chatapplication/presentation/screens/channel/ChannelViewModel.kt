package com.example.chatapplication.presentation.screens.channel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.common.Constants.COLLECTION_USER
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.Channel
import com.example.chatapplication.domain.use_cases.user_use_cases.UserUseCases
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChannelViewModel @Inject constructor(
    private val useCases: UserUseCases,
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
            useCases.getChannelsFromNetwork().onEach {    result ->
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
            }.collect{}
        }
    }

    fun joinChannel(channelID: String, callback: ()-> Unit){
        Log.d(TAG, "joinChannel: $channelID")
        for(channel in state.value.channels){
            if (channel.channelID == channelID){
                Log.d(TAG, "joinChannel: exists")
                callback()
                return
            }
        }
        viewModelScope.launch {
            useCases.joinChannel(channelID).collect{ result ->
                if(result is Resource.Success && result.data){
                    Log.d(TAG, "joinChannel: new")
                    callback()
                }
            }
        }
    }
}