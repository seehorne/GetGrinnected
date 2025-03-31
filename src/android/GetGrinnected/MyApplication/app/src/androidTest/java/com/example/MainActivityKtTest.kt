package com.example

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.AppNavigator

@RunWith(AndroidJUnit4::class)
class MainActivityKtTest {

 @get:Rule
 val composeTestRule = createComposeRule()

 @Test
 fun welcomeScreen_LoginButton_NavigatesCorrectly() {
  composeTestRule.setContent { AppNavigator() }

  composeTestRule.onNodeWithText("Login").performClick()
  composeTestRule.onNodeWithText("Login Page").assertIsDisplayed()
 }

 @Test
 fun welcomeScreen_SignUpButton_NavigatesCorrectly() {
  composeTestRule.setContent { AppNavigator() }

  composeTestRule.onNodeWithText("Sign Up").performClick()
  composeTestRule.onNodeWithText("Sign Up Page").assertIsDisplayed()
 }

 @Test
 fun backButton_NavigatesCorrectly() {
  composeTestRule.setContent { AppNavigator() }

  composeTestRule.onNodeWithText("Sign Up").performClick()
  composeTestRule.onNodeWithText("Back").performClick()
  composeTestRule.onNodeWithText("Welcome to GetGrinnected").assertIsDisplayed()
 }
}
