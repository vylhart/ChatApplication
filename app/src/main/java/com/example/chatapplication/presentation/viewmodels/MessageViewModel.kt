package com.example.chatapplication.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.domain.use_cases.chat_use_cases.ChatUseCases
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(private val chatUseCases: ChatUseCases, private val auth: FirebaseAuth): ViewModel() {

    var state = MutableStateFlow(ChannelState())
        private set

    fun enterChannel(channel: String){
        val userId  = auth.currentUser.toString()
        Log.d(TAG, "enterChannel: $channel")
        Log.d(TAG, "user: $userId")
        state.value = ChannelState(channelId = channel, userId = userId)
        getMessages()
    }

    fun sendMessage(text: String){
        Log.d(TAG, "sendMessage: sending")
        viewModelScope.launch {
            chatUseCases.run {
                sendMessage(
                        message =  Message(
                            senderId = state.value.userId,
                            channelId = state.value.channelId,
                            messageId = UUID.randomUUID().toString(),
                            data = text,
                            date = System.currentTimeMillis()
                        )
                    ).collect{}
            }
        }
    }

    fun deleteMessage(message: Message){
        viewModelScope.launch {
            chatUseCases.deleteMessage(message)
        }
    }

    private fun getMessages(){
        viewModelScope.launch{
            chatUseCases.getMessagesFromNetwork(state.value.channelId)
        }
        chatUseCases.getMessagesFromLocalDB(state.value.channelId).onEach { result ->
            when(result){
                is Resource.Success -> {
                    Log.d(TAG, "getMessages: Success")
                    state.value = ChannelState(messages = result.data, channelId = state.value.channelId, userId = state.value.userId)
                }
                is Resource.Loading -> {
                    Log.d(TAG, "getMessages: Loading")
                    state.value = ChannelState(isLoading = true, channelId = state.value.channelId, userId = state.value.userId)
                }
                is Resource.Error -> {
                    Log.d(TAG, "getMessages: Error")
                    state.value = ChannelState(error = result.message, channelId = state.value.channelId, userId = state.value.userId)
                }
            }
        }.launchIn(viewModelScope)
    }
}