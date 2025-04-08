package com.example.myapplication

import android.os.Build
import androidx.annotation.RequiresApi
import screens.CalendarScreen
import screens.FavoritesScreen
import screens.HomeScreen
import screens.SettingsScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
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

/**
 * A composable function that displays the main page of the app with a bottom navbar.
 *
 * It sets up the primary UI for our logged in app experience (Navigation between Homepage,
 * Calendar, Favorites and Settings)
 *
 * @param modifier Modifier to be applied to the root layout.
 *
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainPage(modifier: Modifier = Modifier, event: List<Event>, navController: NavController) {
    val bottomNavController = rememberNavController()

    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Calendar", Icons.Default.DateRange),
        NavItem("Favorites", Icons.Default.Favorite),
        NavItem("Settings", Icons.Default.AccountCircle),
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val currentDestination = bottomNavController.currentBackStackEntryAsState().value?.destination?.route
                navItemList.forEach { navItem ->
                    NavigationBarItem(
                        selected = currentDestination == navItem.label,
                        onClick = {
                            bottomNavController.navigate(navItem.label) {
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
        NavHost(
            navController = bottomNavController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("Home") { HomeScreen(event) }
            composable("Calendar") { CalendarScreen() }
            composable("Favorites") { FavoritesScreen() }
            composable("Settings") { SettingsScreen() }
        }
    }
}
