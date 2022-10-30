package com.example.chatapplication.presentation.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun BackGroundCompose(content: @Composable (FeatureColor)-> Unit){
    val featureColor = getFeatureColor()
    /*val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = featureColor.extraDarkColor
    )*/

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = featureColor.darkColor),
        contentAlignment = Alignment.Center
    ) {
        val feature = getFeaturePath(constraints)
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawPath(path = feature.mediumPath, color = featureColor.mediumColor)
            drawPath(path = feature.lightPath, color = featureColor.lightColor)
        }
        content(featureColor)
    }
}
