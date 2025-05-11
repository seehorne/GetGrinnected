package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.GetGrinnected.myapplication.R
import com.GetGrinnected.myapplication.RetrofitApiClient
import com.GetGrinnected.myapplication.SignupRequest
import kotlinx.coroutines.launch
import com.GetGrinnected.myapplication.DataStoreSettings

/**
 * A composable function that represents the Signup screen of our application.
 *
 * This screen includes input fields for username, email (That are all Strings),
 * along with a sign-up button (To navigate to the homepage/complete account creation)
 * and a sign in navigation option for users who already have an account.
 *
 * @param modifier modifier applied to the screen layout
 * @param navController used to move through the app
 */

@Composable
fun SignupScreen(modifier: Modifier, navController: NavController) {
    // Username input by the user
    var username by remember { mutableStateOf("") }
    // Email input by the user
    var email by remember { mutableStateOf("") }
    // Manages the focus of the keyboard so we can move it
    val focusManager = LocalFocusManager.current
    // Boolean associated with specifically a username error to shift field color
    var errUsername by remember { mutableStateOf(false) }
    // Boolean associated with specifically an email error to shift field color
    var errEmail by remember { mutableStateOf(false) }
    // General message of errors associated with any given issue
    var errMsg by remember { mutableStateOf("") }
    // Boolean to track whether our api is messaging and we need to halt input
    var isLoading by remember { mutableStateOf(false) }
    // Process to launch background tasks
    val coroutineScope = rememberCoroutineScope()
    // Flag sent to the verification function to indicate a signUp Process
    val signUp = true
    // To access our theme colors
    val colorScheme = MaterialTheme.colorScheme
    // To access our font info from our theme
    val typography = MaterialTheme.typography
    // The current context of our app
    val context = LocalContext.current

    // This sets up the general look of the entire screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // This is the app logo image
            Image(
                painter = painterResource(id = R.drawable.getgrinnected_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .size(250.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome to GetGrinnected",
                style = typography.headlineMedium,
                color = colorScheme.onBackground,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Create a free account",
                style = typography.bodyLarge,
                color = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            // This handles the username field
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    errUsername = false
                },
                label = { Text("Username", style = typography.labelLarge) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Username field input icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                isError = errUsername,
                keyboardOptions = KeyboardOptions(
                    // This makes it so the enter key is a next button instead of enter
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        // This moves our focus down to the next text field.
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // This handles the email field
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it.trim().lowercase()
                    errEmail = false
                }, //ensures it isn't case sensitive
                label = { Text("Email", style = typography.labelLarge) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Email input field icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                isError = errEmail,
                keyboardOptions = KeyboardOptions(
                    // This makes it so the enter key is a done button instead of enter
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        // Clear focus makes it so we are no longer focused on the field and can close the
                        // keyboard on the phone
                        focusManager.clearFocus()
                    }
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Handles reporting issues and errors that arise from user input
            if (errMsg.isNotEmpty()) {
                Text(
                    text = errMsg,
                    color = colorScheme.error,
                    style = typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // This is how we setup our signup button
            Button(
                onClick = {
                    coroutineScope.launch {

                        // Checks the validation conditions
                        val emailError = validateEmail(email)
                        val usernameError = validateUsername(username)

                        if (usernameError != null) {
                            errMsg = usernameError
                            errUsername = true
                            return@launch // Escapes launch due to invalid username
                        }

                        if (emailError != null) {
                            errMsg = emailError
                            errEmail = true
                            return@launch // Escapes launch due to non Grinnell email
                        }

                        isLoading = true // Set loading state to true to disable the button
                        try {
                            // Makes the api email request check
                            val response = RetrofitApiClient.apiModel.signup(
                                SignupRequest(username = username, email = email)
                            )
                            // Assess if the request and if the email was available
                            if (response.isSuccessful) {
                                // Sets our pending verification states so that we return to the proper
                                // page if we leave the app to check an the email
                                DataStoreSettings.setPendingVerification(context, email, signUp)
                                navController.navigate("verification/${email}/${signUp}") {
                                    popUpTo(0) { inclusive = true }
                                    launchSingleTop = true
                            }
                            } else {
                                errMsg = if(response.errorBody()?.string()?.contains("Username exists") == true){
                                    "User with that username already exists"
                                } else if (response.errorBody()?.string()?.contains("Email exists") == true){
                                    "User with that email already exists"
                                } else{
                                    "Signup Failed"
                                }
                            }
                            } catch(e: Exception) { // Handles network errors that way arise when making the api call
                               errMsg = "Network error: ${e.localizedMessage}"
                        } finally { // Set loading state to false to reenable the button
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Signing up" else "Sign up", style = typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // This creates a row that implies our sign in text button to take you to the login page
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already on GetGrinnected?",
                    style = typography.bodyMedium,
                    color = colorScheme.onBackground
                )
                TextButton(onClick = { navController.navigate("login") }) {
                    Text(text = "Sign in",
                        style = typography.labelLarge,
                        modifier = modifier.semantics { contentDescription = "Log in to your account" })
                }
            }

            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}
/**
 * Validates an email ends in @grinnell.edu
 * @param email takes in a String representation of an email
 * @return null which basically means it ends in @grinnell.edu otherwise
 * it returns a string associated with the error it isn't meeting.
 */
fun validateEmail(email: String): String? {
    if (!email.endsWith("@grinnell.edu")) return "Email must end with @grinnell.edu"
    return null
}

@Preview(showBackground = true)
@Composable
fun SignupScreenPreview(){
    SignupScreen(modifier = Modifier, navController = rememberNavController())
}

/**
 * Validates username meets our general requirements (see docs)
 * @param username is the username to be validated
 * @return either null if we pass validation or a string reporting the associated error
 */
fun validateUsername(username: String): String? {
    // Set of allowed characters Alphabetical, Numeric and periods and underscores
    val allowedChars = Regex("^[a-zA-Z0-9._]+$")
    // If the username contains characters that aren't allowed we display the following error
    if (!allowedChars.matches(username)) {
        return "Username can only include letters, numbers, '.', and '_'"
    }
    // Set of letters
    val letters = Regex("[a-zA-Z]")
    // If the username does not contain at least one letter display the following error
    if (!letters.containsMatchIn(username)) {
        return "Username must contain at least one letter"
    }
    // If username has consecutive periods return the following error
    if (username.contains("..")) return "Username cannot contain two or more '.' in a row"
    // If username has consecutive underscores return the following error
    if (username.contains("__")) return "Username cannot contain two or more '_' in a row"
    // If username has a period followed by an underscore return the following error
    if (username.contains("._")) return "Username cannot contain a '.' followed by a '_'"
    // If username has an underscore followed by a period return the following error
    if (username.contains("_.") && !username.contains("._")) return "Username cannot contain a '_' followed by a '.'"
    // If username starts with a period or underscore return the following error
    if (username.startsWith(".") || username.startsWith("_")) {
        return "Username cannot start with '.' or '_'"
    }
    // If username ends with a period or underscore return the following error
    if (username.endsWith(".") || username.endsWith("_")) {
        return "Username cannot end with '.' or '_'"
    }
    // If username is more than 20 characters long return the following error
    if (username.length > 20) return "Username must be no more that 20 characters long"

    return null // means we passed validation
}
