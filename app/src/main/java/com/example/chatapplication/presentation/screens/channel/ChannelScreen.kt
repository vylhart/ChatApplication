package com.example.chatapplication.presentation.screens.channel

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.chatapplication.R
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.data.model.User
import com.example.chatapplication.domain.model.Channel
import com.example.chatapplication.presentation.Screen
import com.example.chatapplication.presentation.composables.BackGroundCompose
import com.example.chatapplication.presentation.composables.getFeatureColor
import com.example.chatapplication.presentation.screens.main.BottomBar
import kotlin.math.min

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChannelScreen(navController: NavHostController, viewModel: ChannelViewModel = hiltViewModel()){
    Log.d(TAG, "ChannelScreen: ")
    val state by viewModel.state.collectAsState()
    Scaffold(topBar = { BottomBar(navController = navController, featureColor = getFeatureColor())}) {
        ChannelScreenContent(state){ channelID: String ->
            navController.navigate(Screen.MessageScreen.route + "/$channelID")
        }
    }
}

@Composable
fun ChannelScreenContent(state: ChannelState, onClick: (String) -> Unit){
    val channelName = remember{ mutableStateOf("")}
    BackGroundCompose {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()){
                items(state.channels){ channel ->
                    ChannelItem(channel = channel, state.userID, onClick)
                }
            }
        }
    }

}

@Composable
fun ChannelItem(channel: Channel, currentUserID: String, onClick: (String) -> Unit){
    var otheruser: User? = null
    for (user in channel.users){
        Log.d(TAG, "ChannelItem: ${user.uid}")
        otheruser = user
        if(user.uid != currentUserID){
            break
        }
    }
        Spacer(modifier = Modifier.padding(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(all = 3.dp))
            CoilImage(otheruser?.photoURL)
            Column(modifier = Modifier
                .fillMaxHeight()
                .padding(start = 10.dp)
                .weight(5f)
                .clickable { onClick(channel.channelID) }
            ) {
                Text(
                    text = otheruser?.name?: "User",
                    fontWeight = FontWeight.Bold
                )
                Text(text = channel.lastMessage.data)
            }
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(1f),
                text = DateUtils.getRelativeTimeSpanString(channel.lastMessage.date, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString(),
                fontSize = 10.sp
            )
        }

}

@Preview(showBackground = true)
@Composable
fun PreviewScreen(){
    ChannelScreenContent(
        state = ChannelState(
            userID = "dfd",
            channels = listOf(
                Channel(
                    channelID = "sdfsd",
                    users = mutableListOf(
                        User(
                            uid = "dfsdf",
                            name = "Shashank",
                        )
                    )
                )
            )
        ),
        onClick = {})
}

@Composable
fun CoilImage(photoURL: String?) {
    if(photoURL!=null){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photoURL)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_user),
            contentDescription = stringResource(R.string.project_id),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxHeight()

        )
    }
    else{
        Image(
            painter = painterResource(id = R.drawable.ic_user),
            contentDescription = "avatar",
            modifier = Modifier
                .clip(CircleShape)
                .width(45.dp)
                .height(45.dp)
        )
    }
}