package com.example.chatapplication.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.chatapplication.domain.model.Message
import com.example.chatapplication.presentation.composables.FeatureColor
import com.example.chatapplication.presentation.composables.getFeatureColor
import com.example.chatapplication.presentation.composables.getFeaturePath
import com.example.chatapplication.presentation.viewmodels.MessageViewModel


@Composable
fun MessageScreen(navController: NavHostController, viewModel: MessageViewModel){
    val state by viewModel.state.collectAsState()
    val featureColor = getFeatureColor()
    var messageText by remember { mutableStateOf("") }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = featureColor.darkColor),
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
                .padding(5.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth().weight(13f)){
                items(state.messages){ msg ->
                    ListItem(msg, featureColor, msg.senderId==state.userId)
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
            }
            Row(
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = {messageText=it},
                    modifier = Modifier.weight(4f)
                )
                Button(
                    onClick = {
                        viewModel.sendMessage(messageText)
                        messageText = ""
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = featureColor.extraDarkColor)
                ) {
                    Text(text = "Send")
                }
            }

        }

    }
}

@Composable
fun ListItem(message: Message, featureColor: FeatureColor, alignRight: Boolean){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = if(alignRight) Arrangement.End else Arrangement.Start

    ) {
        Text(text = message.data,
            color = Color.White,
            fontSize = MaterialTheme.typography.subtitle1.fontSize,
            modifier = Modifier
                .background(
                    shape = RoundedCornerShape(10.dp),
                    color = featureColor.extraDarkColor
                ).padding(10.dp)
        )
    }
    Spacer(modifier = Modifier.padding(5.dp))
}

@Composable
@Preview(showBackground = true)
fun PreviewScreen(){
    val msg = Message(
        messageId = "fsdfds",
        senderId = "sdfsdf",
        data = "sdfsdf",
    )
    Column(modifier = Modifier.fillMaxSize()) {
        ListItem(message = msg, featureColor = getFeatureColor(), alignRight = true)
    }
}

