package com.example.chatapplication.presentation.screens.message

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.common.Constants.CHANNEL_ID
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
class MessageViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases,
    auth: FirebaseAuth,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var state = MutableStateFlow(MessageState())
        private set

    init {
        val channel = savedStateHandle.get<String>(CHANNEL_ID) ?: "random_channel"
        Log.d(TAG, "enterChannel: $channel")
        val userId = auth.currentUser?.uid ?: "random"
        state.value = MessageState(channelId = channel, userId = userId)
        getMessages()
    }

    fun sendMessage(text: String) {
        if (text.isEmpty()) return
        Log.d(TAG, "sendMessage: sending")
        viewModelScope.launch {
            chatUseCases.run {
                sendMessage(
                    message = Message(
                        senderId = state.value.userId,
                        channelId = state.value.channelId,
                        messageId = UUID.randomUUID().toString(),
                        data = text,
                        date = System.currentTimeMillis()
                    )
                ).collect {}
            }
        }
    }

    fun deleteMessage(message: Message) {
        viewModelScope.launch {
            chatUseCases.deleteMessage(message)
        }
    }

    private fun getMessages() {
        viewModelScope.launch {
            chatUseCases.getMessagesFromNetwork(state.value.channelId)
        }
        chatUseCases.getMessagesFromLocalDB(state.value.channelId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d(TAG, "getMessages: Success")
                    state.value = MessageState(
                        messages = result.data,
                        channelId = state.value.channelId,
                        userId = state.value.userId
                    )
                }
                is Resource.Loading -> {
                    Log.d(TAG, "getMessages: Loading")
                    state.value = MessageState(
                        isLoading = true,
                        channelId = state.value.channelId,
                        userId = state.value.userId
                    )
                }
                is Resource.Error -> {
                    Log.d(TAG, "getMessages: Error")
                    state.value = MessageState(
                        error = result.message,
                        channelId = state.value.channelId,
                        userId = state.value.userId
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}
