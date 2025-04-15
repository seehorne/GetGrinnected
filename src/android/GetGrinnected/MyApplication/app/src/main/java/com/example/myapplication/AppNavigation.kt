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
 * @param darkTheme the current state of the Theme of light or dark mode
 * @param onToggleTheme a lambda function passed down from previous screen that calls back to the
 * function instated in the call the AppNavigation in the MainActivity.
 * @param event the list of events read in from the api
 * @param eventnum the integer value of the length of events
 * @param startDestination is a string that tells us which screen to start at (this depends on
 * if a user is already logged in or not)
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(modifier: Modifier = Modifier,
                  darkTheme: Boolean,
                  onToggleTheme: (Boolean) -> Unit,
                  event: List<Event>,
                  eventnum: Int,
                  startDestination: String){
    // This handles our navigation system with a nav controller
    val navController = rememberNavController()
    // This instantiates our nave controller with a start destination dependent on whether
    // the user is logged in or not.
    NavHost(navController = navController, startDestination = startDestination){

        // These are routes that allow us to easily discern the screen we are navigating
        // to and to call just the string and not the name.
        composable("welcome"){
            WelcomeScreen(modifier, navController)
        }
        composable("signup"){
            SignupScreen(modifier, navController)
        }
        composable("login"){
            LoginScreen(modifier, navController)
        }
        // This is our home area with our navbar it acts as our view model in a sense
        // for navigating through the various logged in app screens.
        composable("main"){
            MainPage(modifier, darkTheme, onToggleTheme, event, eventnum, navController = navController)
        }

    }
}