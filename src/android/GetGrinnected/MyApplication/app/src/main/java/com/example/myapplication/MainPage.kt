package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.SearchViewModel
import screens.FavoritesScreen
import screens.HomeScreen
import screens.SearchScreen
import screens.SettingsScreen


/**
 * A composable function that displays the main page of the app with a bottom navbar.
 *
 * It sets up the primary UI for our logged in app experience (Navigation between Homepage,
 * Calendar, Favorites and Settings)
 *
 *  @param modifier Modifier to be applied to the root layout.
 *  @param darkTheme the current state of the Theme of light or dark mode
 *  @param onToggleTheme a lambda function passed down from previous screen that calls back to the
 *  function instated in the call the AppNavigation in the MainActivity.
 *  @param navController this is the nav Controller to our login flow process to be used in sign out functionality
 *  @param fontSizeSetting String associated with the current font size setting we have selected
 *  @param onFontSizeChange String call back function that allows us to change the state of the font size
 */

@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainPage(
    modifier: Modifier = Modifier,
    darkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    tags: List<Check>,
    navController: NavController,
    account: AccountEntity,
    fontSizeSetting: String,
    onFontSizeChange: (String) -> Unit
) {
    // Creates our navbar
    val bottomNavController = rememberNavController()

    // Creates a list of navItems including a name and an icon associated to display on the navbar
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Search", Icons.Default.Search),
        NavItem("Favorites", Icons.Default.Favorite),
        NavItem("Settings", Icons.Default.Settings),
    )

    // Creates a scaffold composable to work from
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                // This value tells us what our next back stack entry would be.
                val currentDestination = bottomNavController.currentBackStackEntryAsState().value?.destination?.route
                // Handles the tracing of the back stack for when a user use the back button
                navItemList.forEach { navItem ->
                    NavigationBarItem(
                        selected = currentDestination == navItem.label,
                        onClick = {
                            bottomNavController.navigate(navItem.label) {
                                //pops current item on the back stack
                                popUpTo(bottomNavController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(navItem.icon, contentDescription = navItem.label) },
                        label = { Text(navItem.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // NavHost to navigate us when we selected a given icon using the navbar
        NavHost(
            navController = bottomNavController,
            startDestination = "home",
            modifier = modifier.padding(innerPadding)
        ) {
            // Set of routes to for our navbar to follow
            composable("Home") { HomeScreen(tags = tags) }

            composable("Search") { SearchScreen(viewModel = SearchViewModel()) }


            composable("Favorites") { FavoritesScreen() }
            composable("Settings") { SettingsScreen(account = account.toUser(), darkTheme = darkTheme, onToggleTheme = onToggleTheme, navController = navController, fontSizeSetting = fontSizeSetting, onFontSizeChange = onFontSizeChange ) }

        }
    }
}
