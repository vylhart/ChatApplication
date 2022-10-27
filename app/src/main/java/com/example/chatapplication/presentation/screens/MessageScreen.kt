package com.example.chatapplication.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.presentation.composables.BackGroundCompose
import com.example.chatapplication.presentation.composables.ErrorText
import com.example.chatapplication.presentation.composables.FeatureColor
import com.example.chatapplication.presentation.viewmodels.ChannelState
import com.example.chatapplication.presentation.viewmodels.MessageViewModel


@Composable
fun MessageScreen(navController: NavHostController, viewModel: MessageViewModel){
    val state by viewModel.state.collectAsState()
    var messageText by remember { mutableStateOf("") }

    BackGroundCompose {
        Column(
            modifier = Modifier.fillMaxSize().padding(5.dp),
            verticalArrangement = Arrangement.Bottom
        ) {

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(13f)){
                items(state.messages){ msg ->
                    ListItem(msg, it, msg.senderId==state.userId)
                }
            }

            Row(
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = {messageText=it},
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.sendMessage(messageText)
                            messageText = ""
                        }) {
                            Icon(imageVector = Icons.Default.Send, contentDescription = "send button")
                        }
                    }
                )
            }

            HandleMessageState(state = state)

        }
    }
}

@Composable
fun ListItem(message: Message, featureColor: FeatureColor, alignRight: Boolean){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if(alignRight) Arrangement.End else Arrangement.Start
    ) {
        Text(text = message.data,
            color = Color.White,
            fontSize = MaterialTheme.typography.subtitle1.fontSize,
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(10.dp),
                    color = featureColor.extraDarkColor
                )
                .padding(10.dp)
        )
    }
    Spacer(modifier = Modifier.padding(5.dp))
}

@Composable
fun HandleMessageState(state: ChannelState) {
    if(state.error.isNotBlank()) {
        ErrorText(error = state.error)
    }
    if(state.isLoading) {
        CircularProgressIndicator()
    }
}
