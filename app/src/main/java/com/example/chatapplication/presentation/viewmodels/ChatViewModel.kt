package com.example.chatapplication.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.common.Constants
import com.example.chatapplication.common.Resource
import com.example.chatapplication.domain.use_cases.channel_use_cases.ChannelUseCases
import com.example.chatapplication.presentation.screens.channel.ChannelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagerViewModel @Inject constructor(private val useCases: ChannelUseCases) : ViewModel() {
    var state = MutableLiveData(ChannelState())
        private set

    init {
        getChannels()
    }

    private fun getChannels() {
        Log.d(Constants.TAG, "getChannels: ")
        viewModelScope.launch {
            useCases.getChannelsFromNetwork()
        }

        viewModelScope.launch {
            useCases.getChannelsFromLocalDB().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.d(Constants.TAG, "getChannels: Success")
                        state.value = ChannelState(channels = result.data)
                    }
                    is Resource.Loading -> {
                        Log.d(Constants.TAG, "getChannels: Loading")
                        state.value = ChannelState(isLoading = true)
                    }
                    is Resource.Error -> {
                        Log.d(Constants.TAG, "getChannels: Error")
                        state.value = ChannelState(error = result.message)
                    }
                }
            }
        }
    }
}