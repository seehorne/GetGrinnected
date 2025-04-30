package com.example.myapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myapplication.FontSizePrefs

/*
This came almost entirely from this article https://canlioya.medium.com/dynamic-font-sizes-with-jetpack-compose-2665c65d78f7
I found an extension as well that did it for all the font sizes and possibilities in the material theme library to cover
all bases.
 */
private const val lineHeightMultiplier = 1.15

fun getPersonalizedTypography(fontSizePrefs: FontSizePrefs): Typography {
    fun baseStyle(
        baseSize: Int,
        weight: FontWeight,
        letterSpacing: Float = 0f
    ) = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = weight,
        fontSize = (baseSize + fontSizePrefs.fontSizeExtra).sp,
        lineHeight = ((baseSize + fontSizePrefs.fontSizeExtra) * lineHeightMultiplier).sp,
        letterSpacing = letterSpacing.sp
    )

    return Typography(
        displayLarge = baseStyle(57, FontWeight.Normal),
        displayMedium = baseStyle(45, FontWeight.Normal),
        displaySmall = baseStyle(36, FontWeight.Normal),

        headlineLarge = baseStyle(32, FontWeight.Normal),
        headlineMedium = baseStyle(28, FontWeight.Normal),
        headlineSmall = baseStyle(24, FontWeight.Normal),

        titleLarge = baseStyle(22, FontWeight.Bold),
        titleMedium = baseStyle(16, FontWeight.SemiBold),
        titleSmall = baseStyle(14, FontWeight.Medium),

        bodyLarge = baseStyle(16, FontWeight.Normal, letterSpacing = 0.5f),
        bodyMedium = baseStyle(14, FontWeight.Normal, letterSpacing = 0.25f),
        bodySmall = baseStyle(12, FontWeight.Normal, letterSpacing = 0.4f),

        labelLarge = baseStyle(14, FontWeight.Medium, letterSpacing = 0.1f),
        labelMedium = baseStyle(12, FontWeight.Medium, letterSpacing = 0.5f),
        labelSmall = baseStyle(11, FontWeight.Medium, letterSpacing = 0.5f)
    )
}