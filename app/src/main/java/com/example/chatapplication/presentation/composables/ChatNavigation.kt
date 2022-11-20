package com.example.chatapplication.presentation.composables

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.chatapplication.common.Constants.CHANNEL_ID
import com.example.chatapplication.presentation.MainActivity
import com.example.chatapplication.presentation.Screen
import com.example.chatapplication.presentation.screens.auth.AuthScreen
import com.example.chatapplication.presentation.screens.auth.AuthViewModel
import com.example.chatapplication.presentation.screens.channel.ChannelScreen
import com.example.chatapplication.presentation.screens.contacts.ContactScreen
import com.example.chatapplication.presentation.screens.message.MessageScreen
import com.example.chatapplication.presentation.screens.status.StatusScreen

@Composable
fun ChatNavigation(navController: NavHostController, activity: MainActivity) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val startDestination =  if(authViewModel.isUserAuthenticated) Screen.MainScreen.route else Screen.SignInScreen.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(route = Screen.MessageScreen.route+"/{$CHANNEL_ID}"){
            MessageScreen(navController)
        }
        composable(route = Screen.SignInScreen.route){
            AuthScreen(navController, authViewModel, activity)
        }


        navigation(startDestination=Screen.ChannelScreen.route, route = Screen.MainScreen.route){
            composable(route = Screen.ChannelScreen.route){
                ChannelScreen(navController)
            }
            composable(route = Screen.ContactScreen.route){
                ContactScreen(navController)
            }
            composable(route = Screen.StatusScreen.route){
                StatusScreen(navController)
            }
        }
    }
}
