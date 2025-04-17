package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.LoginRequest
import com.example.myapplication.R
import com.example.myapplication.RetrofitApiClient
import com.example.myapplication.SignupRequest
import kotlinx.coroutines.launch

/**
 * A composable function that represents the email verification screen of our application.
 *
 * This screen includes the ability to resend a code or to cancel verification to navigate back
 * to the signup screen.
 *
 * @param modifier modifier applied to the screen layout
 * @param navController used to move through the app
 */
@Composable
fun EmailVerificationScreen(email: String, flag: Boolean, username: String, navController: NavController, modifier: Modifier = Modifier) {
    // The code the user inputs
    var codeInput by remember { mutableStateOf("") }
    // General error messages
    var errMsg by remember { mutableStateOf("") }
    // The real code to compare against
    val validCode = "123456" // TODO: LOGIC TO GET CORRECT CODE
    // Process to launch background tasks
    val coroutineScope = rememberCoroutineScope()
    // To access our theme colors
    val colorScheme = MaterialTheme.colorScheme
    // To access our font info from our theme
    val typography = MaterialTheme.typography

    // This sets up all of our elements in a column layout
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // This is our app logo image
        Image(
            painter = painterResource(id = R.drawable.gg_logo_2),
            contentDescription = "App Logo",
            modifier = Modifier
                .fillMaxWidth()
                .size(250.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Enter the 6-digit verification code sent to your email",
            style = typography.titleLarge,
            color = colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // This is the text field they enter the verification code into
        OutlinedTextField(
            value = codeInput,
            onValueChange = {
                if (it.length <= 6) codeInput = it
            },
            label = { Text("Verification Code", style = typography.labelLarge) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        // This handles displaying of errors
        if (errMsg.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errMsg,
                color = colorScheme.error,
                style = typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // This is our verify button
        Button (onClick = {
            if (codeInput == validCode) {
                coroutineScope.launch {
                    try {
                        if (flag){
                            // Makes the api signup request
                            val response = RetrofitApiClient.apiModel.signup(
                                SignupRequest(email = email, account_username = username)
                            )
                            // Assess if the request and creation of account was successful if so
                            // nav to main if not show signup failure.
                            if (response.isSuccessful && response.body()?.success == true) {
                                navController.navigate("main") {
                                    popUpTo(0) { inclusive = true }
                                    launchSingleTop = true
                                }
                            } else {
                                errMsg = response.body()?.message ?: "Sign up failed"
                            }
                        } else {
                            // Makes the api login request
                            val response = RetrofitApiClient.apiModel.login(
                                LoginRequest(email = email)
                            )
                            // Assess if the request for login was successful if so
                            // nav to main if not show login failure.
                            if (response.isSuccessful && response.body()?.success == true) {
                                navController.navigate("main") {
                                    popUpTo(0) { inclusive = true }
                                    launchSingleTop = true
                                }
                            } else {
                                errMsg = response.body()?.message ?: "login failed"
                            }
                        }
                        // Failure specifically with a network connection ie couldn't leave our app
                    } catch (e: Exception) {
                        errMsg = "Network error: ${e.localizedMessage}"
                    }
                }
            } else {
                errMsg = "Incorrect code. Please try again."
            }
        }) {
            Text("Verify", style = typography.labelLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // This is our resend code button that will resend the verification code
        TextButton (onClick = {
            // TODO: LOGIC TO RESEND CODE
            errMsg = "A new code has been sent to your email."
        }) {
            Text("Resend Code",
                style = typography.labelLarge,
                color = colorScheme.primary)
        }

        // This is our cancel button that navigates back to the sign up page
        TextButton(onClick = {
            if(flag) {
                navController.navigate("signup") {
                    popUpTo("verification") { inclusive = true }
                }
            } else{
                navController.navigate("login") {
                    popUpTo("verification") { inclusive = true }
                }
            }
        }) {
            Text("Cancel",
                style = typography.labelLarge,
                color = colorScheme.primary)
        }
    }
}


/**
 * A preview of our email verification screen for UI editing purposes only
 */
@Preview (showBackground = true)
@Composable
fun EmailVerificationScreenPreview(){
    EmailVerificationScreen(modifier= Modifier, username = "user", email= "", flag = true, navController = rememberNavController())
}
