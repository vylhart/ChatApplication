package com.example.chatapplication.presentation.screens.auth


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.presentation.MainActivity
import com.example.chatapplication.presentation.Screen
import com.example.chatapplication.presentation.composables.BackGroundCompose
import com.example.chatapplication.presentation.composables.ErrorText


@Composable
fun AuthScreen(navController: NavHostController, viewModel: AuthViewModel, activity: MainActivity) {
    Log.d(TAG, "AuthScreen: ")
    val state = viewModel.state.collectAsState()
    val navigateToChannelScreen = {
        navController.navigate(Screen.MainScreen.route){
            popUpTo(Screen.SignInScreen.route){
                inclusive = true
            }
        }
    }



    HandleSignInState(state.value)
    SignInScreenContent(state.value) {
        if(!state.value.codeSent && !state.value.isSignedIn){
            Log.d(TAG, "AuthScreen: 1")
            viewModel.beginSignIn(it, activity)
        }
        else if(state.value.codeSent && !state.value.isSignedIn){
            Log.d(TAG, "AuthScreen: 2")
            viewModel.firebaseSignIn(it)
        }
        else if(state.value.isSignedIn && state.value.isNewUser){
            Log.d(TAG, "AuthScreen: 3")
            viewModel.addNewUser(it, navigateToChannelScreen)
        }
        else{
            navigateToChannelScreen()
        }
    }
}


@Composable
fun SignInScreenContent(state: AuthState, onClick: (String) -> Unit) {
    Log.d(TAG, "SignInScreenContent: ")
    var input by remember { mutableStateOf("") }

    BackGroundCompose {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text = "Chat App",
                fontSize = MaterialTheme.typography.h2.fontSize,
                color = Color.White
            )

            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text(text =

                    if(state.codeSent && !state.isSignedIn){
                        "Enter the code"
                    }
                    else if(state.isSignedIn){
                        "Enter your name"
                    }
                    else{
                        "Enter phone number"
                    }
                    )},
                trailingIcon = {
                    IconButton(onClick = {
                        if(input.isNotEmpty()){
                            onClick(input)
                        }
                        input = ""
                    }) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "send button")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
            )
        }
    }
}

@Composable
fun HandleSignInState(
    state: AuthState
) {
    if(state.error.isNotBlank()) {
        ErrorText(error = state.error)
    }
    if(state.isLoading){
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun PreviewSignIn(){
    SignInScreenContent(AuthState()) {
        {}
    }
}

