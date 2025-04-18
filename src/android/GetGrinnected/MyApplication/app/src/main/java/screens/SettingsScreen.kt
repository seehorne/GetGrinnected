package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.DataStoreSettings
import com.example.myapplication.OrgCard
import com.example.myapplication.R
import com.example.myapplication.User
import kotlinx.coroutines.launch

/**
 * A composable function that represents the Settings screen of our app.
 *
 * @param modifier Modifier to be applied to the screen layout.
 * @param orgs the list of orgs that they follow.
 * @param account the user account that is currently logged in
 * @param darkTheme the current state of the Theme of light or dark mode
 * @param onToggleTheme a lambda function passed down from previous screen that calls back to the
 * function instated in the call the AppNavigation in the MainActivity.
 * @param navController is the navigation system passed down from our app navigation to be
 * used for sign out functionality
 */
@Composable
fun SettingsScreen(modifier: Modifier = Modifier,
                   orgs: List<User>,
                   account: User,
                   darkTheme: Boolean,
                   onToggleTheme: (Boolean) -> Unit,
                   navController: NavController) {
    // Allows the app to be a scrollable view
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { scrollState.animateScrollTo(0) }


    // Sets the orgs to be only the set of orgs that are followed by the user.
    val isFollowed = orgs.filter { it.is_followed }
    val context = LocalContext.current // The current context of our app
    val coroutineScope = rememberCoroutineScope() // Used to launch background tasks and processes

    // Sets up our ui to follow a box layout
    Box(modifier = modifier.fillMaxSize()) {
        // We make a row to set our profile text in line with our switch account icon
        Row(
            modifier = modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Profile", fontSize = 28.sp)

            Spacer(modifier = modifier.weight(1f))

            // This is the icon button for switch an account
            IconButton(
                onClick = { /* TODO handle switch account */ },
                modifier = modifier.padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Switch Account",
                )
            }
        }

        // Sets up a column for the rest of the information
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 80.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Sets up the layout to be a box
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = modifier.padding(16.dp)
            ) {
                // This is our profile image
                Image(
                    painter = painterResource(id = R.drawable.blank_profile_picture),
                    contentDescription = "Profile Image",
                    modifier = modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
                // Button to change the profile picture
                IconButton(
                    onClick = { /* TODO handle image change */ },
                    modifier = modifier
                        .offset(x = (-8).dp, y = (-8).dp)
                        .background(Color.White, CircleShape)
                        .size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile Image",
                        tint = Color.Black,
                        modifier = modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = modifier.height(8.dp))

            // Setup row to have the username and editing button inline with each other
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = account.account_name, fontSize = 16.sp)
                // Button to edit the username
                IconButton(onClick = { /* TODO handle username edit */ }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Username",
                        modifier = modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = modifier.height(8.dp))

            Text("Settings", fontSize = 20.sp)

            Spacer(modifier = modifier.height(8.dp))

            // Setup a row to have the wording for the dark mode switch inline with the switch
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Switch between light and dark mode", fontSize = 16.sp)

                Spacer(modifier = Modifier.width(12.dp))

                // Switch to toggle dark or light mode
                Switch(
                    checked = darkTheme,
                    onCheckedChange = {
                        onToggleTheme(it)
                    })
            }

            Spacer(modifier = modifier.height(8.dp))

            // This button handles our sign out process
            Button (
                onClick = {  // Sets our logged in state to false
                    coroutineScope.launch{
                        DataStoreSettings.setLoggedIn(context, false)
                    }
                    navController.navigate("welcome"){ // takes us to the welcome screen
                        popUpTo(0){inclusive = true} // pops the back stack
                        launchSingleTop = true
                    }
                          },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Sign Out")
            }

            Text("Organizations you follow: ", fontSize = 20.sp)

            Spacer(modifier = modifier.height(4.dp))

            // Checks if the user has any followed orgs if not it displays the following
            if (isFollowed.isEmpty()) {
                Text("You haven't followed any organizations yet.", modifier = Modifier.padding(16.dp))
            } else { // If the user does it makes a scrollable list of org cards that they follow
                isFollowed.forEach { account ->
                    OrgCard(
                        account = account, modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

/**
 * Preview used specifically for UI design purposes
 */
@Preview (showBackground = true)
@Composable
fun SettingsScreenPreview(){
    val sampleOrgs = listOf(
        User(1, "test", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1),
        User(1, "test2", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1, true),
        User(1, "test3", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1, true),
        User(1, "test4", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1, true),
        User(1, "test5", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1, true),
        )
    SettingsScreen(orgs = sampleOrgs, account = User(1, "User123", "test@test.com", "password", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), "a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1),  darkTheme = false, onToggleTheme =  {}, navController = rememberNavController())
}
