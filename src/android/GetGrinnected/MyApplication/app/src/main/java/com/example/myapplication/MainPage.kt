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
 *  @param event is the list of the events we read in from the api
 *  @param eventnum is the length of the list of the events
 *  @param navController this is the nav Controller to our login flow process to be used in sign out functionality
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainPage(
    modifier: Modifier = Modifier,
    darkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    event: List<Event>,
    eventnum: Int,
    tags: MutableList<Check>,
    navController: NavController
) {
    // Creates our navbar
    val bottomNavController = rememberNavController()

    // Creates a list of navItems including a name and an icon associated to display on the navbar
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Calendar", Icons.Default.DateRange),
        NavItem("Favorites", Icons.Default.Favorite),
        NavItem("Settings", Icons.Default.Settings),
    )
    // For demo purposes only
    val sampleEvents = listOf(
        Event(eventid= 2, event_name = "Crafternoon", event_description =  "Lots of fun arts and crafts", event_location = "Downtown Theater", organizations = listOf("NAMI"), rsvp = 0, event_date ="2025-05-01", event_start_time = "6:30 PM", event_private = 0, event_end_time = "8:00 PM", event_time ="8:00 PM", tags =listOf("art, fun"), is_draft = 0,repeats = 0, event_image = "h", event_all_day = 0, is_favorited = true),
    )
    // For demo purposes only
    val sampleOrgs = listOf(
        User(1, "test", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1),
        User(1, "test2", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1, true),
        User(1, "test3", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1, true),
        User(1, "test4", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1, true),
        User(1, "test5", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1, true),
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
            composable("Home") { HomeScreen(
                event = event, eventnum = eventnum, tags = tags
            ) }
            composable("Calendar") { CalendarScreen() }
            composable("Favorites") { FavoritesScreen(events = sampleEvents) }
            composable("Settings") { SettingsScreen(orgs = sampleOrgs, account = User(1, "User123", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1), darkTheme = darkTheme, onToggleTheme = onToggleTheme, navController = navController ) }
        }
    }
}
