package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.ui.Alignment
import androidx.compose.material3.*
import androidx.compose.runtime.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                    AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
    var currentScreen by remember { mutableStateOf("welcome") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (currentScreen) {
            "welcome" -> WelcomeScreen(
                onLoginClick = { currentScreen = "login" },
                onSignupClick = { currentScreen = "signup" }
            )
            "signup" -> SignupScreen(
                onSignInClick = { currentScreen = "login"},
                onSignupClick = {currentScreen = "mainpage"}
            )
            "login" -> LoginScreen(
                onLoginClick = { currentScreen = "mainpage"},
                onSignupClick = {currentScreen = "signup"})
            "mainpage" -> MainPage()
        }
    }
}