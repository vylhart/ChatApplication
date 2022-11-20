package com.example.chatapplication.presentation.screens.status

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.chatapplication.presentation.composables.BackGroundCompose
import com.example.chatapplication.presentation.composables.getFeatureColor
import com.example.chatapplication.presentation.screens.main.BottomBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StatusScreen(navController: NavHostController) {
    Scaffold(topBar = { BottomBar(navController = navController, getFeatureColor())}) {
        BackGroundCompose {
            Text(text = "status")
        }
    }
}