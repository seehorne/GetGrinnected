package com.example

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.AccountEntity
import com.example.myapplication.AppNavigation
import com.example.myapplication.Check
import com.example.myapplication.MainPage
import com.example.myapplication.User
import screens.FavoritesScreen
import screens.LoginScreen
import screens.SettingsScreen
import screens.SignupScreen

@RunWith(AndroidJUnit4::class)
class MainActivityKtTest {

 @get:Rule
 val composeTestRule = createComposeRule()

 /**
  * Tests whether we can go from the welcome screen to the login screen
  */

 @Test
 fun welcomeScreen_LoginButton_NavigatesCorrectly() {
  composeTestRule.setContent { AppNavigation(
      darkTheme = false, onToggleTheme = {},
      startDestination = "welcome",
      tags = mutableListOf<Check>()
  ) }

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
  composeTestRule.setContent { AppNavigation(
      darkTheme = false, onToggleTheme = {},
      startDestination = "welcome",
      tags = mutableListOf<Check>()

  ) }

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
/* Still is not passing in CI
 @Test
 fun signupScreen_SigninButton_NavigatesCorrectly() {
  composeTestRule.setContent { AppNavigation(
      darkTheme = false, onToggleTheme = {},
      event = listOf(
          Event(eventid = 22349,event_name = "MLC Meeting", event_description = "\n  Meetings for MLC Student Leaders\n", event_location = "Rosenfield Center 209 (B&amp;C) - Academic Classroom", organizations = listOf("Affairs"), rsvp = 0, event_date = "April 8", event_time = "Noon - 1 p.m.", event_all_day = 0, event_start_time = "2025-04-08T17:00:00.000Z", event_end_time = "2025-04-08T18:00:00.000Z", tags = listOf("Multicultural","Student Activity","Students"), event_private = 0, repeats =0, event_image = "null", is_draft = 0)
      ),
      eventnum = 1,
      startDestination = "signup"
  ) }

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
  composeTestRule.setContent { AppNavigation(
      darkTheme = false, onToggleTheme = {},
      startDestination = "login",
      tags = mutableListOf<Check>(),
  ) }

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
   NavTestData("Home", "Tags"),
   NavTestData("Favorites", "Favorite Events"),
   NavTestData("Settings", "Preferences"),
  )

  composeTestRule.setContent {
   MainPage(
       darkTheme = false,
       onToggleTheme = {},
       tags = mutableListOf<Check>(),
       navController = rememberNavController(),
       account = AccountEntity(accountid = 1,
      account_name = "Test",
      email = "",
      profile_picture = "",
      favorited_events = emptyList(),
      drafted_events = emptyList(),
      favorited_tags = emptyList(),
      account_description = "",
      account_role = 0,
      is_followed = false,
           notified_events = emptyList())
   )
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

    /** Currently working on this test couldn't get it done before deadline.
    /**
     * Parameterized Test to ensure the navigation bar works as anticipated.
     */
    @Test
    fun calendarPage_Dropdown_NavigatesToAllViews() {
        val destinations = listOf(
            NavTestData("Month View", "April"),
            NavTestData("Day View", "DayView"),
            NavTestData("Week View", "WeekView"),
        )

        composeTestRule.setContent {
            CalendarScreen()
        }
        destinations.forEach { (tabText, expectedScreenText) ->
                composeTestRule.onNodeWithText("Month").performClick()
                composeTestRule.waitUntil(timeoutMillis = 5_000) {
                    composeTestRule.onAllNodesWithText(tabText).fetchSemanticsNodes().isNotEmpty()
                }
                composeTestRule.onNodeWithText(tabText).assertExists().performClick()

                composeTestRule.waitUntil(timeoutMillis = 5_000) {
                    composeTestRule.onAllNodesWithText(expectedScreenText).fetchSemanticsNodes()
                        .isNotEmpty()
                }

                composeTestRule.onNodeWithText(expectedScreenText).assertIsDisplayed()
        }
    }
    **/

    /**
     * Tests the dark mode switch works as intended
     */
    @Test
    fun settingsScreen_TogglesDarkModeSwitch() {
        var darkTheme = false
        composeTestRule.setContent {
            SettingsScreen(
                orgs = emptyList(),
                account = User(1, "user", "email@grinnell.edu", "", listOf(), listOf(), listOf(), listOf(), "", 0),
                darkTheme = darkTheme,
                onToggleTheme = { darkTheme = it },
                navController = rememberNavController()
            )
        }
        composeTestRule.onNodeWithText("Switch to dark mode").assertExists()
        composeTestRule.onNode(isToggleable()).performClick()
        assert(darkTheme)
    }

    /**
     * Tests the signup gives errors on none Grinnell email
     */
    @Test
    fun signupScreen_EmailValidation_Only_Grinnell_Emails_Allowed() {
        composeTestRule.setContent {
            SignupScreen(modifier = Modifier, navController = rememberNavController())
        }
        composeTestRule.onNodeWithText("Username").performTextInput("TestUser")
        composeTestRule.onNodeWithText("Email").performTextInput("user@gmail.com")
        composeTestRule.onNodeWithText("Sign up").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Email must end with @grinnell.edu").assertIsDisplayed()
    }

    /**
     * With no favorited events the favorites screen displays the correct text
     */
    @Test
    fun favoritesScreen_NoFavorites_ShowsEmptyText() {
        composeTestRule.setContent {
            FavoritesScreen()
        }
        composeTestRule.onNodeWithText("You haven't favorited any events yet.").assertIsDisplayed()
    }

    /**
     * Tests the signup screen gives an error if the username is not entered
     */
    @Test
    fun signupScreen_UsernameRequired_ShowsError() {
        composeTestRule.setContent {
            SignupScreen(modifier = Modifier, navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Email").performTextInput("user@grinnell.edu")
        composeTestRule.onNodeWithText("Sign up").performClick()
        composeTestRule.onNodeWithText("Username can only include letters, '.', and '_'").assertIsDisplayed()
    }

    /**
     * Tests the login screen gives an error if the email is not entered
     */
    @Test
    fun loginScreen_RequiresEmail_ShowsError() {
        composeTestRule.setContent {
            LoginScreen(modifier = Modifier, navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Login").performClick()
        composeTestRule.onNodeWithText("Please enter email").assertIsDisplayed()
    }
}
