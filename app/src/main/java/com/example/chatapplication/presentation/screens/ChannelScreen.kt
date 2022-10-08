package com.example.chatapplication.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chatapplication.common.Screen
import com.example.chatapplication.presentation.viewmodels.MessageViewModel

@Composable
fun ChannelScreen(navController: NavHostController, viewModel: MessageViewModel){

    val channelName = remember{ mutableStateOf("")}
    val userName = remember{ mutableStateOf("")}
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            OutlinedTextField(
                value = userName.value,
                onValueChange = {userName.value=it},
            )
            OutlinedTextField(
                value = channelName.value,
                onValueChange = {channelName.value=it}
            )
            Button(onClick = {
                navController.navigate(Screen.MessageScreen.route)
                viewModel.enterChannel(channel = channelName.value, userId = userName.value)

            }) {
                Text(text = "Enter")
            }

        }
    }
}

