package com.example.chatapplication.presentation.screens.channel

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.chatapplication.R
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Constants.link
import com.example.chatapplication.common.Screen
import com.example.chatapplication.domain.model.Channel
import com.example.chatapplication.domain.model.User
import com.example.chatapplication.presentation.composables.BackGroundCompose

@Composable
fun ChannelScreen(navController: NavHostController, viewModel: ChannelViewModel = hiltViewModel()){
    Log.d(TAG, "ChannelScreen: ")
    val state = viewModel.state.collectAsState()
    ChannelScreenContent(state){ channelID: String ->
        navController.navigate(Screen.MessageScreen.route)
        viewModel.onClick(channelID)
    }
}

@Composable
fun ChannelScreenContent(state: State<ChannelState>, onClick: (String) -> Unit){
    val channelName = remember{ mutableStateOf("")}

    BackGroundCompose {
        Box(
            modifier = Modifier
                .fillMaxSize(),
                //.verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {

            OutlinedTextField(
                value = channelName.value,
                onValueChange = { channelName.value = it },
                label = {
                    Text(text = "Join Channel")
                },
                trailingIcon = {
                    IconButton(onClick = {
                        onClick(channelName.value)
                    }) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "send button")
                    }
                }
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ){
                items(state.value.channels){ channel ->
                    ChannelItem(channel = channel, state.value.userID, onClick)

                }
            }
        }
    }
}

@Composable
fun ChannelItem(channel: Channel, currentUserID: String, onClick: (String) -> Unit){
    var otheruser: User? = null
    for (user in channel.users){
        if(user.uid != currentUserID){
            otheruser = user
            break
        }
    }
    otheruser?.let {
        Row(
            modifier = Modifier.fillMaxWidth()
                                .height(45.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //AsyncImage(model = link  , contentDescription = null)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(link)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_user),
                contentDescription = stringResource(R.string.project_id),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .clip(CircleShape)
                    .weight(1f)
                    .fillMaxHeight()
            )
            Column(modifier = Modifier
                .fillMaxHeight()
                .padding(start = 10.dp)
                .weight(5f)
                .clickable { onClick(channel.channelID) }
            ) {
                Text(
                    text = "Shashank",
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Hello World")
            }
            Text(
                modifier = Modifier.wrapContentHeight().weight(1f),
                text = "10:55",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScreen(){
    //ChannelItem(channel = Channel(users = listOf(User(uid = "sdfsd"))), "fdsdf", {})
}

