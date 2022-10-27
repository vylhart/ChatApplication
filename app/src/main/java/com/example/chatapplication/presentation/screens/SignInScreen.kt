package com.example.chatapplication.presentation.screens

import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.chatapplication.R
import com.example.chatapplication.common.Constants.TAG
import com.example.chatapplication.common.Screen
import com.example.chatapplication.presentation.composables.BackGroundCompose
import com.example.chatapplication.presentation.composables.FeatureColor
import com.example.chatapplication.presentation.composables.getFeatureColor
import com.example.chatapplication.presentation.composables.getFeaturePath
import com.example.chatapplication.presentation.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider.getCredential


@Composable
fun SignInScreen(navController: NavHostController, viewModel: AuthViewModel) {
    val launcher = rememberLauncherForActivityResult(StartIntentSenderForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            try {
                val credentials = viewModel.oneTapClient.getSignInCredentialFromIntent(result.data)
                val googleIdToken = credentials.googleIdToken
                val googleCredentials = getCredential(googleIdToken, null)
                viewModel.firebaseSignIn(googleCredentials)
            } catch (e: ApiException) {
                Log.e(TAG, "SignInScreen: ", e)
            }
        }
    }
    fun callback(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }


    val state = viewModel.state.collectAsState()
    if(state.value.isSignedIn){
        Log.d(TAG, "SignInScreen: ${state.value.isSignedIn}")
        LaunchedEffect(true){
            navController.navigate(Screen.ChannelScreen.route)
        }
    }
    SignInScreenComponent {
        viewModel.oneTapSignIn(callback = { callback(it) })
    }
}


@Composable
fun SignInScreenComponent(onClick: () -> Unit) {
    BackGroundCompose {
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




