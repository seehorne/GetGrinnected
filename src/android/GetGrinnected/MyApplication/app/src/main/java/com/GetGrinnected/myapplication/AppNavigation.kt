package com.GetGrinnected.myapplication

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
 * @param startDestination is a string that tells us which screen to start at (this depends on
 * if a user is already logged in or not)
 * @param fontSizeSetting String associated with the current font size setting we have selected
 * @param onFontSizeChange String call back function that allows us to change the state of the font size
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    darkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    startDestination: String,
    tags: List<Check>,
    fontSizeSetting: String,
    onFontSizeChange: (String) -> Unit
  // This handles our navigation system with a nav controller
){

    // Gets current active account from App Repository
    val account = AppRepository.currentAccount.value

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
            // Makes sure account is not null just since it could be to start as someone who hasn't logged in
            if (account != null) {
                MainPage(modifier, darkTheme, onToggleTheme, tags = tags, navController = navController, account = account, fontSizeSetting = fontSizeSetting, onFontSizeChange =  onFontSizeChange)
            }
        }
        composable(
            "verification/{email}/{previous}",
            arguments = listOf(
                navArgument("email") { type = NavType.StringType },
                navArgument("previous"){ type = NavType.StringType },

            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val previous = backStackEntry.arguments?.getString("previous") ?: ""

            EmailVerificationScreen(email, previous, navController = navController)
        }
    }
}