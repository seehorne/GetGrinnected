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
import androidx.compose.runtime.*

/**
 * Our main used to run and create our app. Currently utilizes the AppNavigator function at
 * creation.
 */
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

/**
 * A composable function used to navigate based on the currentScreen. (This utilizes the lambda function
 * parameters we made for the login, welcome and signup screens).
 */

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