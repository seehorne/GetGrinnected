package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R

/**
 * A composable function that represents the Welcome screen of our application.
 *
 * This screen includes a sign-up button (To navigate to account creation) and
 * a login button (To navigate to account login).
 *
 *  @param modifier modifier applied to the screen layout
 *  @param navController used to move through the app
 */

@Composable
fun WelcomeScreen(modifier: Modifier, navController: NavController) {
    // To access our theme colors
    val colorScheme = MaterialTheme.colorScheme
    // To access our font info from our theme
    val typography = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize(),
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

        Text(text = "Welcome to GetGrinnected",
            style = typography.headlineMedium,
            color = colorScheme.onBackground)

        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = {navController.navigate("login"){
                popUpTo("welcome"){inclusive = true}
            } }) {
                Text("Login", style = typography.labelLarge)
            }
            Button(onClick = {navController.navigate("signup"){
                popUpTo("welcome"){inclusive = true}
            } }) {
                Text("Sign Up", style = typography.labelLarge)
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun WelcomeScreenPreview(){
    WelcomeScreen(modifier = Modifier, navController = rememberNavController())
}