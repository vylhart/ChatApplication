package com.example.chatapplication.presentation.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatapplication.common.Screen
import com.example.chatapplication.presentation.screens.ChannelScreen
import com.example.chatapplication.presentation.screens.MessageScreen

@Composable
fun ChatNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.ChannelScreen.route
    ){
        composable(route = Screen.ChannelScreen.route){
            ChannelScreen()
        }
        composable(route = Screen.MessageScreen.route){
            MessageScreen()
        }
    }
}