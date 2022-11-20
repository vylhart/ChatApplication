package com.example.chatapplication.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String = "", val icon: ImageVector= Icons.Default.Person){
    object MainScreen:    Screen("screen_main")
    object SignInScreen:  Screen("screen_sign_in")
    object MessageScreen: Screen("screen_message")
    object StatusScreen:  Screen(route = "screen_status",   title = "Status")
    object ChannelScreen: Screen(route = "screen_channel",  title = "Home",     icon = Icons.Default.Home)
    object ContactScreen: Screen(route = "screen_contacts", title = "Contacts", icon = Icons.Default.Phone)
}
