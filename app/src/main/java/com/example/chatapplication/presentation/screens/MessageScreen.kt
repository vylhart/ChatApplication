package com.example.chatapplication.presentation.screens

import android.util.Log
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.presentation.viewmodels.MessageViewModel

@Composable
fun MessageScreen(navController: NavHostController, viewModel: MessageViewModel ){
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text =   state.userId+state.messages.size+state.channelId+state.error)
            Button(onClick = {
                viewModel.onClick()
            }) {
                Text(text = "click")
            }
            LazyColumn(modifier = Modifier.fillMaxWidth()){
                items(state.messages){ msg ->
                    Log.d(TAG, "MessageScreen: "+msg.messageId)
                    ListItem(msg)
                }
            }
            if(state.error.isNotBlank()) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            }
            if(state.isLoading) {
                CircularProgressIndicator()
                Log.d(TAG, "MessageScreen: loading")
            }

        }
    }
}

@Composable
fun ListItem(message: Message){
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = "item"+message.messageId)
    }
}

