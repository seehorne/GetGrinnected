package com.example.myapplication


/**
 * This class is used to define the account data type
 */

data class User(
    val accountid: Int,
    val account_name: String,
    val email: String,
    val password: String,
    val profile_picture: String,
    val favorited_events: List<Int>,
    val drafted_events: List<Int>,
    val favorited_tags: List<String>,
    val account_description: String,
    val account_role: Int,
    val is_followed: Boolean = false
)

