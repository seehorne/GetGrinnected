package com.GetGrinnected.myapplication.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.GetGrinnected.myapplication.FontSizePrefs

private val DarkColorScheme = darkColorScheme(
    primary = NavyBlue,
    onPrimary = White,
    secondary = GrayishBlue,
    secondaryContainer = VeryDarkGray,
    tertiary = BabyBlue,
    background = BluishBlack,
    onBackground = White,
)

private val LightColorScheme = lightColorScheme(
    primary = NavyBlue,
    onPrimary = White,
    secondary = GrayishBlue,
    secondaryContainer = OffWhiteBlue,
    tertiary = NavyBlue,
    background = White,
    onBackground = Black,
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    fontSizePrefs: FontSizePrefs = FontSizePrefs.DEFAULT,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getPersonalizedTypography(fontSizePrefs),
        content = content
    )
}