package com.example.chatapplication.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Screen
import com.example.chatapplication.presentation.composables.BackGroundCompose
import com.example.chatapplication.presentation.viewmodels.MessageViewModel

@Composable
fun ChannelScreen(navController: NavHostController, viewModel: MessageViewModel = hiltViewModel()){
    Log.d(TAG, "ChannelScreen: ")
    val enterChannel = { name:String -> viewModel.enterChannel(channel = name) }
    ChannelScreenContent(navController = navController, enterChannel)
}

@Composable
fun ChannelScreenContent(navController: NavHostController, enterChannel: (String) -> Unit){
    val channelName = remember{ mutableStateOf("")}

    BackGroundCompose {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            OutlinedTextField(
                value = channelName.value,
                onValueChange = { channelName.value = it },
                label = {
                    Text(text = "Enter Channel Name")
                }
            )
            Button(
                onClick = {
                    navController.navigate(Screen.MessageScreen.route)
                    enterChannel(channelName.value)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = it.extraDarkColor),
            ) {
                Text(text = "Enter")
            }
        }
    }
}
