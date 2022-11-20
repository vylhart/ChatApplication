package com.example.chatapplication.presentation.composables

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Constraints
import com.example.chatapplication.presentation.theme.*
import kotlin.math.abs

val features= listOf(
    FeatureColor(
        BlueViolet1,
        BlueViolet2,
        BlueViolet3,
        BlueViolet4
    ),
    FeatureColor(
        LightGreen1,
        LightGreen2,
        LightGreen3,
        LightGreen4
    ),
    FeatureColor(
        OrangeYellow1,
        OrangeYellow2,
        OrangeYellow3,
        OrangeYellow4
    ),
    FeatureColor(
        Beige1,
        Beige2,
        Beige3,
        Beige4
    ),
)

fun getFeatureColor(): FeatureColor {
    val rand = (0..3).random()
    return features[0]
}

fun getFeaturePath(constraints: Constraints): FeaturePath {
    val width = constraints.maxWidth
    val height = constraints.maxHeight

    val mediumPoint1 = Offset(0f, height * 0.3f)
    val mediumPoint2 = Offset(width * 0.1f, height * 0.35f)
    val mediumPoint3 = Offset(width * 0.4f, height * 0.05f)
    val mediumPoint4 = Offset(width * 0.75f, height * 0.7f)
    val mediumPoint5 = Offset(width * 1.4f, -height.toFloat())

    val mediumPath = Path().apply {
        moveTo(mediumPoint1.x, mediumPoint1.y)
        standardQuadTo(mediumPoint1, mediumPoint2)
        standardQuadTo(mediumPoint2, mediumPoint3)
        standardQuadTo(mediumPoint3, mediumPoint4)
        standardQuadTo(mediumPoint4, mediumPoint5)
        lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
        lineTo(-100f, height.toFloat() + 100f)
        close()
    }
    val lightPoint1 = Offset(0f, height * 0.35f)
    val lightPoint2 = Offset(width * 0.1f, height * 0.4f)
    val lightPoint3 = Offset(width * 0.3f, height * 0.35f)
    val lightPoint4 = Offset(width * 0.65f, height.toFloat())
    val lightPoint5 = Offset(width * 1.4f, -height.toFloat() / 3f)

    val lightPath = Path().apply {
        moveTo(lightPoint1.x, lightPoint1.y)
        standardQuadTo(lightPoint1, lightPoint2)
        standardQuadTo(lightPoint2, lightPoint3)
        standardQuadTo(lightPoint3, lightPoint4)
        standardQuadTo(lightPoint4, lightPoint5)
        lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
        lineTo(-100f, height.toFloat() + 100f)
        close()
    }
    return FeaturePath(
        lightPath = lightPath,
        mediumPath = mediumPath
    )
}

fun Path.standardQuadTo(from: Offset, to: Offset){
    quadraticBezierTo(
        from.x,
        from.y,
        abs(from.x + to.x) / 2f,
        abs(from.y + to.y) / 2f
    )
}

data class FeaturePath(
    val lightPath: Path,
    val mediumPath: Path
)

data class FeatureColor(
    val lightColor: Color,
    val mediumColor: Color,
    val darkColor: Color,
    val extraDarkColor: Color
)