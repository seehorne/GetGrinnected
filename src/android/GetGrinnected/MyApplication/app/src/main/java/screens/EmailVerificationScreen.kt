package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.DataStoreSettings
import com.example.myapplication.R
import com.example.myapplication.RetrofitApiClient
import kotlinx.coroutines.launch
import com.example.myapplication.AppRepository
import com.example.myapplication.LoginRequest
import com.example.myapplication.VerifyRequest
import com.example.myapplication.toAccountEntity

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
fun EmailVerificationScreen(email: String, flag: Boolean, navController: NavController, modifier: Modifier = Modifier) {
    // The code the user inputs
    var codeInput by remember { mutableStateOf("") }
    // General error messages
    var errMsg by remember { mutableStateOf("") }
    // Process to launch background tasks
    val coroutineScope = rememberCoroutineScope()
    // To access our theme colors
    val colorScheme = MaterialTheme.colorScheme
    // To access our font info from our theme
    val typography = MaterialTheme.typography
    // The current context of our app
    val context = LocalContext.current
    // Boolean associated with specifically a code error to shift field color
    var errCode by remember { mutableStateOf(false) }

    // This sets up all of our elements in a column layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
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
                painter = painterResource(id = R.drawable.getgrinnected_logo),
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
                    errCode = false
                },
                label = { Text("Verification Code", style = typography.labelLarge) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Verification Icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                isError = errCode,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // This handles displaying of errors
            if (errMsg.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errMsg,
                    color = colorScheme.error,
                    style = typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // This is our verify button
            Button(onClick = {
                coroutineScope.launch {
                    // Checks that the code input is 6 characters long since we hard
                    // set that it can't be more than 6 in the input field
                    if (codeInput.length < 6) {
                        errMsg = "Please enter 6-digit code"
                        errCode = true
                        return@launch // Escapes launch due to missing username
                    }

                    try {
                        // Contact the api to see if our OTP is correct
                        val response = RetrofitApiClient.apiModel.verifyOTP(
                            VerifyRequest(email = email, code = codeInput)
                        )
                        // If it is we continue
                        if (response.isSuccessful && response.body()?.access_token != null) {
                            // Store our access token as a variable
                            val authToken = response.body()?.access_token
                            // Sets our access token to the token we obtained
                            DataStoreSettings.setAccessToken(context, authToken!!)
                            // Sets our refresh token to the token we obtained
                            DataStoreSettings.setRefreshToken(context, response.body()?.refresh_token!!)
                            try {
                                // Make a second call to get the full account data
                                val accountResponse = RetrofitApiClient.apiModel.getUserData("Bearer $authToken")

                                if (accountResponse.isSuccessful)
                                {
                                    // Gets the response as a user data type
                                    val userData = accountResponse.body()

                                    // If what is returned is non null
                                    if (userData != null) {
                                        // Converts user returned to an accountEntity
                                        val accountEntity = userData.toAccountEntity()
                                        // Upserts the account into our local Repo
                                        AppRepository.upsertAccount(accountEntity)
                                        // Sets our current account from the given id
                                        AppRepository.setCurrentAccountById(accountEntity.accountid)
                                        // Sets a persistent state for our logged in account via the id to reference else where in the app
                                        DataStoreSettings.setLoggedInAccountId(context, accountEntity.accountid)
                                        // Sets storage preference logged in to true
                                        DataStoreSettings.setLoggedIn(context, true)
                                        // Gets the current time
                                        val now = System.currentTimeMillis()
                                        // Syncs from API
                                        AppRepository.syncFromApi()
                                        // Sets the new LastSyncTime to now
                                        DataStoreSettings.setLastSyncTime(context, now)
                                        // Navigates to main page
                                        navController.navigate("main") {
                                            popUpTo(0) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    }
                                    // Handle if we authorization
                                } else {
                                    errMsg = accountResponse.errorBody()?.string() ?: "Failed to get user data."
                                }
                                // Handle network error if we can't leave the app for some reason
                            } catch(e: Exception){
                                errMsg = "Network error: ${e.localizedMessage}"
                            }
                            // Handles error if we couldn't verify the code or it was wrong
                        } else {
                            errMsg = "Incorrect code"
                            errCode = true
                        }
                        // Failure specifically with a network connection ie couldn't leave our app
                    } catch (e: Exception) {
                        errMsg = "Network error: ${e.localizedMessage}"
                    }
                }
            }) {
                Text("Verify", style = typography.labelLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // This is our resend code button that will resend the verification code
            TextButton(onClick = {
                coroutineScope.launch {
                    errMsg = try {
                        // Gets a login response to send us another email
                        val response = RetrofitApiClient.apiModel.login(LoginRequest(email))
                        // If this is successful we tell them we sent an email
                        if (response.isSuccessful) {
                            "A new code has been sent to your email."
                        } else {
                            // Else we have some error for why we couldn't
                            response.errorBody()?.string() ?: "Failed to resend code."
                        }
                        // This catches an issue where we couldn't leave the app
                    } catch (e: Exception) {
                        "Network error: ${e.localizedMessage}"
                    }
                }
            }) {
                Text(
                    "Resend Code",
                    style = typography.labelLarge,
                    color = colorScheme.primary
                )
            }

            // This is our cancel button that navigates back to the sign up page
            TextButton(onClick = {
                if (flag) {
                    navController.navigate("signup") {
                        popUpTo("verification") { inclusive = true }
                    }
                } else {
                    navController.navigate("login") {
                        popUpTo("verification") { inclusive = true }
                    }
                }
            }) {
                Text(
                    "Cancel",
                    style = typography.labelLarge,
                    color = colorScheme.primary
                )
            }
        }
    }
}


/**
 * A preview of our email verification screen for UI editing purposes only
 */
@Preview (showBackground = true)
@Composable
fun EmailVerificationScreenPreview(){
    EmailVerificationScreen(modifier= Modifier, email= "", flag = true, navController = rememberNavController())
}
