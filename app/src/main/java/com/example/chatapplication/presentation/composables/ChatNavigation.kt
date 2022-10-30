package com.example.chatapplication.presentation.composables

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chatapplication.common.Screen
import com.example.chatapplication.presentation.screens.ChannelScreen
import com.example.chatapplication.presentation.screens.MessageScreen
import com.example.chatapplication.presentation.screens.SignInScreen
import com.example.chatapplication.presentation.viewmodels.AuthViewModel
import com.example.chatapplication.presentation.viewmodels.MessageViewModel

@Composable
fun ChatNavigation(navController: NavHostController) {
    val messageViewModel: MessageViewModel  = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()
    val startDestination =  if(authViewModel.isUserAuthenticated) Screen.ChannelScreen.route else Screen.SignInScreen.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(route = Screen.ChannelScreen.route){
            ChannelScreen(navController, messageViewModel)
        }
        composable(route = Screen.MessageScreen.route){
            MessageScreen(navController, messageViewModel)
        }
        composable(route = Screen.SignInScreen.route){
            SignInScreen(navController, authViewModel)
        }
    }
}
