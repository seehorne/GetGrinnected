package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.LoginRequest
import com.example.myapplication.R
import com.example.myapplication.RetrofitLoginClient
import kotlinx.coroutines.launch

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
    // Username input by user
    var username by remember { mutableStateOf("") }
    // Password input by user
    var password by remember { mutableStateOf("") }
    // Error Message to be displayed
    var errMsg by remember { mutableStateOf("") }
    // Process to launch background tasks
    val coroutineScope = rememberCoroutineScope()
    // Boolean to track whether our api is messaging and we need to halt input
    var isLoading by remember { mutableStateOf(false)}
    // Boolean associated with specifically a password error to shift field color
    var errPwd by remember { mutableStateOf(false) }
    // Boolean associated with specifically a username error to shift field color
    var errUsername by remember { mutableStateOf(false) }

    // Manages Keyboard focus and allows us to pull to specific locations
    val focusManager = LocalFocusManager.current

    // This sets up the general look of the entire screen
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
            painter = painterResource(id = R.drawable.gg_logo_2),
            contentDescription = "App Logo",
            modifier = Modifier
                .fillMaxWidth()
                .size(250.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Welcome Back", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Login to your account")

        Spacer(modifier = Modifier.height(8.dp))
        // This is our username text box that takes in our user's password
        OutlinedTextField(
            value = username,
            onValueChange = { username = it
                            errUsername = false},
            label = { Text("Username") },
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
        // This is the password text box that takes in our user's password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it
                            errPwd = false},
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = errPwd,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
                    ),
            keyboardActions = KeyboardActions(
                onDone ={
                    focusManager.clearFocus()
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // This handles displaying the specific errors of note
        if (errMsg.isNotEmpty()) {
            Text(
                text = errMsg,
                color = androidx.compose.ui.graphics.Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        // This is our login button handles an api request for login and if successful navigates
        // to main screen if not gives an error message
        Button(onClick = {
            coroutineScope.launch {
                errMsg = ""
                // This does general field validation to insure stuff has at least been entered
                if (username.isBlank()){
                    errMsg = "Please enter username"
                    errUsername = true
                    return@launch // Escapes launch due to missing username
                }

                if (password.isBlank()) {
                    errMsg = "Please enter password"
                    errPwd = true
                    return@launch // Escapes launch if they are missing info
                }

                isLoading = true
                try {
                    // Makes the api login request
                    val response = RetrofitLoginClient.authModel.login(
                        LoginRequest(username, password)
                    )
                    // Assess if the request and validation of login was successful if so
                    // nav to main if not show login failure.
                    if (response.isSuccessful && response.body()?.success == true) {
                        navController.navigate("main") {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        errMsg = response.body()?.message ?: "Login failed"
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
            Text(if (isLoading) "Logging in..." else "Login")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Forgot Password?", modifier = Modifier.clickable {  })

        Spacer(modifier = Modifier.height(32.dp))

        // This is to properly align our link to signup page with a intuitive message.
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically){
            Text(text = "New to GetGrinnected?")
            TextButton(onClick = {navController.navigate("signup")}) { Text(text = "Join now")}
        }
        Spacer(modifier = Modifier.height(200.dp))
    }
}
