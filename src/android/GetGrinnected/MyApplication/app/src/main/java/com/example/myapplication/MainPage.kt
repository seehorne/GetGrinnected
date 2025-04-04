package com.example.myapplication

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/**
 * A composable function that displays the main page of the app with a bottom navbar.
 *
 * It sets up the primary UI for our logged in app experience (Navigation between Homepage,
 * Calendar, Favorites and Settings)
 *
 * @param modifier Modifier to be applied to the root layout.
 *
 */

@Composable
fun MainPage(modifier: Modifier = Modifier){

    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("Calendar", Icons.Default.DateRange),
        NavItem("Favorites", Icons.Default.Favorite),
        NavItem("Profile", Icons.Default.AccountCircle),
    )

    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold (modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index},
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = "Icon" )
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
        )
    { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex)
    }
}

/**
 * A composable function used to display the currently selected screen based on the selectedIndex.
 *
 * @param modifier The Modifier to be applied to the screen layout.
 * @param selectedIndex The index of the currently selected navItem.
 */
@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex : Int){
    when(selectedIndex){
        0 -> HomeScreen()
        1 -> CalendarScreen()
        2 -> FavoritesScreen()
        3 -> SettingsScreen()
    }
}
