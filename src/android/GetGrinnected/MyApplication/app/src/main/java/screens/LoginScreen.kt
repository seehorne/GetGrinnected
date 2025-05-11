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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.GetGrinnected.myapplication.LoginRequest
import com.GetGrinnected.myapplication.R
import com.GetGrinnected.myapplication.RetrofitApiClient
import kotlinx.coroutines.launch
import com.GetGrinnected.myapplication.DataStoreSettings

/**
 * A composable function that represents the Login screen of our application.
 *
 * This screen includes input fields for username and password (That are both Strings),
 * along with a Login button (To navigate to the homepage/complete account creation)
 * and a sign up navigation option for users who need to create an account.
 *
 *  @param modifier modifier applied to the screen layout
 *  @param navController used to move through the app
 */

@Composable
fun LoginScreen(modifier: Modifier, navController: NavController) {
    // Email input by user
    var email by remember { mutableStateOf("") }
    // Error Message to be displayed
    var errMsg by remember { mutableStateOf("") }
    // Process to launch background tasks
    val coroutineScope = rememberCoroutineScope()
    // Boolean to track whether our api is messaging and we need to halt input
    var isLoading by remember { mutableStateOf(false) }
    // Boolean associated with specifically a email error to shift field color
    var errEmail by remember { mutableStateOf(false) }
    // Flag sent to the verification function to indicate a login process (false means login true means signup)
    val signUp = false
    // To access our theme colors
    val colorScheme = MaterialTheme.colorScheme
    // To access our font info from our theme
    val typography = MaterialTheme.typography
    // The current context of our app
    val context = LocalContext.current


    // Manages Keyboard focus and allows us to pull to specific locations
    val focusManager = LocalFocusManager.current

    // This sets up the general look of the entire screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // This makes our app scrollable
                .padding(16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // This is our app logo image
            Image(
                painter = painterResource(id = R.drawable.getgrinnected_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .size(250.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome Back",
                style = typography.headlineMedium,
                color = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Login to your account",
                style = typography.bodyLarge,
                color = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))
            // This is our textbox to handle users email input
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errEmail = false
                },
                label = { Text("Email", style = typography.labelLarge) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Email input field Icon",
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

            Spacer(modifier = Modifier.height(16.dp))

            // This handles displaying the specific errors of note
            if (errMsg.isNotEmpty()) {
                Text(
                    text = errMsg,
                    color = colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            // This is our login button handles an api request for login and if successful navigates
            // to main screen if not gives an error message
            Button(
                onClick = {
                    coroutineScope.launch {
                        errMsg = ""
                        // This does general field validation to insure stuff has at least been entered
                        if (email.isBlank()) {
                            errMsg = "Please enter email"
                            errEmail = true
                            return@launch // Escapes launch due to missing username
                        }


                        isLoading = true
                        try {
                            // Makes the api login request
                              val emailResponse = RetrofitApiClient.apiModel.login(LoginRequest(email))
                            // Assess if the request and validation of login was successful if so
                            // nav to main if not show login failure.
                            if (emailResponse.isSuccessful) {
                                // Sets our pending verification states so that we return to the proper
                                // page if we leave the app to check an the email
                                DataStoreSettings.setPendingVerification(context, email, signUp)
                                navController.navigate("verification/${email}/${signUp}") {
                                    popUpTo(0) { inclusive = true }
                                    launchSingleTop = true
                            }
                            } else {
                                errMsg = if(emailResponse.errorBody()?.string()?.contains("No such user") == true){
                                    "No user found with that email"
                                } else{
                                    "Login Failed"
                                }
                            }
                            // Failure specifically with a network connection ie couldn't leave our app
                            } catch (e: Exception) {
                               errMsg = "Network error: ${e.localizedMessage}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Logging in..." else "Login", style = typography.labelLarge)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // This is to properly align our link to signup page with a intuitive message.
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New to GetGrinnected?",
                    style = typography.bodyMedium,
                    color = colorScheme.onBackground
                )
                TextButton(onClick = { navController.navigate("signup") }) {
                    Text(
                        text = "Join now",
                        style = typography.labelLarge,
                        modifier = modifier.semantics { contentDescription = "Sign Up for an account" }
                    )
                }
            }
            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    LoginScreen(modifier = Modifier, navController = rememberNavController())
}