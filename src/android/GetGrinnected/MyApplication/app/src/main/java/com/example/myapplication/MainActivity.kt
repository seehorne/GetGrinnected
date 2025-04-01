package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.runtime.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                    MainPage()
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
        if (currentScreen != "welcome") {
            Button(
                onClick = { currentScreen = "welcome" },
                modifier = Modifier.align(Alignment.Start).padding(8.dp)
            ) {
                Text("Back")
            }
        }

        when (currentScreen) {
            "welcome" -> WelcomeScreen(
                onLoginClick = { currentScreen = "login" },
                onSignupClick = { currentScreen = "signup" }
            )
            "login" -> LoginScreen { _, _ -> }
            "signup" -> SignupScreen { _, _, _ -> }
        }
    }
}