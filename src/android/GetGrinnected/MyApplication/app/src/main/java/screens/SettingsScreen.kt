package screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import com.example.myapplication.FontSizePrefs
import com.example.myapplication.toAccountEntity
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.res.painterResource
import androidx.core.net.toUri
import com.example.myapplication.R

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
                   navController: NavController,
                   fontSizeSetting: String,
                   onFontSizeChange: (String) -> Unit) {
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
    // State associated with whether there is an error in the username
    var usernameError by remember { mutableStateOf<String?>(null) }

    var fontSizeDropdownExpanded by remember { mutableStateOf(false) }
    val fontSizeOptions = FontSizePrefs.entries
    val selectedFontPref = FontSizePrefs.getFontPrefFromKey(fontSizeSetting)

    // Boolean associated with whether the delete account dialog needs to display or not
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Sets up our ui to follow a box layout
    Box(modifier = modifier.fillMaxSize()) {

        // We make a row to set our preferences text in line with our switch account icon
        Column(
            modifier = modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            Text(
                text = "Preferences",
                style = typography.headlineMedium,
                color = colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider()
        }

        // Sets up a column for the rest of the information
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 70.dp, start = 8.dp, end = 8.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            SettingsSection(title = "Profile") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Username:", style = typography.bodyLarge)

                    Spacer(modifier = Modifier.weight(1f))

                    Text(text = account.account_name, style = typography.bodyLarge)
                    // Button to edit the username
                    IconButton(onClick = {
                        newUsername = account.account_name
                        showEditDialog = true
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Username", tint = colorScheme.tertiary)
                    }
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
                            Text("Save", color = colorScheme.tertiary, style = typography.labelLarge)
                        }
                    },
                    dismissButton = {
                        // This is our button to cancel the editing to the username
                        TextButton(onClick = {
                            showEditDialog = false
                        }) {
                            Text(
                                "Cancel",
                                color = colorScheme.tertiary,
                                style = typography.labelLarge
                            )
                        }
                    }
                )
            }

            Spacer(modifier = modifier.height(8.dp))

            // Setup a row to have the wording for the dark mode switch inline with the switch
            SettingsSection(title = "Appearance") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (darkTheme) "Switch to light mode" else "Switch to dark mode",
                        style = typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    // Switch to toggle dark or light mode
                    Switch(
                        checked = darkTheme,
                        onCheckedChange = { onToggleTheme(it) }
                    )
                }
            }

            Spacer(modifier = modifier.height(8.dp))

            SettingsSection(title = "Accessibility") {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Font Size:", style = typography.bodyLarge)
                    Spacer(modifier = Modifier.weight(1f))
                    Box {
                        Text(
                            text = selectedFontPref.label,
                            modifier = Modifier.clickable { fontSizeDropdownExpanded = true },
                            color = colorScheme.tertiary
                        )
                        DropdownMenu(
                            expanded = fontSizeDropdownExpanded,
                            onDismissRequest = { fontSizeDropdownExpanded = false }
                        ) {
                            fontSizeOptions.forEach { size ->
                                DropdownMenuItem(
                                    text = { Text(size.label) },
                                    onClick = {
                                        onFontSizeChange(size.key)
                                        fontSizeDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = modifier.height(8.dp))

            SettingsSection(title = "Notifications") {

                Spacer(modifier = modifier.height(4.dp))

                Row(
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Time to alert before event",
                        style = typography.bodyLarge,
                        color = colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = modifier.height(8.dp))

            SettingsSection(title = "About") {
                val githubUrl = "https://github.com/seehorne/GetGrinnected"
                val discordUrl = "https://discord.com/invite/e4PrM4RyEr"

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Acknowledgements",
                        style = typography.titleMedium,
                        color = colorScheme.onBackground
                    )
                    Text(
                        text = "Some text about stakeholders or mission",
                        style = typography.bodyMedium,
                        color = colorScheme.onBackground
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        IconButton(onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, githubUrl.toUri())
                            context.startActivity(intent)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.github_mark), // Add this to your drawable
                                contentDescription = "GitHub",
                                tint = colorScheme.tertiary,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        IconButton(onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, discordUrl.toUri())
                            context.startActivity(intent)
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.discord_icon_svgrepo_com), // Add this too
                                contentDescription = "Discord",
                                tint = colorScheme.tertiary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = modifier.height(4.dp))

            TextButton(onClick = {showDeleteDialog = true},
                modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()) {
                Text(text = "Delete Account",
                    style = typography.bodyLarge,
                    color = colorScheme.tertiary)
            }

            if (showDeleteDialog) {
                AlertDialog(
                    // When we dismiss with our dismiss button we set our show boolean to false
                    onDismissRequest = { showDeleteDialog = false },
                    title = {
                        Text(
                            "Are you sure want to delete your account?",
                            color = colorScheme.onBackground,
                            style = typography.titleLarge
                        )
                    },
                    confirmButton = {
                        // Button to save the edited username
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                   //TODO Delete Account
                                }
                                showDeleteDialog = false
                            },
                        ) {
                            Text(
                                "Save",
                                color = colorScheme.tertiary,
                                style = typography.labelLarge)

                        }
                    },
                    dismissButton = {
                        // This is our button to cancel the deleting user process
                        TextButton(onClick = {
                            showDeleteDialog = false
                        }) {
                            Text(
                                "Cancel",
                                color = colorScheme.tertiary,
                                style = typography.labelLarge
                            )
                        }
                    }
                )
            }

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
                    .fillMaxWidth()
            ) {
                Row {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Edit Username",
                        modifier = modifier.size(20.dp),
                        tint = colorScheme.onPrimary
                    )
                    Spacer(modifier.width(4.dp))
                    Text(
                        "Sign Out",
                        style = typography.labelLarge
                    )
                }
            }
        }
    }
}

/**
 * Creates a composable of a generic settings card that expands with
 * the animation and has the expanded or expanded less icon in the right
 * so we can avoid the repetitive set up.
 * @param title the title of the section for the card
 * @param modifier to be applied to the composable layout
 * @param content this is the composable body we want when the card is
 * expanded
 */

@Composable
fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = typography.titleLarge,
                    color = colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Toggle Section",
                    tint = colorScheme.onSurface
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    content = content
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
    SettingsScreen(account = User(1, "User123", "test@test.com", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), listOf(),"a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1),  darkTheme = false, onToggleTheme =  {}, navController = rememberNavController(), fontSizeSetting = "M", onFontSizeChange = {})
}
