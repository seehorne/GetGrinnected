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
fun MainPage(modifier: Modifier = Modifier, navController: NavController) {
    val bottomNavController = rememberNavController()

    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Calendar", Icons.Default.DateRange),
        NavItem("Favorites", Icons.Default.Favorite),
        NavItem("Settings", Icons.Default.AccountCircle),
    )
    val sampleEvents = listOf(
        Event(1, "Concert Night", "Live music and fun really long description to really push the sizing as far as I can to see waht it would look like if you did this and had a longer description for an event just in general for any normal event!", "Downtown Theater",listOf("Music Club"), 0,"2025-04-20", "8:00 PM", 0,"8:00 PM", "8:00 PM", listOf("music", "fun"), 0, 0,"h", 0,true),
        Event(2, "Crafternoon", "Lots of fun arts and crafts", "Downtown Theater",listOf("NAMI"), 0,"2025-05-01", "6:30 PM", 0,"8:00 PM", "8:00 PM", listOf("art, fun"), 0,0,"h", 0),
        Event(3, "Concert Night2", "Live music and fun!", "Downtown Theater",listOf("Music Club"), 0,"2025-04-20", "8:00 PM", 0,"8:00 PM", "8:00 PM", listOf("music", "fun"), 0,0,"h", 0, true),
        Event(4, "Concert Night3", "Live music and fun!", "Downtown Theater",listOf("Music Club"), 0,"2025-04-20", "8:00 PM", 0,"8:00 PM", "8:00 PM", listOf("music", "fun"), 0,0,"h",0, true),
        Event(5, "Concert Night4", "Live music and fun!", "Downtown Theater",listOf("Music Club"), 0,"2025-04-20", "8:00 PM", 0,"8:00 PM", "8:00 PM", listOf("music", "fun"), 0,0,"h",0, true),
        Event(6, "Concert Night5", "Live music and fun!", "Downtown Theater",listOf("Music Club"), 0,"2025-04-20", "8:00 PM", 0,"8:00 PM", "8:00 PM", listOf("music", "fun"), 0,0,"h", 0, true),
        Event(7, "Concert Night6", "Live music and fun!","Downtown Theater", listOf("Music Club"), 0,"2025-04-20", "8:00 PM", 0,"8:00 PM", "8:00 PM", listOf("music", "fun"), 0,0,"h", 0, true),
        Event(8, "Concert Night7", "Live music and fun!","Downtown Theater", listOf("Music Club"), 0,"2025-04-20", "8:00 PM",  0,"8:00 PM", "8:00 PM", listOf("music", "fun"), 0,0,"h",0, true),
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
            composable("Home") { HomeScreen() }
            composable("Calendar") { CalendarScreen() }
            composable("Favorites") { FavoritesScreen(events = sampleEvents) }
            composable("Settings") { SettingsScreen() }
        }
    }
}
