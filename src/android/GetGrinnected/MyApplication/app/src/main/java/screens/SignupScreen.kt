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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R

/**
 * A composable function that represents the Signup screen of our application.
 *
 * This screen includes input fields for username, email, and password (That are all Strings),
 * along with a sign-up button (To navigate to the homepage/complete account creation)
 * and a sign in navigation option for users who already have an account.
 *
 * @param modifier modifier applied to the screen layout
 * @param navController used to move through the app
 */

@Composable
fun SignupScreen(modifier: Modifier, navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var confirmPassword by remember { mutableStateOf("") }
    var errPwd by remember { mutableStateOf(false) }
    var errMsg by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.gg_logo_2),
            contentDescription = "App Logo",
            modifier = Modifier
                .fillMaxWidth()
                .size(250.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Welcome to GetGrinnected", fontSize = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = "Create a free account")

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
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

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
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

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                val validationError = validatePassword(it)
                errMsg = validationError ?: if (confirmPassword.isNotEmpty() && it != confirmPassword) {
                    "Passwords do not match"
                } else ""
                errPwd = errMsg.isNotEmpty()
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = errPwd,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                errMsg = if (password != it) {
                    "Passwords do not match"
                } else validatePassword(password) ?: ""
                errPwd = errMsg.isNotEmpty()
            },
            label = { Text("Retype Password") },
            visualTransformation = PasswordVisualTransformation(),
            isError = errPwd,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )

        if (errPwd) {
            Text(
                text = errMsg,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            ) //Everything I am seeing says we should start using this for style so that it can switch between light and dark mode and is scalable.
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick =  {
            navController.navigate("main"){
                popUpTo(0){inclusive = true}
                launchSingleTop = true
        } }) {
            Text("Sign up")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically){
            Text(text = "Already on GetGrinnected?")
            TextButton(onClick = {navController.navigate("login")}){
                Text(text = "Sign in")
            }
        }

        Spacer(modifier = Modifier.height(200.dp))
    }
}

/**
 * Validates a password meets the following parameters:
 * At least 8 characters long
 * Not longer that 256 characters
 * Has at least 1 uppercase letter
 * Has at least 1 lowercase letter
 * Has at least 1 number
 * Has at least 1 special character
 * If it meets all of these:
 * @return null which basically means it meets all the requirements other wise
 * it returns a string associated with the error it isn't meeting.
 */

fun validatePassword(pwd: String): String? {
    if (pwd.length < 8) return "Password must be at least 8 characters"
    if (pwd.length > 256) return "Password is too long"
    if (!pwd.any { it.isUpperCase() }) return "Password must contain an uppercase letter"
    if (!pwd.any { it.isLowerCase() }) return "Password must contain a lowercase letter"
    if (!pwd.any { it.isDigit() }) return "Password must contain a number"
    if (!pwd.any { "!@#$%^&*()-_=+[]{}|;:'\",.<>?/\\`~".contains(it) }) // I think this should cover all possible special characters
        return "Password must contain a special character"
    return null
}