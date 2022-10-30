package com.example.chatapplication.presentation.screens

import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.chatapplication.R
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Screen
import com.example.chatapplication.presentation.composables.BackGroundCompose
import com.example.chatapplication.presentation.composables.ErrorText
import com.example.chatapplication.presentation.viewmodels.AuthState
import com.example.chatapplication.presentation.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider.getCredential

@Composable
fun SignInScreen(navController: NavHostController, viewModel: AuthViewModel) {
    Log.d(TAG, "SignInScreen: ")
    val navigateToChannelScreen = {
        navController.navigate(Screen.ChannelScreen.route){
            popUpTo(Screen.SignInScreen.route){
                inclusive = true
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(StartIntentSenderForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            try {
                val credentials = viewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                val googleIdToken = credentials.googleIdToken
                val googleCredentials = getCredential(googleIdToken, null)
                viewModel.firebaseSignIn(googleCredentials, navigateToChannelScreen)
            } catch (e: ApiException) {
                Log.e(TAG, "SignInScreen: ", e)
            }
        }
    }

    fun callback(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    HandleSignInState(viewModel.state.collectAsState())

    SignInScreenComponent {
        viewModel.oneTapSignIn(callback = { callback(it) })
    }
}


@Composable
fun SignInScreenComponent(onClick: () -> Unit) {
    BackGroundCompose {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Chat App",
                fontSize = MaterialTheme.typography.h2.fontSize,
                color = Color.White
            )

            Button(
                modifier = Modifier.padding(bottom = 48.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = it.extraDarkColor
                ),
                onClick = onClick
            ) {
                Image(
                    painter = painterResource(
                        id = R.drawable.ic_google_logo
                    ),
                    contentDescription = null
                )
                Text(
                    text = "Sign in with Google",
                    modifier = Modifier.padding(6.dp),
                    fontSize = 18.sp
                )
            }
        }

    }
}

@Composable
fun HandleSignInState(
    state: State<AuthState>
) {
    if(state.value.error.isNotBlank()) {
        ErrorText(error = state.value.error)
    }
    if(state.value.isLoading){
        CircularProgressIndicator()
    }
}

