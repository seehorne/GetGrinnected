package screens

import android.content.Intent
import android.widget.Toast
import com.GetGrinnected.myapplication.R
import androidx.compose.animation.AnimatedVisibility
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
import com.GetGrinnected.myapplication.AppRepository
import com.GetGrinnected.myapplication.DataStoreSettings
import com.GetGrinnected.myapplication.User
import kotlinx.coroutines.launch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import com.GetGrinnected.myapplication.FontSizePrefs
import com.GetGrinnected.myapplication.toAccountEntity
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.res.painterResource
import androidx.core.net.toUri
import androidx.compose.material3.SwitchDefaults
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.GetGrinnected.myapplication.AppRepository.deleteAccount
import com.GetGrinnected.myapplication.SnackBarController
import com.GetGrinnected.myapplication.SnackBarEvent
import kotlinx.coroutines.flow.collectLatest

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
    var showEditUsernameDialog by remember { mutableStateOf(false) }
    // String associated with storing and changing the username of an account when we edit
    var newUsername by remember { mutableStateOf(account.account_name) }
    // State associated with whether there is an error in the username
    var usernameError by remember { mutableStateOf<String?>(null) }

    // State associated with whether the fontsize drop down is expanded
    var fontSizeDropdownExpanded by remember { mutableStateOf(false) }
    // The possible fontsize values
    val fontSizeOptions = FontSizePrefs.entries
    // Keys associated with the enum of the selected fontsizes
    val selectedFontPref = FontSizePrefs.getFontPrefFromKey(fontSizeSetting)

    // State associated with notification time
    var notificationDropdownExpanded by remember { mutableStateOf(false) }
    // tracks chosen notification time
    var selectedTime by remember { mutableIntStateOf(15) }

    // Boolean associated with whether the delete account dialog needs to display or not
    var showDeleteDialog by remember { mutableStateOf(false) }
    // Boolean associated with whether the sign out dialog needs to display or not
    var showSignoutDialog by remember { mutableStateOf(false) }

    // Boolean associated with whether the editing for the username needs to display or not
    var showEditEmailDialog by remember { mutableStateOf(false) }
    // String associated with storing and changing the username of an account when we edit
    var newEmail by remember { mutableStateOf(account.email) }
    // State associated with whether there is an error in the email
    var emailError by remember { mutableStateOf<String?>(null) }

    // Host for our snack bar
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        SnackBarController.events.collectLatest { event ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
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
                        color = colorScheme.onBackground,
                        modifier = modifier.semantics { heading() }
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
                    // Profile Section
                    SettingsSection(title = "Profile") {
                        // Username Section of Profile Section
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
                                showEditUsernameDialog = true
                            }) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit Username",
                                    tint = colorScheme.tertiary
                                )
                            }
                        }
                        // Email Section of Profile Section
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Email:", style = typography.bodyLarge)

                            Spacer(modifier = Modifier.weight(1f))

                            Text(text = account.email, style = typography.bodyLarge)
                            // Button to edit the username
                            IconButton(onClick = {
                                newEmail = account.email
                                showEditEmailDialog = true
                            }) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit Email",
                                    tint = colorScheme.tertiary
                                )
                            }
                        }
                    }

                    // We check to see if showEdit dialog is set to true and if it is we display the editing username
                    if (showEditUsernameDialog) {
                        AlertDialog(
                            // When we dismiss with our dismiss button we set our show boolean to false
                            onDismissRequest = { showEditUsernameDialog = false },
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
                                            val updatedAccount =
                                                account.copy(account_name = newUsername)
                                            // Upserts ie updates the account name
                                            AppRepository.upsertAccount(updatedAccount.toAccountEntity())
                                            // Sets our current active account to the given account
                                            AppRepository.setCurrentAccountById(updatedAccount.accountid)
                                            // Sends our updated username to the remote database
                                            AppRepository.syncUsername(newUsername)
                                            // Sends snack bar
                                            SnackBarController.sendEvent(SnackBarEvent("Username was updated"))
                                            // Closes the editing dialog
                                            showEditUsernameDialog = false
                                        }
                                    },
                                    // If username is valid the button will be enabled otherwise it will be disabled
                                    enabled = usernameError == null
                                ) {
                                    Text(
                                        "Save",
                                        color = colorScheme.tertiary,
                                        style = typography.labelLarge
                                    )
                                }
                            },
                            dismissButton = {
                                // This is our button to cancel the editing to the username
                                TextButton(onClick = {
                                    showEditUsernameDialog = false
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

                    // We check to see if showEdit dialog is set to true and if it is we display the editing email
                    if (showEditEmailDialog) {
                        AlertDialog(
                            // When we dismiss with our dismiss button we set our show boolean to false
                            onDismissRequest = { showEditEmailDialog = false },
                            title = {
                                Text(
                                    "Edit Email",
                                    color = colorScheme.onBackground,
                                    style = typography.titleLarge
                                )
                            },
                            text = {
                                Column {
                                    // Makes the text field to enter the input
                                    OutlinedTextField(
                                        value = newEmail,
                                        onValueChange = {
                                            newEmail = it
                                            // Check that the Email is valid
                                            emailError = validateEmail(it)
                                        },
                                        singleLine = true,
                                        isError = emailError != null,
                                        label = {
                                            Text(
                                                "Email",
                                                color = colorScheme.onBackground,
                                                style = typography.labelLarge
                                            )
                                        }
                                    )
                                    // If we have a validation issue with Email we display the issue with
                                    // the associated error
                                    if (emailError != null) {
                                        Text(
                                            text = emailError ?: "",
                                            color = colorScheme.error,
                                            style = typography.bodySmall
                                        )
                                    }
                                }
                            },
                            confirmButton = {
                                // Button to save the edited email
                                TextButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            if (newEmail != account.email) {
                                                // Syncs the current account info with the remote db
                                                AppRepository.syncAccountData(context = context)
                                                // Sends our updated email to the remote database
                                                AppRepository.syncEmail(newEmail)
                                                SnackBarController.sendEvent(SnackBarEvent("Verification Code sent to Email"))
                                                // Closes the editing dialog
                                                showEditEmailDialog = false
                                                // Set pending verification state so that we go to the verification page with the correct info
                                                DataStoreSettings.setPendingVerification(context, newEmail, "settings")
                                                // Navigate to the Verification Page so the user can enter the one time code.
                                                navController.navigate("verification/${newEmail}/settings") {
                                                    popUpTo(0) { inclusive = false }
                                                    launchSingleTop = true
                                                }
                                            } else {
                                                emailError = "Email is the same as your current email"
                                            }
                                        }
                                    },
                                    // If email is valid the button will be enabled otherwise it will be disabled
                                    enabled = emailError == null
                                ) {
                                    Text(
                                        "Change",
                                        color = colorScheme.tertiary,
                                        style = typography.labelLarge
                                    )
                                }
                            },
                            dismissButton = {
                                // This is our button to cancel the editing to the email
                                TextButton(onClick = {
                                    showEditEmailDialog = false
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

                    // Appearance Section
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
                                onCheckedChange = { onToggleTheme(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                    checkedTrackColor = MaterialTheme.colorScheme.secondary,
                                ),
                                modifier = Modifier.semantics {
                                    stateDescription =
                                        if (darkTheme) "Dark mode is on" else "Dark mode is off"
                                }
                            )
                        }
                    }

                    Spacer(modifier = modifier.height(8.dp))

                    // Accessibility Section
                    SettingsSection(title = "Accessibility") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Font Size:", style = typography.bodyLarge)
                            Spacer(modifier = Modifier.weight(1f))
                            Box {
                                Text(
                                    text = selectedFontPref.label,
                                    modifier = Modifier.clickable {
                                        fontSizeDropdownExpanded = true
                                    },
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

                    // Notification Section
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
                            Spacer(modifier = modifier.width(100.dp))
                            Text(
                                text = selectedTime.toString(),
                                modifier = Modifier.clickable {
                                    notificationDropdownExpanded = true
                                },
                                color = colorScheme.tertiary
                            )
                            DropdownMenu(
                                expanded = notificationDropdownExpanded,
                                onDismissRequest = { notificationDropdownExpanded = false }
                            ) {
                                val times = listOf(0,15,30,45,60)
                                for (t in times.indices) {
                                    DropdownMenuItem(
                                        text = { Text(times[t].toString()) },
                                        onClick = {
                                            selectedTime = times[t]
                                            notificationDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = modifier.height(8.dp))

                    // About Section
                    SettingsSection(title = "About") {
                        // Github URL
                        val githubUrl = "https://github.com/seehorne/GetGrinnected"
                        // Discord URL
                        val discordUrl = "https://discord.com/invite/e4PrM4RyEr"

                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "GetGrinnected was developed by Grinnellians, for Grinnellians, with the goal of creating an intuitive and accessible platform to stay informed about campus events.",
                                style = typography.bodyMedium,
                                color = colorScheme.onBackground
                            )

                            Text(
                                text = "Acknowledgements",
                                style = typography.titleMedium,
                                color = colorScheme.onBackground
                            )

                            Text(
                                text = "We are deeply grateful to the amazing individuals who helped bring GetGrinnected to life:",
                                style = typography.bodyMedium,
                                color = colorScheme.onBackground
                            )

                            Text(
                                text = "Logo Design",
                                style = typography.titleMedium,
                                color = colorScheme.onBackground
                            )

                            Text(
                                text = "• Rei Yamada",
                                style = typography.bodyMedium,
                                color = colorScheme.onBackground
                            )

                            Text(
                                text = "Testing & Stakeholder Feedback",
                                style = typography.titleMedium,
                                color = colorScheme.onBackground
                            )

                            Text(
                                text = "• Yuina Iseki\n" +
                                        "• Lily Freeman\n" +
                                        "• Regan Stambaugh",
                                style = typography.bodyMedium,
                                color = colorScheme.onBackground
                            )

                            Text(
                                text = "Development Team",
                                style = typography.titleMedium,
                                color = colorScheme.onBackground
                            )

                            Text(
                                text = "• almond Heil \n" +
                                        "• Anthony Schwindt \n" +
                                        "• Budhil Thijm \n" +
                                        "• Ellie Seehorn \n" +
                                        "• Ethan Hughes \n" +
                                        "• Michael Paulin",
                                style = typography.bodyMedium,
                                color = colorScheme.onBackground
                            )

                            Text(
                                text = "Faculty",
                                style = typography.titleMedium,
                                color = colorScheme.onBackground
                            )

                            Text(
                                text = "• Prof. Leah",
                                style = typography.bodyMedium,
                                color = colorScheme.onBackground
                            )

                            Text(
                                text = "• Morris Pelzel",
                                style = typography.bodyMedium,
                                color = colorScheme.onBackground
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(24.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                IconButton(onClick = {
                                    // Allows us to leave the app to go to said URL
                                    val intent = Intent(Intent.ACTION_VIEW, githubUrl.toUri())
                                    context.startActivity(intent)
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.github_mark),
                                        contentDescription = "Press to Navigate to GitHub Repository",
                                        tint = colorScheme.tertiary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }

                                IconButton(onClick = {
                                    // Allows us to leave the app to go to said URL
                                    val intent = Intent(Intent.ACTION_VIEW, discordUrl.toUri())
                                    context.startActivity(intent)
                                }) {
                                    Icon(
                                        painter = painterResource(R.drawable.discord_icon_svgrepo_com),
                                        contentDescription = "Press to Navigate to Discord",
                                        tint = colorScheme.tertiary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = modifier.height(4.dp))

                    // Delete Account Button
                    TextButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Delete Account",
                            style = typography.bodyLarge,
                            color = colorScheme.tertiary,
                            modifier = modifier.semantics {
                                contentDescription = "Delete your account"
                                role = Role.Button
                            })
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
                                // Button to confirm account deletion
                                TextButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            val success = deleteAccount(context)
                                            if (success) {
                                                // clear user session
                                                DataStoreSettings.clearUserSession(context)
                                                onToggleTheme(false)
                                                navController.navigate("welcome") {
                                                    popUpTo(0) { inclusive = true }
                                                    launchSingleTop = true
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Couldn't Delete Account at this time try again later",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                        showDeleteDialog = false
                                    },
                                ) {
                                    Text(
                                        "Delete",
                                        color = colorScheme.tertiary,
                                        style = typography.labelLarge
                                    )

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
                            showSignoutDialog = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Logout",
                                modifier = modifier.size(20.dp),
                                tint = colorScheme.onPrimary
                            )
                            Spacer(modifier.width(4.dp))
                            Text(
                                "Log Out",
                                style = typography.labelLarge
                            )
                        }
                    }

                    if (showSignoutDialog) {
                        AlertDialog(
                            // When we dismiss with our dismiss button we set our show boolean to false
                            onDismissRequest = { showSignoutDialog = false },
                            title = {
                                Text(
                                    "Are you sure want to logout?",
                                    color = colorScheme.onBackground,
                                    style = typography.titleLarge
                                )
                            },
                            confirmButton = {
                                // Button to confirm logout
                                TextButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            // syncs current account info with the repo
                                            AppRepository.syncAccountData(context = context)
                                            // This resets user preferences to default states
                                            DataStoreSettings.clearUserSession(context)
                                            onToggleTheme(false)
                                        }
                                        navController.navigate("welcome") { // takes us to the welcome screen
                                            popUpTo(0) { inclusive = true } // pops the back stack
                                            launchSingleTop = true
                                        }
                                        showSignoutDialog = false
                                    },
                                ) {
                                    Text(
                                        "Log out",
                                        color = colorScheme.tertiary,
                                        style = typography.labelLarge
                                    )

                                }
                            },
                            dismissButton = {
                                // This is our button to cancel the signout process
                                TextButton(onClick = {
                                    showSignoutDialog = false
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
    // State associated with whether the care is expanded or not
    var expanded by remember { mutableStateOf(false) }
    // Accessing colors from our theme
    val colorScheme = MaterialTheme.colorScheme
    // Accessing font info from our theme
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
                .semantics {
                    role = Role.Button
                    contentDescription = "$title section, ${if (expanded) "expanded" else "collapsed"}. Tap to ${if (expanded) "collapse" else "expand"}."
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Title of the card text
                Text(
                    text = title,
                    style = typography.titleLarge,
                    color = colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                        .semantics { heading() }
                )
                // Icon associated with expanded or not
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Toggle Section",
                    tint = colorScheme.onSurface
                )
            }
            // Handles the visibility and structure of content in the expanded state
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
/**
 * Preview used specifically for UI design purposes
 */
@Preview (showBackground = true)
@Composable
fun SettingsScreenPreview(){
    SettingsScreen(account = User(1, "User123", "test@test.com", "profile picture", listOf(1, 2), listOf(1, 2), listOf("music", "fun"), listOf(),"a relatively long description to give me a good idea of what the look of the about section will entail if an org has more info to discuss about themselves", 1),  darkTheme = false, onToggleTheme =  {}, navController = rememberNavController(), fontSizeSetting = "M", onFontSizeChange = {})
}
