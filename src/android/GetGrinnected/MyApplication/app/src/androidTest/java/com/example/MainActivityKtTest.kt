package com.example

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.AppNavigation

@RunWith(AndroidJUnit4::class)
class MainActivityKtTest {

 @get:Rule
 val composeTestRule = createComposeRule()

 @Test
 fun welcomeScreen_LoginButton_NavigatesCorrectly() {
  composeTestRule.setContent { AppNavigation() }

  composeTestRule.onNodeWithText("Login").performClick()

  composeTestRule.waitUntil(timeoutMillis = 5_000) {
   composeTestRule.onAllNodesWithText("Login to your account").fetchSemanticsNodes().isNotEmpty()
  }
  composeTestRule.onNodeWithText("Login to your account").assertIsDisplayed()
 }

 @Test
 fun welcomeScreen_SignUpButton_NavigatesCorrectly() {
  composeTestRule.setContent { AppNavigation() }

  composeTestRule.onNodeWithText("Sign Up").performClick()

  composeTestRule.waitUntil(timeoutMillis = 5_000) {
   composeTestRule.onAllNodesWithText("Create a free account").fetchSemanticsNodes().isNotEmpty()
  }
  composeTestRule.onNodeWithText("Create a free account").assertIsDisplayed()
 }

 @Test
 fun signupScreen_SigninButton_NavigatesCorrectly() {
  composeTestRule.setContent { AppNavigation() }

  composeTestRule.waitUntil(timeoutMillis = 5_000) {
   composeTestRule.onAllNodesWithText("Sign up").fetchSemanticsNodes().isNotEmpty()
  }
  composeTestRule.onNodeWithText("Sign up").assertExists().performClick()

  composeTestRule.waitUntil(timeoutMillis = 5_000) {
   composeTestRule.onAllNodesWithText("Sign in").fetchSemanticsNodes().isNotEmpty()
  }
  composeTestRule.onNodeWithText("Sign in").assertExists().performClick()

  composeTestRule.waitUntil(timeoutMillis = 5_000) {
   composeTestRule.onAllNodesWithText("Login to your account").fetchSemanticsNodes().isNotEmpty()
  }
  composeTestRule.onNodeWithText("Login to your account").assertIsDisplayed()
 }

 @Test
 fun loginScreen_SignupButton_NavigatesCorrectly() {
  composeTestRule.setContent { AppNavigation() }

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
}
