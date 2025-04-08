package com.example

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.AppNavigation
import com.example.myapplication.MainPage

@RunWith(AndroidJUnit4::class)
class MainActivityKtTest {

 @get:Rule
 val composeTestRule = createComposeRule()

 /**
  * Tests whether we can go from the welcome screen to the login screen
  */

 @Test
 fun welcomeScreen_LoginButton_NavigatesCorrectly() {
  composeTestRule.setContent { AppNavigation(darkTheme = false, onToggleTheme =  {}) }

  composeTestRule.onNodeWithText("Login").performClick()

  composeTestRule.waitUntil(timeoutMillis = 5_000) {
   composeTestRule.onAllNodesWithText("Login to your account").fetchSemanticsNodes().isNotEmpty()
  }
  composeTestRule.onNodeWithText("Login to your account").assertIsDisplayed()
 }

 /**
  * Tests whether we can go from the welcome screen to the signup screen
  */

 @Test
 fun welcomeScreen_SignUpButton_NavigatesCorrectly() {
  composeTestRule.setContent { AppNavigation(darkTheme = false, onToggleTheme =  {}) }

  composeTestRule.onNodeWithText("Sign Up").performClick()

  composeTestRule.waitUntil(timeoutMillis = 5_000) {
   composeTestRule.onAllNodesWithText("Create a free account").fetchSemanticsNodes().isNotEmpty()
  }
  composeTestRule.onNodeWithText("Create a free account").assertIsDisplayed()
 }

 /**
  * Tests whether we can go from the welcome screen to the signup and then use the
  * Signin button to go to the signup page
  */

 /*
 @Test
 fun signupScreen_SigninButton_NavigatesCorrectly() {
  composeTestRule.setContent { AppNavigation() }

  composeTestRule.waitUntil(timeoutMillis = 5_000) {
   composeTestRule.onAllNodesWithText("Sign Up").fetchSemanticsNodes().isNotEmpty()
  }
  composeTestRule.onNodeWithText("Sign Up").assertExists().performClick()

  composeTestRule.waitUntil(timeoutMillis = 10_000) {
   composeTestRule.onAllNodesWithText("Sign in").fetchSemanticsNodes().isNotEmpty()
  }
  composeTestRule.onNodeWithText("Sign in").assertExists().performClick()

  composeTestRule.waitUntil(timeoutMillis = 10_000) {
   composeTestRule.onAllNodesWithText("Login to your account").fetchSemanticsNodes().isNotEmpty()
  }
  composeTestRule.onNodeWithText("Login to your account").assertIsDisplayed()
 }
*/

 /**
  * Tests whether we can go from the welcome screen to the login and then use the
  * join now button to go to the signup page
  */
 @Test
 fun loginScreen_SignupButton_NavigatesCorrectly() {
  composeTestRule.setContent { AppNavigation(darkTheme = false, onToggleTheme =  {}) }

  composeTestRule.onNodeWithText("Login").performClick()

  composeTestRule.waitUntil(timeoutMillis = 5_000) {
   composeTestRule.onAllNodesWithText("Join now").fetchSemanticsNodes().isNotEmpty()
  }
  composeTestRule.onNodeWithText("Join now").assertExists().performClick()

  composeTestRule.waitUntil(timeoutMillis = 5_000) {
   composeTestRule.onAllNodesWithText("Create a free account").fetchSemanticsNodes().isNotEmpty()
  }
  composeTestRule.onNodeWithText("Create a free account").assertIsDisplayed()
 }

 /**
  * Parameterized Test to ensure the navigation bar works as anticipated.
  */
 @Test
 fun mainPage_Navbar_NavigatesToAllScreens() {
  val destinations = listOf(
   NavTestData("Calendar", "April"),
   NavTestData("Favorites", "Favorite Events"),
   NavTestData("Settings", "Profile"),
  )

  composeTestRule.setContent {
   MainPage(darkTheme = false, onToggleTheme =  {})
  }

  destinations.forEach { (tabText, expectedScreenText) ->
   composeTestRule.onNodeWithText(tabText).performClick()

   composeTestRule.waitUntil(timeoutMillis = 5_000) {
    composeTestRule.onAllNodesWithText(expectedScreenText).fetchSemanticsNodes().isNotEmpty()
   }

   composeTestRule.onNodeWithText(expectedScreenText).assertIsDisplayed()
  }
 }

 /**
  * Data class used for testing navigation
  */
 data class NavTestData(
  val navText: String,
  val expectedScreenText: String
 )

}
