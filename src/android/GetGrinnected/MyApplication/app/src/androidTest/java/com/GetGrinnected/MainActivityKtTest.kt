package com.GetGrinnected

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.GetGrinnected.myapplication.AccountEntity
import com.GetGrinnected.myapplication.AppNavigation
import com.GetGrinnected.myapplication.Check
import com.GetGrinnected.myapplication.MainPage
import com.GetGrinnected.myapplication.User
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import screens.FavoritesScreen
import screens.LoginScreen
import screens.SettingsScreen
import screens.SignupScreen
import screens.validateEmail
import screens.validateUsername

@RunWith(AndroidJUnit4::class)
class MainActivityKtTest {

 @get:Rule
 val composeTestRule = createComposeRule()

    /* UI TESTING */
 /**
  * Tests whether we can go from the welcome screen to the login screen
  */

 @Test
 fun welcomeScreen_LoginButton_NavigatesCorrectly() {
  composeTestRule.setContent { AppNavigation(
      darkTheme = false, onToggleTheme = {},
      startDestination = "welcome",
      tags = mutableListOf<Check>(),
      fontSizeSetting = "M",
      onFontSizeChange = {}
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
      tags = mutableListOf<Check>(),
      fontSizeSetting = "M",
      onFontSizeChange = {}

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
 fun signUpScreen_SignInButton_NavigatesCorrectly() {
     composeTestRule.setContent { AppNavigation(
         darkTheme = false, onToggleTheme = {},
         startDestination = "signup",
         tags = mutableListOf<Check>(),
         fontSizeSetting = "M",
         onFontSizeChange = {}
     ) }

     composeTestRule.waitUntil(timeoutMillis = 5_000) {
         composeTestRule.onAllNodesWithText("Sign in").fetchSemanticsNodes().isNotEmpty()
     }
     composeTestRule.onNodeWithText("Sign in").assertExists().performClick()

     composeTestRule.waitForIdle()
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
      fontSizeSetting = "M",
      onFontSizeChange = {}
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
   //NavTestData("Calendar", "April"),
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
           notified_events = emptyList()),
       fontSizeSetting = "M",
       onFontSizeChange = {}
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

    /**
     * Tests the dark mode switch works as intended
     */
    @Test
    fun settingsScreen_TogglesDarkModeSwitch() {
        var darkTheme = false
        composeTestRule.setContent {
            SettingsScreen(
                account = User(1, "user", "email@grinnell.edu", "", listOf(), listOf(), listOf(), listOf(), "", 0),
                darkTheme = darkTheme,
                onToggleTheme = { darkTheme = it },
                navController = rememberNavController(),
                fontSizeSetting = "M",
                onFontSizeChange = {}
            )
        }
        composeTestRule.onNodeWithText("Appearance").performClick()
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
        composeTestRule.onNodeWithText("Username can only include letters, numbers, '.', and '_'").assertIsDisplayed()
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

    /**
     * Test to ensure that font size selection on the settings page
     * changes to the fontsize enum we have selected.
     */
    @Test
    fun settingsScreen_FontSizeDropdown_SetsSize() {
        var selectedFontSize = ""
        composeTestRule.setContent {
            SettingsScreen(
                account = User(1, "user", "email@grinnell.edu", "", listOf(), listOf(), listOf(), listOf(), "", 0),
                darkTheme = false,
                onToggleTheme = {},
                navController = rememberNavController(),
                fontSizeSetting = "M",
                onFontSizeChange = { selectedFontSize = it }
            )
        }
        composeTestRule.onNodeWithText("Accessibility").performClick()
        composeTestRule.onNodeWithText("Medium").performClick()
        composeTestRule.onNodeWithText("Large").performClick()
        assert(selectedFontSize == "L")
    }

    /* UNIT TESTING HELPER FUNCTIONS */

    // Tests for validateUsername Function

    /**
     * Test to ensure legal usernames return null
     */
    @Test
    fun validUsername_ReturnsNull() {
        // 3 possible examples of legal usernames within our scheme
        assertNull(validateUsername("anthony_yay"))
        assertNull(validateUsername("anthony.yay"))
        assertNull(validateUsername("anthony123"))
    }

    /**
     * Tests that if a username has characters not in our allowed list that we give it an error
     * explaining the characters allowed in a username
     */
    @Test
    fun usernameWithInvalidCharacters_ReturnsCorrectError() {
        assertEquals("Username can only include letters, numbers, '.', and '_'", validateUsername("@ops"))
    }

    /**
     * Tests that if a username has no characters that we tell the user they need at least
     * a single letter.
     */
    @Test
    fun usernameWithoutLetters_ReturnsCorrectError() {
        assertEquals("Username must contain at least one letter", validateUsername("123"))
    }

    /**
     * Tests that if a username has consecutive periods that we tell the user that they cannot have
     * multiple ..s in a row
     */
    @Test
    fun usernameWithConsecutivePeriods_ReturnsCorrectError() {
        assertEquals("Username cannot contain two or more '.' in a row", validateUsername("periods..woah"))
    }

    /**
     * Tests that if a username has consecutive underscores that we tell the user that they cannot have
     * multiple __s in a row
     */
    @Test
    fun usernameWithConsecutiveUnderscores_ReturnsCorrectError() {
        assertEquals("Username cannot contain two or more '_' in a row", validateUsername("yay__testing"))
    }

    /**
     * Tests that if a username has consecutive period and underscore that we tell the user that they cannot have
     * said pattern
     */
    @Test
    fun usernameWithPeriodUnderscore_ReturnsCorrectError() {
        assertEquals("Username cannot contain a '.' followed by a '_'", validateUsername("first._way"))
    }

    /**
     * Tests that if a username has consecutive underscore and period that we tell the user that they cannot have
     * said pattern
     */
    @Test
    fun usernameWithUnderscorePeriod_ReturnsCorrectError() {
        assertEquals("Username cannot contain a '_' followed by a '.'", validateUsername("other_.way"))
    }

    /**
     * Tests that if a username has a period or underscore as the starting letter that we report
     * the correct error in our return
     */
    @Test
    fun usernameStartsWithInvalidCharacter_ReturnsCorrectError() {
        assertEquals("Username cannot start with '.' or '_'", validateUsername(".start"))
        assertEquals("Username cannot start with '.' or '_'", validateUsername("_start"))
    }

    /**
     * Tests that if a username has a period or underscore as the ending letter that we report
     * the correct error in our return
     */
    @Test
    fun usernameEndsWithInvalidCharacter_ReturnsCorrectError() {
        assertEquals("Username cannot end with '.' or '_'", validateUsername("end."))
        assertEquals("Username cannot end with '.' or '_'", validateUsername("end_"))
    }

    /**
     * Tests that if a username is longer than 20 characters that we return the correct error
     * saying they can't do that.
     */
    @Test
    fun usernameExceedsMaxLength_ReturnsCorrectError() {
        assertEquals("Username must be no more that 20 characters long", validateUsername("superduperextralongusernamebecausewhynot"))
    }

    // Tests for validateEmail Function

    /**
     * Tests that a grinnell email returns null as we would expect
     */
    @Test
    fun validEmail_ReturnsNull(){
        assertNull(validateEmail("schwindt2@grinnell.edu"))
    }

    /**
     * Tests that a non-grinnell email returns the correct error
     */
    @Test
    fun invalidEmail_ReturnsCorrectError(){
        assertEquals("Email must end with @grinnell.edu", validateEmail("test@test.com"))
    }
}
