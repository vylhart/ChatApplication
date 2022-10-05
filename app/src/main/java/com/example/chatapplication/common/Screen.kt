package com.example.chatapplication.common

sealed class Screen(val route: String){
    object ChannelScreen: Screen("screen_channel")
    object MessageScreen: Screen("screen_message")
}
