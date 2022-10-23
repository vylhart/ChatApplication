package com.example.chatapplication.presentation.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.domain.use_cases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(private val useCases: UseCases): ViewModel() {

    private val _state = MutableStateFlow(ChannelState())
    val state : StateFlow<ChannelState> = _state

    fun enterChannel(channel: String, userId: String){
        Log.d(TAG, "enterChannel: $channel")
        _state.value = ChannelState(channelId = channel, userId = userId)
        getMessages()

    }

    fun sendMessage(text: String){
        Log.d(TAG, "sendMessage: sending")
        viewModelScope.launch {
            useCases.sendMessage(
                message =  Message(
                    senderId = state.value.userId,
                    channelId = state.value.channelId,
                    messageId = UUID.randomUUID().toString(),
                    data = text,
                    date = System.currentTimeMillis()
                )
            ).collect{
                //getMessages()
            }
        }
    }

    fun deleteMessage(message: Message){
        viewModelScope.launch {
            useCases.deleteMessage(message)
        }
    }

    private fun getMessages(){
        viewModelScope.launch{
            useCases.fetchMessages(state.value.channelId)
        }
        useCases.getMessages(state.value.channelId).onEach { result ->
            when(result){
                is Resource.Success -> {
                    Log.d(TAG, "getMessages: Success")
                    _state.value = ChannelState(messages = result.data, channelId = state.value.channelId, userId = state.value.userId)
                }
                is Resource.Loading -> {
                    Log.d(TAG, "getMessages: Loading")
                    _state.value = ChannelState(isLoading = true, channelId = state.value.channelId, userId = state.value.userId)
                }
                is Resource.Error -> {
                    Log.d(TAG, "getMessages: Error")
                    _state.value = ChannelState(error = result.message, channelId = state.value.channelId, userId = state.value.userId)
                }
            }
        }.launchIn(viewModelScope)

    }
}