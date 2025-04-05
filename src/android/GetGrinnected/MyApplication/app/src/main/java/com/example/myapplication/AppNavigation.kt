package com.example.myapplication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import screens.LoginScreen
import screens.SignupScreen
import screens.WelcomeScreen

/**
 * A composable function that is utilized for smooth navigation through login/signup process
 * @param modifier the modifier to be applied to page layouts navigated to.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(modifier: Modifier = Modifier){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcome"){

        composable("welcome"){
            WelcomeScreen(modifier, navController)
        }
        composable("signup"){
            SignupScreen(modifier, navController)
        }
        composable("login"){
            LoginScreen(modifier, navController)
        }
        composable("main"){
            MainPage(modifier, navController)
        }

    }
}