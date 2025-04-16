package com.example.myapplication

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import screens.EmailVerificationScreen
import screens.LoginScreen
import screens.SignupScreen
import screens.WelcomeScreen

/**
 * A composable function that is utilized for smooth navigation through login/signup process
 * @param modifier the modifier to be applied to page layouts navigated to.
 * @param darkTheme the current state of the Theme of light or dark mode
 * @param onToggleTheme a lambda function passed down from previous screen that calls back to the
 * function instated in the call the AppNavigation in the MainActivity.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(modifier: Modifier = Modifier,
                  darkTheme: Boolean,
                  onToggleTheme: (Boolean) -> Unit,
                  event: List<Event>,
                  eventnum: Int){
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
            MainPage(modifier, darkTheme, onToggleTheme, event, eventnum)
        }
        composable(
            "verification/{email}/{flag}/{username}",
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("flag"){ type = NavType.BoolType },

            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val flag = backStackEntry.arguments?.getBoolean("flag") ?: false

            EmailVerificationScreen(email, flag, username, navController = navController)
        }
    }
}