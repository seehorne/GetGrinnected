package screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.EmailRequest
import com.example.myapplication.R
import com.example.myapplication.RetrofitApiClient
import kotlinx.coroutines.launch

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
    var isLoading by remember { mutableStateOf(false)}
    // Process to launch background tasks
    val coroutineScope = rememberCoroutineScope()
    // Flag sent to the verification function to indicate a signUp Process
    val signUp = true
    // To access our theme colors
    val colorScheme = MaterialTheme.colorScheme
    // To access our font info from our theme
    val typography = MaterialTheme.typography



    // This sets up the general look of the entire screen
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
            painter = painterResource(id = R.drawable.gg_logo_2),
            contentDescription = "App Logo",
            modifier = Modifier
                .fillMaxWidth()
                .size(250.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Welcome to GetGrinnected",
            style = typography.headlineMedium,
            color = colorScheme.onBackground)

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Create a free account",
            style = typography.bodyLarge,
            color = colorScheme.onBackground)

        Spacer(modifier = Modifier.height(8.dp))

        // This handles the username field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it
                            errUsername = false},
            label = { Text("Username", style = typography.labelLarge) },
            isError = errUsername,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext ={
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // This handles the email field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it.trim().lowercase()
                            errEmail = false}, //ensures it isn't case sensitive
            label = { Text("Email", style = typography.labelLarge) },
            isError = errEmail,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone ={
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
        Button(onClick = {
            coroutineScope.launch {

                // Checks the validation conditions
                val emailError = validateEmail(email)
                val missingUsername = username.isBlank()

                if (missingUsername){
                    errMsg = "Please enter username"
                    errUsername = true
                    return@launch // Escapes launch due to missing username
                }

                if (emailError != null){
                    errMsg = emailError
                    errEmail = true
                    return@launch // Escapes launch due to non Grinnell email
                }

                isLoading = true // Set loading state to true to disable the button
                try{
                    // Makes the api email request check
                    val emailReponse = RetrofitApiClient.apiModel.checkemail(
                        EmailRequest(email)
                    )
                    // Assess if the request and if the email was available
                    if (emailReponse.isSuccessful && emailReponse.body()?.success == true) {
                        // TODO SEND EMAIL HERE
                        navController.navigate("verification/${email}/${signUp}/${username}") {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        errMsg = emailReponse.body()?.message ?: "Email already in use"
                    }
                } catch(e: Exception) { // Handles network errors that way arise when making the api call
                    errMsg = "Network error: ${e.localizedMessage}"
                } finally{ // Set loading state to false to reenable the button
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
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically){
            Text(text = "Already on GetGrinnected?", style = typography.bodyMedium, color = colorScheme.onBackground)
            TextButton(onClick = {navController.navigate("login")}){
                Text(text = "Sign in", style = typography.labelLarge)
            }
        }

        Spacer(modifier = Modifier.height(200.dp))
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