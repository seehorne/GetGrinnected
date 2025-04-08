package com.example.myapplication


/**
 * This class is used to define the account data type
 * accountid: Integer associated with the unique id for an account
 * account_name: String the username of the account
 * email: String the email associated with the account
 * password: String the password associated with the account
 * profile_picture: String path to the picture for the user profile
 * favorited_events: List<Integer> List of event ids a user has favorited
 * drafted_events: List<Integer> List of event ids a user has in drafts
 * account_description: String a description about the user (specifically used for orgs)
 * account_role: Int used to determine the type of account a user has (Admin, org, normal)
 * is_followed: Boolean used specifically for org accounts to determine if they are followed or not by a user.
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

