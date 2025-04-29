package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.AppRepository
import com.example.myapplication.DataStoreSettings
import com.example.myapplication.User
import kotlinx.coroutines.launch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import com.example.myapplication.toAccountEntity

/**
 * A composable function that represents the Settings screen of our app.
 *
 * @param modifier Modifier to be applied to the screen layout.
 * @param account the user account that is currently logged in
 * @param darkTheme the current state of the Theme of light or dark mode
 * @param onToggleTheme a lambda function passed down from previous screen that calls back to the
 * function instated in the call the AppNavigation in the MainActivity.
 * @param navController is the navigation system passed down from our app navigation to be
 * used for sign out functionality
 */
@Composable
fun SettingsScreen(modifier: Modifier = Modifier,
                   account: User,
                   darkTheme: Boolean,
                   onToggleTheme: (Boolean) -> Unit,
                   navController: NavController) {
    // Allows the app to be a scrollable view
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { scrollState.animateScrollTo(0) }

    // Accessing colors from our theme
    val colorScheme = MaterialTheme.colorScheme
    // Accessing font info from our theme
    val typography = MaterialTheme.typography
    // The current context of our app
    val context = LocalContext.current
    // Used to launch background tasks and processes
    val coroutineScope = rememberCoroutineScope()

    // Boolean associated with whether the editing for the username needs to display or not
    var showEditDialog by remember { mutableStateOf(false) }
    // String associated with storing and changing the username of an account when we edit
    var newUsername by remember { mutableStateOf(account.account_name) }

    var usernameError by remember { mutableStateOf<String?>(null) }

    // Sets up our ui to follow a box layout
    Box(modifier = modifier.fillMaxSize()) {

        // We make a row to set our preferences text in line with our switch account icon
        Row(
            modifier = modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Preferences",
                style = typography.headlineMedium,
                color = colorScheme.onBackground
            )

            Spacer(modifier = modifier.weight(1f))
        }
        // Sets up a column for the rest of the information
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 80.dp, start = 16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = modifier.height(8.dp))

            Text(
                text = "Profile",
                style = typography.titleLarge,
                color = colorScheme.onBackground
            )

            Spacer(modifier = modifier.height(4.dp))

            // Setup row to have the username and editing button inline with each other
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Username:",
                    style = typography.bodyLarge,
                    color = colorScheme.onBackground
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = account.account_name,
                    style = typography.bodyLarge,
                    color = colorScheme.onBackground
                )

                // Button to edit the username
                IconButton(onClick = {
                    newUsername = account.account_name
                    showEditDialog = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Username",
                        modifier = modifier.size(20.dp),
                        tint = colorScheme.primary
                    )
                }
            }

            // We check to see if showEdit dialog is set to true and if it is we display the editing username
            if (showEditDialog) {
                AlertDialog(
                    // When we dismiss with our dismiss button we set our show boolean to false
                    onDismissRequest = { showEditDialog = false },
                    title = {
                        Text(
                            "Edit Username",
                            color = colorScheme.onBackground,
                            style = typography.titleLarge
                        )
                    },
                    text = {
                        Column {
                            // Makes the text field to enter the input
                            OutlinedTextField(
                                value = newUsername,
                                onValueChange = {
                                    newUsername = it
                                    // Check that the username is valid
                                    usernameError = validateUsername(it)
                                },
                                singleLine = true,
                                isError = usernameError != null,
                                label = {
                                    Text(
                                        "Username",
                                        color = colorScheme.onBackground,
                                        style = typography.labelLarge
                                    )
                                }
                            )
                            // If we have a validation issue with username we display the issue with
                            // the associated error
                            if (usernameError != null) {
                                Text(
                                    text = usernameError ?: "",
                                    color = colorScheme.error,
                                    style = typography.bodySmall
                                )
                            }
                        }
                    },
                    confirmButton = {
                        // Button to save the edited username
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    // Makes a new account entity with the new name
                                    val updatedAccount = account.copy(account_name = newUsername)
                                    // Upserts ie updates the account name
                                    AppRepository.upsertAccount(updatedAccount.toAccountEntity())
                                    // Sets our current active account to the given account
                                    AppRepository.setCurrentAccountById(updatedAccount.accountid)
                                    // Sends our updated username to the remote database
                                    AppRepository.syncUsername(newUsername)
                                    // Closes the editing dialog
                                    showEditDialog = false
                                }
                            },
                            // If username is valid the button will be enabled otherwise it will be disabled
                            enabled = usernameError == null
                        ) {
                            Text("Save", color = colorScheme.primary, style = typography.labelLarge)
                        }
                    },
                    dismissButton = {
                        // This is our button to cancel the editing to the username
                        TextButton(onClick = {
                            showEditDialog = false
                        }) {
                            Text(
                                "Cancel",
                                color = colorScheme.primary,
                                style = typography.labelLarge
                            )
                        }
                    }
                )
            }

            Spacer(modifier = modifier.height(8.dp))

            // Setup a row to have the wording for the dark mode switch inline with the switch
            Text(
                text = "Appearance",
                style = typography.titleLarge,
                color = colorScheme.onBackground
            )

            Spacer(modifier = modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (darkTheme) "Switch to light mode" else "Switch to dark mode",
                    style = typography.bodyLarge,
                    color = colorScheme.onBackground,
                )

                Spacer(modifier = Modifier.weight(1f))

                // Switch to toggle dark or light mode
                Switch(
                    checked = darkTheme,
                    modifier = Modifier.padding(end = 8.dp),
                    onCheckedChange = {
                        onToggleTheme(it)
                    })
            }

            Spacer(modifier = modifier.height(8.dp))

            Text(
                text = "Accessibility",
                style = typography.titleLarge,
                color = colorScheme.onBackground
            )

            Spacer(modifier = modifier.height(4.dp))

            Row(modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Font Size",
                    style = typography.bodyLarge,
                    color = colorScheme.onBackground
                )
            }

            Spacer(modifier = modifier.height(4.dp))

            Row(modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Screen Reader",
                    style = typography.bodyLarge,
                    color = colorScheme.onBackground
                )
            }

            Spacer(modifier = modifier.height(8.dp))

            Text(
                text = "About",
                style = typography.titleLarge,
                color = colorScheme.onBackground
            )

            Spacer(modifier = modifier.height(4.dp))

            // This button handles our sign out process
            Button(
                onClick = {  // Sets our logged in state to false
                    coroutineScope.launch {
                        // This resets user preferences to default states
                        DataStoreSettings.clearUserSession(context)
                        onToggleTheme(false)
                    }
                    navController.navigate("welcome") { // takes us to the welcome screen
                        popUpTo(0) { inclusive = true } // pops the back stack
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "Sign Out",
                    style = typography.labelLarge
                )
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
    SettingsScreen(account = User(1, "User123", "test@test.com", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), listOf(),"a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1),  darkTheme = false, onToggleTheme =  {}, navController = rememberNavController())
}
