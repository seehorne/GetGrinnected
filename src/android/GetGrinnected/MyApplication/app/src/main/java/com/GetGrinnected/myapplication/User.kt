package com.GetGrinnected.myapplication


/**
 * This class is used to define the account data type
 * @property accountid: Integer associated with the unique id for an account
 * @property account_name: String the username of the account
 * @property email: String the email associated with the account
 * @property profile_picture: String path to the picture for the user profile
 * @property favorited_events: List<Integer> List of event ids a user has favorited
 * @property notified_events List<Integer> List of event ids a user has notifications on for
 * @property drafted_events: List<Integer> List of event ids a user has in drafts
 * @property account_description: String a description about the user (specifically used for orgs)
 * @property account_role: Int used to determine the type of account a user has (Admin, org, normal)
 * @property is_followed: Boolean used specifically for org accounts to determine if they are followed or not by a user.
 */

data class User(
    val accountid: Int,
    val account_name: String,
    val email: String,
    val profile_picture: String,
    val favorited_events: List<Int>,
    val drafted_events: List<Int>,
    val favorited_tags: List<String>,
    val notified_events: List<Int>,
    val account_description: String,
    val account_role: Int,
    val is_followed: Boolean = false
)

