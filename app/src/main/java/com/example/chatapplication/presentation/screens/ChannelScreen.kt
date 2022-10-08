package com.example.chatapplication.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chatapplication.common.Screen
import com.example.chatapplication.presentation.composables.getFeatureColor
import com.example.chatapplication.presentation.composables.getFeaturePath
import com.example.chatapplication.presentation.viewmodels.MessageViewModel


@Composable
fun ChannelScreen(navController: NavHostController, viewModel: MessageViewModel = hiltViewModel()){
    val featureColor = getFeatureColor()
    val channelName = remember{ mutableStateOf("")}
    val userName = remember{ mutableStateOf("")}
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = featureColor.darkColor)
        ){
        val feature = getFeaturePath(constraints)
        Canvas(
            modifier = Modifier.fillMaxSize()
        ){
            drawPath(path = feature.mediumPath, color = featureColor.mediumColor)
            drawPath(path = feature.lightPath, color = featureColor.lightColor)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Chat App",
                fontSize = MaterialTheme.typography.h2.fontSize,
                color = Color.White
            )
            OutlinedTextField(
                value = userName.value,
                onValueChange = {userName.value=it},
                label = {
                    Text(text = "Enter username")
                }
            )
            OutlinedTextField(
                value = channelName.value,
                onValueChange = {channelName.value=it},
                label = {
                    Text(text = "Enter Channel Name")
                }
            )
            Button(
                onClick = {
                    navController.navigate(Screen.MessageScreen.route)
                    viewModel.enterChannel(channel = channelName.value, userId = userName.value)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = featureColor.extraDarkColor),
            ) {
                Text(text = "Enter")
            }
        }
    }
}


